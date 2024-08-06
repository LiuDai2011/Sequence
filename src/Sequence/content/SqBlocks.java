package Sequence.content;

import Sequence.SeqMod;
import Sequence.graphic.SqColor;
import Sequence.world.blocks.BlockTile;
import Sequence.world.blocks.production.MultiCrafter;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.ImagineEnergyModule;
import arc.graphics.Color;
import arc.util.Align;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Fonts;
import mindustry.world.Block;

public class SqBlocks {
    public static Block test, test1;

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
                public void draw() {
                    super.draw();
                    Color color = SqColor.imagineEnergy;
                    Fonts.def.draw("active " + IEM().record.active, x, y - 12, color, 0.3f, false, Align.center);
                    Fonts.def.draw("amount " + IEM().record.amount, x, y - 8, color, 0.3f, false, Align.center);
                    Fonts.def.draw("activity " + IEM().record.activity, x, y - 4, color, 0.3f, false, Align.center);
                    Fonts.def.draw("instability " + IEM().record.instability, x, y, color, 0.3f, false, Align.center);
                    Fonts.def.draw("multi " + IEM().record.multi(), x, y + 4, color, 0.3f, false, Align.center);
                    Fonts.def.draw("energy " + IEM().record.energy(), x, y + 8, color, 0.3f, false, Align.center);
                    Fonts.def.draw("boost " + IEM().record.boost(), x, y + 12, color, 0.3f, false, Align.center);

                    BlockTile blockTile = new BlockTile(Blocks.arc, 5 + tileX(), 3 + tileY());
                    if (!blockTile.valid()) blockTile.draw();
                }

                @Override
                public void updateTile() {
                    IEM().update();
                    IEM().record.active();
                    IEM().capacity = 1;
                    IEM().add(1);
                }
            };

//            ord = 11;
        }};
    }
}
