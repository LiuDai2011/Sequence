package Sequence.content;

import Sequence.SeqMod;
import Sequence.graphic.SqColor;
import Sequence.world.blocks.production.MultiCrafter;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.BuildingIEc;
import Sequence.world.meta.imagine.Test;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Wall;

public class SqBlocks {
    public static Block test, test1, test2;

    public static void load() {
        if (!SeqMod.dev) return;
//        test = new Wall("test-wall") {{
//            requirements(Category.effect, ItemStack.empty);
//            health = 100;
//            configurable = true;
//
//            buildType = () -> new WallBuild() {
//                @Override
//                public void buildConfiguration(Table table) {
//                    table.table(Styles.black6, ta -> {
//                        ta.add(SqUI.formula(new Formula(
//                                ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
//                                LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
//                                3,
//                                ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
//                                LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
//                                5,
//                                8)
//                        )).left().row();
//                        ta.add(SqUI.formula(new Formula(
//                                ItemStack.with(Items.thorium, 2),
//                                LiquidStack.with(Liquids.water, 5),
//                                114,
//                                ItemStack.with(SqItems.grainBoundaryAlloy, 1145),
//                                LiquidStack.with(Liquids.cryofluid, 7),
//                                6,
//                                9)
//                        )).left().row();
//                    }).margin(5).fillX().grow();
//                }
//            };
//        }};
        test = new MultiCrafter("test-multi-crafter") {{
            addFormula(
                    new Formula(
                            ItemStack.with(Items.thorium, 2),
                            LiquidStack.with(Liquids.water, 5),
                            114, ItemStack.with(SqItems.grainBoundaryAlloy, 11),
                            LiquidStack.with(Liquids.cryofluid, 7),
                            6,
                            90),
                    new Formula(
                            ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                            3,
                            ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                            LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                            5,
                            8),
                    new Formula(
                            ItemStack.empty,
                            LiquidStack.empty,
                            3,
                            ItemStack.empty,
                            LiquidStack.empty,
                            5,
                            8
                    )
            );

            requirements(Category.crafting, ItemStack.empty);

            craftEffect = Fx.pulverizeMedium;
            size = 2;
            hasItems = true;
            hasLiquids = true;
            hasPower = true;

            itemCapacity = 100;
            liquidCapacity = 100;

            ord = 11;
        }};
        test1 = new MultiCrafter("test-one-formula-crafter") {{
            addFormula(
                    new Formula(
                            ItemStack.with(Items.thorium, 2),
                            LiquidStack.with(Liquids.water, 5),
                            114,
                            ItemStack.with(SqItems.grainBoundaryAlloy, 11),
                            LiquidStack.with(Liquids.cryofluid, 7),
                            6,
                            90),
                    new Formula(
                            ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                            3,
                            ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                            LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                            5,
                            8),
                    new Formula(
                            ItemStack.empty,
                            LiquidStack.empty,
                            3,
                            ItemStack.empty,
                            LiquidStack.empty,
                            5,
                            8)
            );
            onlyOneFormula = true;

            requirements(Category.crafting, ItemStack.empty);

            craftEffect = Fx.pulverizeMedium;
            size = 2;
            hasItems = true;
            hasLiquids = true;
            hasPower = true;

            itemCapacity = 100;
            liquidCapacity = 100;

            buildType = () -> new MultiCrafterBuild() {
                @Override
                public void updateTile() {
                    IEG().getModule(this).update();
                    IEG().getModule(this).capacity(1e8f);
                }
            };

//            ord = 11;
        }};
        test2 = new Wall("test2") {{
            requirements(Category.effect, ItemStack.empty);
            health = 100;
            update = true;
            buildType = () -> new WallBuild() {
                @Override
                public void updateTile() {
                    for (int len = 1; len < 15; len++) {
                        for (int i = 0; i < 4; i++) {
                            Tile tile1 = Vars.world.tile(Geometry.d4x(i) * len + tileX(), Geometry.d4y(i) * len + tileY());
                            if (tile1 != null && tile1.build != null && tile1.build instanceof BuildingIEc icc && icc.acceptImagineEnergy(false, 0, 0)) {
                                icc.IEG().getModule(tile1.build).add(Time.delta);
                                if (Mathf.chanceDelta(tile1.block().size * 0.1f / len))
                                    Fx.healBlockFull.at(tile1.build.x, tile1.build.y, 0, SqColor.LiuDai.cpy().a(0.3f), tile1.build.block);
                            }
                        }
                    }
                    super.updateTile();
                }
            };
        }};
        new Test("test3") {{
            requirements(Category.effect, ItemStack.empty);
            health = 100;
            buildType = () -> new WB() {
                @Override
                public void updateTile() {
                    IEG().getModule(this).active(false);
//                    tile.setBlock(Blocks.air);
                }
            };
        }};
        new Test("test4") {{
            requirements(Category.effect, ItemStack.empty);
            health = 100;
            buildType = () -> new WB() {
                @Override
                public void updateTile() {
                    IEG().getModule(this).active();
//                    tile.setBlock(Blocks.air);
                }
            };
        }};
    }
}
