package Sequence.world.blocks.production;

import Sequence.core.SeqElem;
import Sequence.core.SqBundle;
import Sequence.core.SqStatValues;
import Sequence.graphic.SqColor;
import Sequence.ui.SqUI;
import Sequence.world.blocks.BlockTile;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.*;
import Sequence.world.util.Util;
import arc.Core;
import arc.func.Cons;
import arc.func.Func2;
import arc.func.Func3;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.*;

import static mindustry.Vars.content;

public class MultiCrafter extends Block implements SeqElem {
    public final Seq<Formula> formulas = new Seq<>();
    public int ord = -1;
    public int[] liquidOutputDirections = {-1};

    public boolean onlyOneFormula = false;

    public boolean dumpExtraLiquid = true;
    public boolean ignoreLiquidFullness = false;

    public Effect craftEffect = Fx.none;
    public Effect updateEffect = Fx.none;
    public float updateEffectChance = 0.04f;
    public float warmupSpeed = 0.019f;

    public DrawBlock drawer = new DrawDefault();
    private Boolean computeOutputsItems = null;

    public MultiCrafter(String name) {
        super(name);
        buildType = MultiCrafterBuild::new;
        configurable = true;
        copyConfig = true;
        update = true;
        solid = true;
        ambientSound = Sounds.machine;
        sync = true;
        ambientSoundVolume = 0.03f;
        flags = EnumSet.of(BlockFlag.factory);
        drawArrow = false;
    }

    @Override
    public void setStats() {
        super.setStats();
        ImagineBlocks.stats(this);

        if (!onlyOneFormula) return;

        stats.timePeriod = formulas.get(0).time;
        if((hasItems && itemCapacity > 0) || formulas.get(0).outputItem.length > 0){
            stats.add(Stat.productionTime, formulas.get(0).time / 60f, StatUnit.seconds);
        }

        if(formulas.get(0).outputItem.length > 0){
            stats.add(Stat.output, StatValues.items(formulas.get(0).time, formulas.get(0).outputItem));
        }

        if(formulas.get(0).outputLiquid.length > 0){
            stats.add(Stat.output, StatValues.liquids(1f, formulas.get(0).outputLiquid));
        }

        if(formulas.get(0).outputPower > 0){
            stats.add(Stat.basePowerGeneration, formulas.get(0).outputPower * 60f, StatUnit.powerSecond);
        }
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("items");
        removeBar("liquid");
        ImagineBlocks.bars(this);
        // in building.displayBars()
    }

    @Override
    public boolean rotatedOutput(int x, int y) {
        return false;
    }

    @Override
    public void load() {
        super.load();

        drawer.load(this);
    }

