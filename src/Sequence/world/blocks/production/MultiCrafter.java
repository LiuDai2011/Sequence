package Sequence.world.blocks.production;

import Sequence.core.SeqElem;
import Sequence.core.SqBundle;
import Sequence.core.SqStatValues;
import Sequence.ui.SqUI;
import Sequence.world.meta.Formula;
import Sequence.world.util.Util;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.Button;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.logic.LAccess;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.StatValue;

public class MultiCrafter extends Block implements SeqElem {
    public int ord = -1;
    public final Seq<Formula> formulas = new Seq<>();
    public int[] liquidOutputDirections = {-1};

    public boolean dumpExtraLiquid = true;
    public boolean ignoreLiquidFullness = false;

    public float craftTime = 80;
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
        consumePowerDynamic((MultiCrafterBuild build) ->
                build.now == -1 ? 0 : build.getFormula().inputPower);
        config(Integer.class, (building, integer) -> {
            ((MultiCrafterBuild) building).now = integer;
        });
        configClear(building -> ((MultiCrafterBuild) building).now = -1);
    }

    @Override
    public void setBars() {
        super.setBars();
        removeBar("items");
        removeBar("liquid");
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
            table.table(t -> {
                t.add(SqBundle.catGet("block", "multi-crafter", "seq")).growX().left().row();
            }).growX().left().row();
            for (Formula formula : formulas) {
                SqStatValues.formulaStat(formula).display(table);
            }
        };
    }

    public class MultiCrafterBuild extends Building {
        private final Seq<Button> all = new Seq<>();
        public int now = -1;

        public float progress;
        public float totalProgress;
        public float warmup;

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
            return getFormula().outputPower * efficiency;
        }

        public @Nullable Formula getFormula() {
            return now == -1 ? null : ((MultiCrafter) block).formulas.get(now);
        }

        @Override
        public void draw() {
            drawer.draw(this);
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
            if (now == -1) return;

            boolean has = true;
            for (LiquidStack stack : getFormula().inputLiquid) {
                if (liquids.get(stack.liquid) < stack.amount - 0.001f) has = false;
            }
            for (ItemStack stack : getFormula().inputItem) {
                if (items.get(stack.item) < stack.amount) has = false;
            }
            if (!has) efficiency(0);

            if (efficiency > 0) {
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
            }

            dumpOutputs();
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
                }
            }

            if (wasVisible) {
                craftEffect.at(x, y);
            }
            progress %= 1f;
        }

        public void dumpOutputs() {
            if (timer(timerDump, dumpTime / timeScale)) {
                for (ItemStack output : getFormula().outputItem) {
                    dump(output.item);
                }
            }

            for (int i = 0; i < getFormula().inputLiquid.length; i++) {
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
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            progress = read.f();
            warmup = read.f();
            now = read.i();
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
                            now = finalI;
                            for (Button button1 : all) {
                                button1.setChecked(false);
                            }
                            button.setChecked(true);
                        }
                        else now = -1;
                    });
                    all.add(button);
                    ta.add(button).left().height(56);
                    ta.add().growX();
                    ta.row();
                }
            }).margin(5).fillX().grow();
        }

        @Override
        public void displayBars(Table table) {
            super.displayBars(table);
            if (now == -1) return;
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
        }
    }
}
