package sequence.core

import arc.Core
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.scene.ui.Image
import arc.scene.ui.ImageButton
import arc.scene.ui.layout.Cell
import arc.scene.ui.layout.Collapser
import arc.scene.ui.layout.Table
import arc.struct.ObjectMap
import arc.util.Scaling
import arc.util.Strings
import mindustry.Vars
import mindustry.content.StatusEffects
import mindustry.ctype.UnlockableContent
import mindustry.entities.bullet.BulletType
import mindustry.gen.Icon
import mindustry.type.LiquidStack
import mindustry.type.UnitType
import mindustry.ui.Styles
import mindustry.world.blocks.defense.turrets.Turret
import mindustry.world.meta.StatUnit
import mindustry.world.meta.StatValue
import mindustry.world.meta.StatValues.fixValue
import mindustry.world.meta.StatValues.withTooltip
import sequence.ui.SqUI.uiILPFormula
import sequence.util.IgnoredFragBullet
import sequence.world.entities.AdditionInfoBulletType
import sequence.world.meta.Formula

object SqStatValues {
    fun formulaStat(form: Formula): StatValue =
        StatValue { table: Table ->
            table.row()
            table.table(Styles.grayPanel) { t: Table ->
                t.table { bt: Table ->
                    bt.left().top().defaults().padRight(3f).left()
                    uiILPFormula(
                        form.inputItem,
                        form.inputLiquid,
                        form.inputPower * form.time,
                        form.inputImagine,
                        bt,
                        true
                    )
                    bt.add()
                    bt.table { ct: Table -> ct.add(Image(Icon.rightSmall)).grow().fill() }
                        .padLeft(10f).padRight(10f)
                    bt.add()
                    if (form.liquidSecond) {
                        val liquidStacks = Array(form.outputLiquid.size) {
                            LiquidStack(form.outputLiquid[it].liquid, form.outputLiquid[it].amount * form.time)
                        }
                        uiILPFormula(form.outputItem, liquidStacks, form.outputPower, form.outputImagine, bt, false)
                    } else uiILPFormula(
                        form.outputItem,
                        form.outputLiquid,
                        form.outputPower * form.time,
                        form.outputImagine,
                        bt,
                        true
                    )
                }.growX().left().row()
                t.add(
                    SqBundle.format(
                        "stat.crafttime",
                        Strings.autoFixed(form.time / 60f, 2)
                    )
                ).left()
            }.padLeft(0f).padTop(5f).padBottom(5f).growX().margin(10f)
        }

    fun <T : UnlockableContent> ammo(map: ObjectMap<T, BulletType>): StatValue {
        return ammo(map, nested = false, showUnit = false)
    }

    fun <T : UnlockableContent> ammo(map: ObjectMap<T, BulletType>, showUnit: Boolean): StatValue {
        return ammo(map, false, showUnit)
    }

