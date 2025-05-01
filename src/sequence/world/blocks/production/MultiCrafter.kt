package sequence.world.blocks.production

import arc.Core
import arc.func.Prov
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.scene.ui.Button
import arc.scene.ui.layout.Table
import arc.struct.EnumSet
import arc.struct.Seq
import arc.util.Eachable
import arc.util.Nullable
import arc.util.Strings
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.content.Fx
import mindustry.entities.Effect
import mindustry.entities.units.BuildPlan
import mindustry.gen.Building
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.logic.LAccess
import mindustry.type.Item
import mindustry.type.Liquid
import mindustry.ui.Bar
import mindustry.ui.Styles
import mindustry.world.consumers.ConsumeItems
import mindustry.world.consumers.ConsumeLiquids
import mindustry.world.consumers.ConsumePower
import mindustry.world.draw.DrawBlock
import mindustry.world.draw.DrawDefault
import mindustry.world.meta.*
import mindustry.world.modules.ItemModule
import mindustry.world.modules.LiquidModule
import mindustry.world.modules.PowerModule
import sequence.core.SeqElem
import sequence.core.SqStatValues.formulaStat
import sequence.ui.SqUI.formula
import sequence.util.Util
import sequence.world.blocks.imagine.ImagineBlock
import sequence.world.meta.Formula

class MultiCrafter(name: String) : ImagineBlock(name), SeqElem {
    val formulas = Seq<Formula>()
    var ord = -1
    var liquidOutputDirections = intArrayOf(-1)
    var onlyOneFormula = false
    var dumpExtraLiquid = true
    var ignoreLiquidFullness = false
    var craftEffect: Effect = Fx.none
    var updateEffect: Effect = Fx.none
    var updateEffectChance = 0.04f
    var warmupSpeed = 0.019f
    var drawer: DrawBlock = DrawDefault()
    private var computeOutputsItems: Boolean? = null

    init {
        buildType = Prov { MultiCrafterBuild() }
        configurable = true
        copyConfig = true
        update = true
        solid = true
        ambientSound = Sounds.machine
        sync = true
        ambientSoundVolume = 0.03f
        flags = EnumSet.of(BlockFlag.factory)
        drawArrow = false
        hasImagine = false
    }

    override fun setStats() {
        super.setStats()
//        ImagineBlocks.stats(this)
        if (!onlyOneFormula) return
        stats.timePeriod = formulas[0].time
        if (hasItems && itemCapacity > 0 || formulas[0].outputItem.isNotEmpty()) {
            stats.add(Stat.productionTime, formulas[0].time / 60f, StatUnit.seconds)
        }
        if (formulas[0].outputItem.isNotEmpty()) {
            stats.add(Stat.output, StatValues.items(formulas[0].time, *formulas[0].outputItem))
        }
        if (formulas[0].outputLiquid.isNotEmpty()) {
            stats.add(Stat.output, StatValues.liquids(1f, *formulas[0].outputLiquid))
        }
        if (formulas[0].outputPower > 0) {
            stats.add(Stat.basePowerGeneration, formulas[0].outputPower * 60f, StatUnit.powerSecond)
        }
        ConsumeItems(formulas[0].inputItem).display(stats)
        ConsumeLiquids(formulas[0].inputLiquid).display(stats)
        ConsumePower(formulas[0].inputPower, 0f, false).display(stats)
    }

    override fun setBars() {
        super.setBars()
        removeBar("items")
        removeBar("liquid")
//        ImagineBlocks.bars(this)
    }

    override fun rotatedOutput(x: Int, y: Int): Boolean {
        return false
    }

    override fun load() {
        super.load()
        drawer.load(this)
    }

    override fun init() {
        for (formula in formulas) {
            if (formula.inputLiquid.isNotEmpty() || formula.outputLiquid.isNotEmpty()) hasLiquids = true
            if (formula.outputLiquid.isNotEmpty()) outputsLiquid = true
            if (formula.outputItem.isNotEmpty() || formula.inputItem.isNotEmpty()) hasItems = true
            if (formula.inputPower > 0 || formula.outputPower > 0) hasPower = true
            if (formula.outputPower > 0) outputsPower = true
//            if (!formula.inputImagine.zero() || !formula.outputImagine.zero()) hasImagine = true
        }
        consumePowerDynamic { build: MultiCrafterBuild -> if (build.now == -1) 0f else build.formula!!.inputPower }
        if (onlyOneFormula) {
            configurable = false
        } else {
            config(Int::class.javaObjectType)
            { building: Building, integer: Int -> (building as MultiCrafterBuild).now = integer }
            configClear { building: Building -> (building as MultiCrafterBuild).now = -1 }
        }
        super.init()
    }

    override fun drawPlanRegion(plan: BuildPlan?, list: Eachable<BuildPlan?>?) {
        drawer.drawPlan(this, plan, list)
    }

    override fun icons(): Array<TextureRegion?>? {
        return drawer.finalIcons(this)
    }