    @Override
    public void init() {
        for (Formula formula : formulas) {
            if (formula.inputLiquid.length > 0 || formula.outputLiquid.length > 0) hasLiquids = true;
            if (formula.outputLiquid.length > 0) outputsLiquid = true;
            if (formula.outputItem.length > 0 || formula.inputItem.length > 0) hasItems = true;
            if (formula.inputPower > 0 || formula.outputPower > 0) hasPower = true;
            if (formula.outputPower > 0) outputsPower = true;
        }
        if (onlyOneFormula) {
            configurable = false;
            consumeItems(formulas.get(0).inputItem);
            consumeLiquids(formulas.get(0).inputLiquid);
            consumePower(formulas.get(0).inputPower);
        } else {
            consumePowerDynamic((MultiCrafterBuild build) ->
                    build.now == -1 ? 0 : build.getFormula().inputPower);
            config(Integer.class, (building, integer) -> {
                ((MultiCrafterBuild) building).now = integer;
            });
            configClear(building -> ((MultiCrafterBuild) building).now = -1);
        }

        super.init();
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public boolean outputsItems() {
        if (computeOutputsItems == null) {
            computeOutputsItems = false;
            for (Formula formula : formulas) {
                if (formula.outputItem.length > 0) {
                    computeOutputsItems = true;
                    break;
                }
            }
        }
        return computeOutputsItems;
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out) {
        drawer.getRegionsToOutline(this, out);
    }

//    @Override
//    public void drawOverlay(float x, float y, int rotation) {
//        if (outputLiquids != null) {
//            for (int i = 0; i < outputLiquids.length; i++) {
//                int dir = liquidOutputDirections.length > i ? liquidOutputDirections[i] : -1;
//
//                if (dir != -1) {
//                    Draw.rect(
//                            outputLiquids[i].liquid.fullIcon,
//                            x + Geometry.d4x(dir + rotation) * (size * tilesize / 2f + 4),
//                            y + Geometry.d4y(dir + rotation) * (size * tilesize / 2f + 4),
//                            8f, 8f
//                    );
//                }
//            }
//        }
//    }

    public void addFormula(Formula... forms) {
        formulas.add(forms);
    }

    @Override
    public int order() {
        return ord;
    }

    @Override
    public StatValue statValue() {
        return table -> {
            table.row();
            table.table(t -> t.add(SqBundle.catGet("block", "multi-crafter", "seq")).growX().left().row()).growX().left().row();
            for (Formula formula : formulas) {
                SqStatValues.formulaStat(formula).display(table);
            }
        };
    }

    public class MultiCrafterBuild extends Building implements BuildingIEc {
        private final Seq<Button> all = new Seq<>();
        public int now = -1;
        public float progress;
        public float totalProgress;
        public float warmup;
        private boolean upd = false;
        private float lastOutputPower = 0f;

        @Override
        public boolean acceptItem(Building source, Item item) {
            return now != -1 && Util.item(getFormula().inputItem, item) > 0 && items.get(item) < getMaximumAccepted(item);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return now != -1 && Util.liquid(getFormula().inputLiquid, liquid) > 0;
        }

        @Override
        public float getPowerProduction() {
            return now == -1 ? 0 : lastOutputPower;
        }

        public @Nullable Formula getFormula() {
            return now == -1 ? null : ((MultiCrafter) block).formulas.get(now);
        }

        @Override
        public void draw() {
            drawer.draw(this);
            Color color = SqColor.imagineEnergy;
            Func3<String, String, Float, Void> func = (n, a, y) -> {
                Fonts.def.draw(n + " " + a, x, this.y + y, color, 0.3f, false, Align.center);
                return null;
            };
            func.get("active", String.valueOf(IEG().getModule(this).isActive()), 8f);
            func.get("amount", String.valueOf(IEG().getModule(this).amount()), 4f);
            func.get("activity", String.valueOf(IEG().getModule(this).activity()), 0f);
            func.get("instability", String.valueOf(IEG().getModule(this).instability()), -4f);
            func.get("capacity", String.valueOf(IEG().getModule(this).capacity()), -8f);

            BlockTile blockTile = new BlockTile(Blocks.arc, 5 + tileX(), 3 + tileY(), Time.time / 5f);
            if (!blockTile.valid()) blockTile.draw();
        }

        @Override
        public void drawLight() {
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public boolean shouldConsume() {
            if (now == -1) return false;
            for (var output : getFormula().outputItem) {
                if (items.get(output.item) + output.amount > itemCapacity) {
                    return false;
                }
            }
            if (!ignoreLiquidFullness) {
                boolean allFull = true;
                for (var output : getFormula().outputLiquid) {
                    if (liquids.get(output.liquid) >= liquidCapacity - 0.001f) {
                        if (!dumpExtraLiquid) {
                            return false;
                        }
                    } else {
                        allFull = false;
                    }
                }

                if (allFull) {
                    return false;
                }
            }

            return enabled;
        }

        @Override
        public void updateTile() {
            IEG().getModule(this).update();
            dumpOutputs();

            if (now == -1) {
                if (onlyOneFormula) now = 0;
                lastOutputPower = 0;
                return;
            }

            boolean has = true;
            float fullness = 0f;
            int count = 0;
            if (getFormula().inputPower > 0) {
                fullness += Mathf.clamp(power.status);
                count++;
            }
            for (LiquidStack stack : getFormula().inputLiquid) {
                if (liquids.get(stack.liquid) < stack.amount - 0.001f) has = false;
                fullness += Mathf.clamp(liquids.get(stack.liquid) / stack.amount);
                count++;
            }
            for (ItemStack stack : getFormula().inputItem) {
                if (items.get(stack.item) < stack.amount) has = false;
                fullness += Mathf.clamp((float) items.get(stack.item) / stack.amount);
                count++;
            }
            if (count == 0) efficiency(1);
            else efficiency(fullness / count);

            if (efficiency > 0 && has) {
                progress += getProgressIncrease(getFormula().time);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                if (getFormula().outputLiquid.length > 0) {
                    float inc = getProgressIncrease(1f);
                    for (var output : getFormula().outputLiquid) {
                        handleLiquid(this, output.liquid, Math.min(output.amount * inc, liquidCapacity - liquids.get(output.liquid)));
                    }
                }

                if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4));
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            if (progress >= 1f) {
                craft();
                progress %= 1f;
            }
        }

        @Override
        public float getProgressIncrease(float baseTime) {
            if (ignoreLiquidFullness || now == -1) {
                return super.getProgressIncrease(baseTime);
            }

            float scaling = 1f, max = 1f;
            if (getFormula().outputLiquid.length > 0) {
                max = 0f;
                for (var s : getFormula().outputLiquid) {
                    float value = (liquidCapacity - liquids.get(s.liquid)) / (s.amount * edelta());
                    scaling = Math.min(scaling, value);
                    max = Math.max(max, value);
                }
            }

            return super.getProgressIncrease(baseTime) * (dumpExtraLiquid ? Math.min(max, 1f) : scaling);
        }

        public float warmupTarget() {
            return 1f;
        }

        @Override
        public float warmup() {
            return warmup;
        }

        @Override
        public float totalProgress() {
            return totalProgress;
        }

        @Override
        public void consume() {
            super.consume();
            items.remove(getFormula().inputItem);
            for (LiquidStack stack : getFormula().inputLiquid) {
                liquids.remove(stack.liquid, stack.amount);
            }
        }

        public void craft() {
            consume();

            for (var output : getFormula().outputItem) {
                for (int i = 0; i < output.amount; i++) {
                    offload(output.item);
                    if (items.get(output.item) > itemCapacity) items.set(output.item, itemCapacity);
                }
            }
            dumpOutputs();
            lastOutputPower = getFormula().outputPower;

            if (wasVisible) {
                craftEffect.at(x, y);
            }
        }

        public void dumpOutputs() {
            if (timer(timerDump, dumpTime / timeScale)) {
                if (now == -1) {
                    dump();
                } else {
                    for (ItemStack output : getFormula().outputItem) {
                        dump(output.item);
                    }
                }
            }

            if (now == -1) {
                for (Liquid liquid : content.liquids()) {
                    dumpLiquid(liquid);
                }
                return;
            }
            for (int i = 0; i < getFormula().outputLiquid.length; i++) {
                int dir = liquidOutputDirections.length > i ? liquidOutputDirections[i] : -1;

                dumpLiquid(getFormula().outputLiquid[i].liquid, 2f, dir);
            }
        }

        @Override
        public double sense(LAccess sensor) {
            if (sensor == LAccess.progress) return progress();
            if (sensor == LAccess.totalLiquids &&
                    getFormula().outputLiquid.length > 0)
                return liquids.get(getFormula().outputLiquid[0].liquid);
            return super.sense(sensor);
        }

        @Override
        public float progress() {
            return Mathf.clamp(progress);
        }

        @Override
        public int getMaximumAccepted(Item item) {
            return itemCapacity;
        }

        @Override
        public boolean shouldAmbientSound() {
            return efficiency > 0;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(progress);
            write.f(warmup);
            write.i(now);
            write.f(lastOutputPower);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            progress = read.f();
            warmup = read.f();
            now = read.i();
            lastOutputPower = read.f();
        }

        @Override
        public void buildConfiguration(Table table) {
            table.table(Styles.black6, ta -> {
                MultiCrafter crafter = (MultiCrafter) block;
                all.clear();
                for (int i = 0; i < crafter.formulas.size; ++i) {
                    Button button = SqUI.formula(crafter.formulas.get(i));
                    button.setChecked(now == i);
                    int finalI = i;
                    button.clicked(() -> {
                        if (button.isChecked()) {
                            configure(finalI);
                            lastOutputPower = 0;
                            upd = true;
                            for (Button button1 : all) {
                                button1.setChecked(false);
                            }
                            button.setChecked(true);
                        } else now = -1;
                    });
                    all.add(button);
                    ta.add(button).left().height(56);
                    ta.add().growX();
                    ta.row();
                }
            }).margin(5).fillX().grow();
        }

        @Override
        public Object config() {
            return now;
        }

        @Override
        public void displayBars(Table table) {
            db(table);
            table.update(() -> {
                if (upd) {
                    upd = false;
                    table.clear();
                    db(table);
                }
            });
        }

        private void db(Table table) {
            super.displayBars(table);
            if (now == -1) return;
            if (getFormula().outputPower > 0) {
                table.add(new Bar(
                        () ->
                        Core.bundle.format("bar.poweroutput",
                                Strings.fixed(this.getPowerProduction() * 60 * this.timeScale(), 1)),
                        () -> Pal.powerBar,
                        () -> this.efficiency)).row();
            }
            for (ItemStack stack : getFormula().inputItem) {
                table.add(new Bar(
                        stack.item.localizedName,
                        stack.item.color,
                        () -> (float) items.get(stack.item) / itemCapacity
                )).row();
            }
            for (LiquidStack stack : getFormula().inputLiquid) {
                table.add(new Bar(
                        stack.liquid.localizedName,
                        stack.liquid.color,
                        () -> liquids.get(stack.liquid) / liquidCapacity
                )).row();
            }

            for (ItemStack stack : getFormula().outputItem) {
                if (Util.item(getFormula().inputItem, stack.item) > 0) return;
                table.add(new Bar(
                        stack.item.localizedName,
                        stack.item.color,
                        () -> (float) items.get(stack.item) / itemCapacity
                )).row();
            }
            for (LiquidStack stack : getFormula().outputLiquid) {
                if (Util.liquid(getFormula().inputLiquid, stack.liquid) > 0) return;
                table.add(new Bar(
                        stack.liquid.localizedName,
                        stack.liquid.color,
                        () -> liquids.get(stack.liquid) / liquidCapacity
                )).row();
            }
        }

        @Override
        public void placed() {
            super.placed();
//            Core.app.post(() -> {
//                ieg.graph().addNode(this);
//            });
        }

        public ImagineEnergyGraph ieg = new SingleModuleImagineEnergyGraph();

        @Override
        public ImagineEnergyGraph IEG() {
            return ieg;
        }

        @Override
        public boolean acceptImagineEnergy(boolean active, float activity, float instability) {
            return true;
        }

        @Override
        public void handleImagineEnergy(Building source, float amount, boolean active, float activity, float instability) {
        }
    }
}
