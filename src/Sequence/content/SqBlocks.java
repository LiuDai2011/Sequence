package Sequence.content;

import Sequence.SeqMod;
import Sequence.graphic.SqColor;
import Sequence.world.blocks.production.MultiCrafter;
import Sequence.world.drawer.DrawBottom;
import Sequence.world.drawer.DrawEfficiency;
import Sequence.world.drawer.NoCheckDrawLiquidRegion;
import Sequence.world.meta.Formula;
import Sequence.world.meta.imagine.BuildingIEc;
import Sequence.world.meta.imagine.ImagineEnergyRecord;
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
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;

public class SqBlocks {
    public static Block multiFluidMixer, berylliumCrystallizer, test, test1, test2;

    public static void load() {
        multiFluidMixer = new MultiCrafter("multi-fluid-mixer") {{
            addFormula(
                    new Formula(
                            ItemStack.with(SqItems.crystallizedBeryllium, 1),
                            LiquidStack.with(Liquids.water, 6),
                            2,
                            ItemStack.empty,
                            LiquidStack.with(SqLiquids.crystallizedFluid, 5),
                            0,
                            0.5f * 60f,
                            false),
                    new Formula(
                            ItemStack.with(SqItems.grainBoundaryAlloy, 1),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 6),
                            3,
                            ItemStack.empty,
                            LiquidStack.with(SqLiquids.vectorizedFluid, 4.5),
                            0,
                            1.2f * 60f,
                            false)
            );

            requirements(Category.crafting, ItemStack.with(
                    Items.graphite, 60,
                    SqItems.pureSilicon, 30,
                    SqItems.crystallizedBeryllium, 120,
                    Items.tungsten, 80));

            drawer = new DrawMulti(
                    new DrawBottom(),
                    new NoCheckDrawLiquidRegion(),
                    new DrawDefault()
            );

            scaledHealth = 65;

            craftEffect = Fx.pulverizeMedium;
            size = 2;

            itemCapacity = 20;
            liquidCapacity = 60;

            ord = 34;
        }};

        berylliumCrystallizer = new MultiCrafter("beryllium-crystallizer") {{
            addFormula(new Formula(ItemStack.with(Items.beryllium, 4),
                    LiquidStack.empty,
                    1,
                    ItemStack.with(SqItems.crystallizedBeryllium, 3),
                    LiquidStack.empty,
                    0,
                    0.83f * 60f));
            onlyOneFormula = true;

            requirements(Category.crafting, ItemStack.with(Items.graphite, 80,
                    Items.tungsten, 20,
                    Items.beryllium, 60));

            drawer = new DrawMulti(
                    new DrawBottom(),
                    new DrawEfficiency(),
                    new DrawDefault()
            );

            scaledHealth = 73;
            size = 3;

            itemCapacity = 30;
        }};

        if (!SeqMod.dev) return;
        test = new MultiCrafter("test-multi-crafter") {{
            addFormula(
                    new Formula(
                            ItemStack.with(Items.thorium, 2),
                            LiquidStack.with(Liquids.water, 5),
                            114, ItemStack.with(SqItems.grainBoundaryAlloy, 11),
                            LiquidStack.with(Liquids.cryofluid, 7),
                            6,
                            90, false),
                    new Formula(
                            ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                            3,
                            ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                            LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                            5,
                            8, false),
                    new Formula(
                            ItemStack.empty,
                            LiquidStack.empty,
                            3,
                            new ImagineEnergyRecord(5, 10, 0, true),
                            ItemStack.empty,
                            LiquidStack.empty,
                            5,
                            new ImagineEnergyRecord(3, 1, 2, false),
                            8, false)
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
                            90, false),
                    new Formula(
                            ItemStack.with(SqItems.berylliumalAlloy, 2, Items.surgeAlloy, 3),
                            LiquidStack.with(SqLiquids.crystallizedFluid, 3, Liquids.water, 5),
                            3,
                            ItemStack.with(SqItems.phaseCore, 5, SqItems.grainBoundaryAlloy, 10),
                            LiquidStack.with(SqLiquids.vectorizedFluid, 2, Liquids.cryofluid, 7),
                            5,
                            8, false),
                    new Formula(
                            ItemStack.empty,
                            LiquidStack.empty,
                            3,
                            new ImagineEnergyRecord(5, 10, 0, true),
                            ItemStack.empty,
                            LiquidStack.empty,
                            5,
                            new ImagineEnergyRecord(3, 1, 2, false),
                            8, false)
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
