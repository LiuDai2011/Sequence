package Sequence.world.meta.imagine;

import Sequence.graphic.SqColor;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.struct.IntSeq;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Wall;

import static mindustry.Vars.tilesize;

public class Test extends Wall {
    public Test(String name) {
        super(name);
        buildType = WB::new;
        update = true;
    }

    @Override
    public void setBars() {
        super.setBars();
        ImagineBlocks.bars(this);
    }

    public class WB extends WallBuild implements BuildingIEc {
        private final ImagineEnergyGraph graph = new SingleModuleImagineEnergyGraph();

        @Override
        public void placed() {
            super.placed();
            Core.app.post(() -> {
                ((SingleModuleImagineEnergyGraph) graph).graph.addNode(this);
            });
        }

        @Override
        public void onRemoved() {
            super.onRemoved();
            ((SingleModuleImagineEnergyGraph) graph).delete(this);
        }

        @Override
        public boolean acceptImagineEnergy(boolean active, float activity, float instability) {
            return true;
        }

        @Override
        public void handleImagineEnergy(Building source, float amount, boolean active, float activity, float instability) {
            graph.getModule(this).add(amount, activity, instability);
        }

        @Override
        public ImagineEnergyGraph IEG() {
            return graph;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            for (Building building : proximity) {
                if (building instanceof BuildingIEc iec) {
                    if (iec.IEG() instanceof SingleModuleImagineEnergyGraph og)
                        ((SingleModuleImagineEnergyGraph) IEG()).merge(og);
                }
            }
            for (int len = 1; len < 10; ++len) {
                for (int i = 0; i < 4; ++i) {
                    Tile tt = Vars.world.tile(tileX() + Geometry.d4x(i) * len, tileY() + Geometry.d4y(i) * len);
                    if (tt != null && tt.build != null && tt.build instanceof BuildingIEc iec) {
                        if (iec.acceptImagineEnergy(false, 0, 0)) {
                            iec.handleImagineEnergy(this, 10000, false, 0, 0);
                            if (Mathf.chance(0.2))
                                Fx.healBlockFull.at(tt.build.x, tt.build.y, 0, SqColor.LiuDai.cpy().a(0.3f), tt.build.block);
                        }
                    }
                }
            }
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            Draw.color(0x00ff0000);
            IntSeq all = ((SingleModuleImagineEnergyGraph) graph).graph.all();
            for (int i = 0; i < all.size; ++i) {
                Building building = ((SingleModuleImagineEnergyGraph) graph).graph.get(all.get(i));
                Drawf.dashSquare(Draw.getColor(), building.x, building.y, building.block.size * tilesize);
            }
            Draw.reset();
        }
    }
}