    override fun outputsItems(): Boolean {
        if (computeOutputsItems == null) {
            computeOutputsItems = false
            for (formula in formulas) {
                if (formula.outputItem.isNotEmpty()) {
                    computeOutputsItems = true
                    break
                }
            }
        }
        return computeOutputsItems ?: false
    }

    override fun getRegionsToOutline(out: Seq<TextureRegion?>) {
        drawer.getRegionsToOutline(this, out)
    }

    fun addFormula(vararg forms: Formula?) {
        formulas.add(forms)
    }

    override fun order() = ord

    override fun statValue() = StatValue { table: Table ->
        table.row()
        table.table {
            it.add(Core.bundle["blocks.multi-crafter.seqstat"]).growX().left().row()
        }.growX().left().row()
        for (formula in formulas) {
            formulaStat(formula!!).display(table)
        }
    }

    inner class MultiCrafterBuild : Building() {
        private val all = Seq<Button>()
        var now = -1
        private var progress = 0f
        private var totalProgress = 0f
        private var warmup = 0f
        private var upd = false
        private var lastOutputPower = 0f

        override fun acceptItem(source: Building, item: Item): Boolean =
            now != -1 && Util.item(formula!!.inputItem, item) > 0 && items[item] < getMaximumAccepted(item)

        override fun acceptLiquid(source: Building, liquid: Liquid): Boolean =
            now != -1 && Util.liquid(formula!!.inputLiquid, liquid) > 0

        override fun getPowerProduction(): Float =
            if (now == -1) 0f else lastOutputPower

        @get:Nullable
        val formula: Formula?
            get() = if (now == -1) null else (block as MultiCrafter).formulas[now]

        override fun draw() {
            drawer.draw(this)
        }

        override fun drawLight() {
            super.drawLight()
            drawer.drawLight(this)
        }

        override fun shouldConsume(): Boolean {
            if (now == -1) return false
            for (output in formula!!.outputItem) {
                if (items[output.item] + output.amount > itemCapacity) {
                    return false
                }
            }
            if (!ignoreLiquidFullness) {
                var allFull = true
                for (output in formula!!.outputLiquid) {
                    if (liquids[output.liquid] >= liquidCapacity - 0.001f) {
                        if (!dumpExtraLiquid) {
                            return false
                        }
                    } else {
                        allFull = false
                    }
                }
                if (allFull) {
                    return false
                }
            }
            return enabled
        }

        override fun updateTile() {
            if (items == null) items = ItemModule()
            if (liquids == null) liquids = LiquidModule()
            if (power == null) power = PowerModule()
            dumpOutputs()
            if (now == -1) {
                if (onlyOneFormula) now = 0
                lastOutputPower = 0f
                return
            }
            var has = true
            var fullness = 0f
            var count = 0
            if (formula!!.inputPower > 0) {
                if (Mathf.zero(power.status)) {
                    has = false
                    count = 0x3f3f3f3f
                }
                fullness += Mathf.clamp(power.status)
                count++
            }
            for (stack in formula!!.inputLiquid) {
                if (liquids[stack.liquid] < stack.amount - 0.001f) has = false
                fullness += Mathf.clamp(liquids[stack.liquid] / stack.amount)
                count++
            }
            for (stack in formula!!.inputItem) {
                if (items[stack.item] < stack.amount) has = false
                fullness += Mathf.clamp(items[stack.item].toFloat() / stack.amount)
                count++
            }
            efficiency = if (count == 0) 1f else fullness / count
            if (efficiency > 0 && has) {
                progress += getProgressIncrease(formula!!.time)
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed)
                if (formula!!.liquidSecond) {
                    if (formula!!.outputLiquid.isNotEmpty()) {
                        val inc = getProgressIncrease(1f)
                        for (output in formula!!.outputLiquid) {
                            handleLiquid(
                                this,
                                output.liquid,
                                (output.amount * inc).coerceAtMost(liquidCapacity - liquids[output.liquid])
                            )
                        }
                    }
                }
                if (wasVisible && Mathf.chanceDelta(updateEffectChance.toDouble())) {
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4))
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed)
            }
            totalProgress += warmup * Time.delta
            if (progress >= 1f) {
                craft()
                progress %= 1f
            }
        }

        override fun getProgressIncrease(baseTime: Float): Float {
            if (ignoreLiquidFullness || now == -1) {
                return super.getProgressIncrease(baseTime)
            }
            var scaling = 1f
            var max = 1f
            if (formula!!.outputLiquid.isNotEmpty()) {
                max = 0f
                for (s in formula!!.outputLiquid) {
                    val value: Float = (liquidCapacity - liquids[s.liquid]) / (s.amount * edelta())
                    scaling = scaling.coerceAtMost(value)
                    max = max.coerceAtLeast(value)
                }
            }
            return super.getProgressIncrease(baseTime) * if (dumpExtraLiquid) max.coerceAtMost(1f) else scaling
        }

        fun warmupTarget(): Float {
            return 1f
        }

        override fun warmup(): Float {
            return warmup
        }

        override fun totalProgress(): Float {
            return totalProgress
        }

        override fun consume() {
            super.consume()
            items.remove(formula!!.inputItem)
            for (stack in formula!!.inputLiquid) {
                liquids.remove(stack.liquid, stack.amount)
            }
        }

        private fun craft() {
            consume()
            for (output in formula!!.outputItem) {
                for (i in 0 until output.amount) {
                    offload(output.item)
                    if (items[output.item] > itemCapacity) items[output.item] = itemCapacity
                }
            }
            if (!formula!!.liquidSecond) {
                for (output in formula!!.outputLiquid) {
                    handleLiquid(this, output.liquid, output.amount)
                    if (liquids[output.liquid] > liquidCapacity) liquids[output.liquid] = liquidCapacity
                }
            }
            dumpOutputs()
            lastOutputPower = formula!!.outputPower
            if (wasVisible) {
                craftEffect.at(x, y)
            }
        }

        private fun dumpOutputs() {
            if (timer(timerDump, dumpTime / timeScale)) {
                if (now == -1) {
                    dump()
                } else {
                    for (output in formula!!.outputItem) {
                        dump(output.item)
                    }
                }
            }
            if (now == -1) {
                for (liquid in Vars.content.liquids()) {
                    dumpLiquid(liquid)
                }
                return
            }
            for (i in formula!!.outputLiquid.indices) {
                val dir = if (liquidOutputDirections.size > i) liquidOutputDirections[i] else -1
                dumpLiquid(formula!!.outputLiquid[i].liquid, 2f, dir)
            }
        }

        override fun sense(sensor: LAccess): Double {
            if (sensor == LAccess.progress) return progress().toDouble()
            return if (sensor == LAccess.totalLiquids &&
                formula!!.outputLiquid.isNotEmpty()
            ) liquids[formula!!.outputLiquid[0].liquid].toDouble() else super.sense(sensor)
        }

        override fun progress(): Float {
            return Mathf.clamp(progress)
        }

        override fun getMaximumAccepted(item: Item): Int {
            return itemCapacity
        }

        override fun shouldAmbientSound(): Boolean {
            return efficiency > 0
        }

        override fun write(write: Writes) {
            super.write(write)
            write.f(progress)
            write.f(warmup)
            write.i(now)
            write.f(lastOutputPower)
        }

        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            progress = read.f()
            warmup = read.f()
            now = read.i()
            lastOutputPower = read.f()
        }

        override fun buildConfiguration(table: Table) {
            table.table(Styles.black6) { ta: Table ->
                val crafter = block as MultiCrafter
                all.clear()
                for (i in 0 until crafter.formulas.size) {
                    val button = formula(crafter.formulas[i])
                    button.isChecked = now == i
                    button.clicked {
                        if (button.isChecked) {
                            configure(i)
                            lastOutputPower = 0f
                            upd = true
                            for (button1 in all) {
                                button1.isChecked = false
                            }
                            button.isChecked = true
                        } else now = -1
                    }
                    all.add(button)
                    ta.add(button).left().height(56f)
                    ta.add().growX()
                    ta.row()
                }
            }.margin(5f).fillX().grow()
        }

        override fun config(): Any {
            return now
        }

        override fun displayBars(table: Table) {
            db(table)
            table.update {
                if (upd) {
                    upd = false
                    table.clear()
                    db(table)
                }
            }
        }

        private fun db(table: Table) {
            super.displayBars(table)
            if (now == -1) return
            if (formula!!.outputPower > 0) {
                table.add(Bar(
                    {
                        Core.bundle.format(
                            "bar.poweroutput",
                            Strings.fixed(this.powerProduction * 60 * timeScale(), 1)
                        )
                    },
                    { Pal.powerBar }
                ) { efficiency }).row()
            }
            for (stack in formula!!.inputItem) {
                table.add(Bar(
                    stack.item.localizedName,
                    stack.item.color
                ) { items[stack.item].toFloat() / itemCapacity }).row()
            }
            for (stack in formula!!.inputLiquid) {
                table.add(Bar(
                    stack.liquid.localizedName,
                    stack.liquid.color
                ) { liquids[stack.liquid] / liquidCapacity }).row()
            }
            for (stack in formula!!.outputItem) {
                if (Util.item(formula!!.inputItem, stack.item) > 0) return
                table.add(Bar(
                    stack.item.localizedName,
                    stack.item.color
                ) { items[stack.item].toFloat() / itemCapacity }).row()
            }
            for (stack in formula!!.outputLiquid) {
                if (Util.liquid(formula!!.inputLiquid, stack.liquid) > 0) return
                table.add(Bar(
                    stack.liquid.localizedName,
                    stack.liquid.color
                ) { liquids[stack.liquid] / liquidCapacity }).row()
            }
        }
    }
}