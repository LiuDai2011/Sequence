package Sequence.content;

import Sequence.SeqMod;
import Sequence.graphic.SqColor;
import Sequence.world.blocks.BlockTile;
import Sequence.world.blocks.production.MultiCrafter;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.ImagineEnergyModule;
import arc.graphics.Color;
import arc.math.geom.Geometry;
import arc.util.Align;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Fonts;
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
                            8,
                            new ImagineEnergyModule.ImagineEnergyRecord(1, 3, true),
                            new ImagineEnergyModule.ImagineEnergyRecord(5, 0, false)
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
                            8,
                            new ImagineEnergyModule.ImagineEnergyRecord(1, 3, true),
                            new ImagineEnergyModule.ImagineEnergyRecord(5, 0, false)
                    )
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
                    IEM().update();
                    IEM().record.active();
                    IEM().capacity = 1000;
                    IEM().add(Time.delta);
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
                    for (int i = 0; i < 4; i++) {
                        Tile tile1 = Vars.world.tile(Geometry.d4x(i) + tileX(), Geometry.d4y(i) + tileY());
                        if (tile1 != null && tile1.build != null && tile1.build instanceof ImagineEnergyModule.IEMc ieMc) {
                            ieMc.IEM().add(Time.delta);
                        }
                    }
                    super.updateTile();
                }
            };
        }};
    }
}
