package Sequence.content;

import Sequence.SeqMod;
import Sequence.ui.SqUI;
import Sequence.world.meta.Formula;
import arc.scene.ui.layout.Table;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;

public class SqBlocks {
    public static Block test;

    public static void load() {
        if (!SeqMod.dev) return;
        test = new Wall("test-wall") {{
            requirements(Category.effect, ItemStack.empty);
            health = 100;
            configurable = true;

            buildType = () -> new WallBuild() {
                @Override
                public void buildConfiguration(Table table) {
                    table.table(Styles.black6, ta -> {
                        ta.add(SqUI.formula(new Formula(
                                ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                                LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                                3,
                                ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                                LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                                5,
                                8)
                        )).left().row();
                        ta.add(SqUI.formula(new Formula(
                                ItemStack.with(Items.thorium, 2),
                                LiquidStack.with(Liquids.water, 5),
                                114,
                                ItemStack.with(SqItems.grainBoundaryAlloy, 1145),
                                LiquidStack.with(Liquids.cryofluid, 7),
                                6,
                                9)
                        )).left().row();
                    }).margin(5).fillX().grow();
                }
            };
        }};
    }
}
