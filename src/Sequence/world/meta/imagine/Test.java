package Sequence.world.meta.imagine;

import Sequence.graphic.SqColor;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.scene.ui.layout.Scl;
import arc.struct.IntSeq;
import arc.struct.IntSet;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.ui.Fonts;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Wall;

import static mindustry.Vars.tilesize;

public class Test extends Wall implements BlockIEc {
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

    @Override
    public boolean hasImagineEnergy() {
        return true;
    }

    public class WB extends WallBuild implements BuildingIEc {
        public ImagineEnergyModule iem = new ImagineEnergyModule(this);
        public Building core = this;
        public Seq<Building> links = new Seq<>(new Building[]{this});

        @Override
        public void onRemoved() {
            super.onRemoved();
            ((BuildingIEc) getCore()).removeLink(this);
        }

        @Override
        public void draw() {
            super.draw();
            Color color = SqColor.imagineEnergy;
            Fonts.def.draw("capacity " + getIEM().capacity(), x, y + 8, color, 0.3f, false, Align.center);
            Fonts.def.draw("activity " + getIEM().activity(), x, y + 4, color, 0.3f, false, Align.center);
            Fonts.def.draw("active " + getIEM().isActive(), x, y + 0, color, 0.3f, false, Align.center);
            Fonts.def.draw("instability " + getIEM().instability(), x, y + -4, color, 0.3f, false, Align.center);
            Fonts.def.draw("amount " + getIEM().amount(), x, y + -8, color, 0.3f, false, Align.center);
        }

        @Override
        public boolean coreAcceptImagineEnergy(boolean active, float activity, float instability) {
            return true;
        }

        @Override
        public float capacity() {
            return 1000;
        }

        @Override
        public void coreHandleImagineEnergy(Building source, float amount, boolean active, float activity, float instability) {
            iem.active(iem.active || active);
            iem.add(amount, activity, instability);
        }

        @Override
        public boolean isCore() {
            return core == this;
        }

        @Override
        public Seq<Building> linkedIEMBuilding() {
            return links;
        }

        @Override
        public void clear() {
            links.clear();
            iem.clear();
        }

        @Override
        public Building getCore() {
            return core;
        }

        @Override
        public Building setCore(Building core) {
            ((BuildingIEc) core).linkedIEMBuilding().add(links);
            ((BuildingIEc) core).getIEM().merge(iem);
            return this.core = core;
        }

        @Override
        public void updateTile() {
            super.updateTile();
            for (int len = 1; len < 10; ++len) {
                for (int i = 0; i < 4; ++i) {
                    Tile tt = Vars.world.tile(tileX() + Geometry.d4x(i) * len, tileY() + Geometry.d4y(i) * len);
                    if (tt != null && tt.build != null && tt.build instanceof BuildingIEc iec) {
                        if (iec.acceptImagineEnergy(false, 0, 0)) {
                            iec.handleImagineEnergy(this, Time.delta, false, 0, 0);
                            addLink(tt.build);
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
            Seq<Building> all = ((BuildingIEc) getCore()).linkedIEMBuilding();
            for (Building building : all) {
                Drawf.dashSquare(Draw.getColor(), building.x, building.y, building.block.size * tilesize);
            }
            Draw.reset();
        }

        @Override
        public ImagineEnergyModule coreGetIEM() {
            return iem;
        }
    }
}