    fun <T : UnlockableContent> ammo(map: ObjectMap<T, BulletType>, nested: Boolean, showUnit: Boolean): StatValue {
        return StatValue { table: Table ->
            table.row()
            val orderedKeys = map.keys().toSeq()
            orderedKeys.sort()
            for (t in orderedKeys) {
                val compact = t is UnitType && !showUnit || nested
                val type = map[t]
                if (type.spawnUnit != null && type.spawnUnit.weapons.size > 0) {
                    ammo(
                        ObjectMap.of(
                            t,
                            type.spawnUnit.weapons.first().bullet
                        ), nested, false
                    ).display(table)
                    continue
                }
                table.table(
                    Styles.grayPanel
                ) { bt: Table ->
                    bt.left().top().defaults().padRight(3f).left()
                    if (!compact && t !is Turret) {
                        bt.table { title: Table ->
                            title.image(icon(t)).size((3 * 8).toFloat()).padRight(4f).right()
                                .scaling(Scaling.fit)
                                .top()
                                .with { i: Image ->
                                    withTooltip(
                                        i,
                                        t,
                                        false
                                    )
                                }
                            title.add(t!!.localizedName).padRight(10f).left().top()
                            if (type.displayAmmoMultiplier && type.statLiquidConsumed > 0f) {
                                title.add("[stat]" + fixValue(type.statLiquidConsumed / type.ammoMultiplier * 60f) + " [lightgray]" + StatUnit.perSecond.localized())
                            }
                        }
                        bt.row()
                    }
                    if (type.damage > 0 && (type.collides || type.splashDamage <= 0)) {
                        if (type.continuousDamage() > 0) {
                            bt.add(
                                Core.bundle.format(
                                    "bullet.damage",
                                    type.continuousDamage()
                                ) + StatUnit.perSecond.localized()
                            )
                        } else {
                            bt.add(Core.bundle.format("bullet.damage", type.damage))
                        }
                    }
                    if (type.buildingDamageMultiplier != 1f) {
                        sep(
                            bt,
                            Core.bundle.format(
                                "bullet.buildingdamage",
                                ammoStat((type.buildingDamageMultiplier * 100 - 100).toInt().toFloat())
                            )
                        )
                    }
                    if (type.rangeChange != 0f && !compact) {
                        sep(
                            bt,
                            Core.bundle.format(
                                "bullet.range",
                                ammoStat(type.rangeChange / Vars.tilesize)
                            )
                        )
                    }
                    if (type.shieldDamageMultiplier != 1f) {
                        sep(
                            bt,
                            Core.bundle.format(
                                "bullet.shielddamage",
                                ammoStat((type.shieldDamageMultiplier * 100 - 100).toInt().toFloat())
                            )
                        )
                    }
                    if (type.splashDamage > 0) {
                        sep(
                            bt,
                            Core.bundle.format(
                                "bullet.splashdamage",
                                type.splashDamage.toInt(),
                                Strings.fixed(type.splashDamageRadius / Vars.tilesize, 1)
                            )
                        )
                    }
                    if (type.statLiquidConsumed <= 0f && !compact && !Mathf.equal(
                            type.ammoMultiplier,
                            1f
                        ) && type.displayAmmoMultiplier && (t !is Turret || t.displayAmmoMultiplier)
                    ) {
                        sep(bt, Core.bundle.format("bullet.multiplier", type.ammoMultiplier.toInt()))
                    }
                    if (!compact && !Mathf.equal(type.reloadMultiplier, 1f)) {
                        val `val` = (type.reloadMultiplier * 100 - 100).toInt()
                        sep(
                            bt,
                            Core.bundle.format("bullet.reload", ammoStat(`val`.toFloat()))
                        )
                    }
                    if (type.knockback > 0) {
                        sep(
                            bt,
                            Core.bundle.format("bullet.knockback", Strings.autoFixed(type.knockback, 2))
                        )
                    }
                    if (type.healPercent > 0f) {
                        sep(
                            bt,
                            Core.bundle.format(
                                "bullet.healpercent",
                                Strings.autoFixed(type.healPercent, 2)
                            )
                        )
                    }
                    if (type.healAmount > 0f) {
                        sep(
                            bt,
                            Core.bundle.format("bullet.healamount", Strings.autoFixed(type.healAmount, 2))
                        )
                    }
                    if (type.pierce || type.pierceCap != -1) {
                        sep(
                            bt,
                            if (type.pierceCap == -1) "@bullet.infinitepierce" else Core.bundle.format(
                                "bullet.pierce",
                                type.pierceCap
                            )
                        )
                    }
                    if (type.incendAmount > 0) {
                        sep(bt, "@bullet.incendiary")
                    }
                    if (type.homingPower > 0.01f) {
                        sep(bt, "@bullet.homing")
                    }
                    if (type.lightning > 0) {
                        sep(
                            bt,
                            Core.bundle.format(
                                "bullet.lightning",
                                type.lightning,
                                if (type.lightningDamage < 0) type.damage else type.lightningDamage
                            )
                        )
                    }
                    if (type.pierceArmor) {
                        sep(bt, "@bullet.armorpierce")
                    }
                    if (type.maxDamageFraction > 0) {
                        sep(
                            bt,
                            Core.bundle.format("bullet.maxdamagefraction", (type.maxDamageFraction * 100).toInt())
                        )
                    }
                    if (type.suppressionRange > 0) {
                        sep(
                            bt,
                            Core.bundle.format(
                                "bullet.suppression",
                                Strings.autoFixed(type.suppressionDuration / 60f, 2),
                                Strings.fixed(type.suppressionRange / Vars.tilesize, 1)
                            )
                        )
                    }
                    if (type.status !== StatusEffects.none) {
                        sep(
                            bt,
                            (if (type.status.hasEmoji()) type.status.emoji() else "") + "[stat]" + type.status.localizedName + if (type.status.reactive) "" else "[lightgray] ~ [stat]" +
                                    (type.statusDuration / 60f).toInt() + "[lightgray] " + Core.bundle["unit.seconds"]
                        ).with { c ->
                            withTooltip(
                                c,
                                type.status
                            )
                        }
                    }
                    if (!type.targetMissiles) {
                        sep(bt, "@bullet.notargetsmissiles")
                    }
                    if (!type.targetBlocks) {
                        sep(bt, "@bullet.notargetsbuildings")
                    }
                    if (type.intervalBullet != null) {
                        bt.row()
                        val ic = Table()
                        ammo(
                            ObjectMap.of(t, type.intervalBullet),
                            true,
                            false
                        ).display(ic)
                        val coll = Collapser(ic, true)
                        coll.setDuration(0.1f)
                        bt.table { it: Table ->
                            it.left().defaults().left()
                            it.add(
                                Core.bundle.format(
                                    "bullet.interval",
                                    Strings.autoFixed(type.intervalBullets / type.bulletInterval * 60, 2)
                                )
                            )
                            it.button(
                                Icon.downOpen,
                                Styles.emptyi
                            ) { coll.toggle(false) }.update { i: ImageButton ->
                                i.style.imageUp =
                                    if (!coll.isCollapsed) Icon.upOpen else Icon.downOpen
                            }.size(8f).padLeft(16f).expandX()
                        }
                        bt.row()
                        bt.add(coll)
                    }
                    if (type.fragBullet != null && type !is IgnoredFragBullet) {
                        bt.row()
                        val fc = Table()
                        ammo(
                            ObjectMap.of(t, type.fragBullet),
                            nested = true,
                            showUnit = false
                        ).display(fc)
                        val coll = Collapser(fc, true)
                        coll.setDuration(0.1f)
                        bt.table { ft: Table ->
                            ft.left().defaults().left()
                            ft.add(Core.bundle.format("bullet.frags", type.fragBullets))
                            ft.button(
                                Icon.downOpen,
                                Styles.emptyi
                            ) { coll.toggle(false) }.update { i: ImageButton ->
                                i.style.imageUp =
                                    if (!coll.isCollapsed) Icon.upOpen else Icon.downOpen
                            }.size(8f).padLeft(16f).expandX()
                        }
                        bt.row()
                        bt.add(coll)
                    }
                    val info = (type as? AdditionInfoBulletType)?.info
                    if (info != null) {
                        bt.row()
                        info.display(bt)
                    }
                }.padLeft(5f).padTop(5f).padBottom((if (compact) 0 else 5).toFloat()).growX()
                    .margin((if (compact) 0 else 10).toFloat())
                table.row()
            }
        }
    }

    private fun sep(table: Table, text: String): Cell<*> {
        table.row()
        return table.add(text)
    }

    private fun ammoStat(`val`: Float): String =
        (if (`val` > 0) "[stat]+" else "[negstat]") + Strings.autoFixed(`val`, 1)

    private fun multStat(`val`: Float): String =
        (if (`val` >= 1) "[stat]" else "[negstat]") + Strings.autoFixed(`val`, 2)

    private fun icon(t: UnlockableContent): TextureRegion? = t.uiIcon
}
