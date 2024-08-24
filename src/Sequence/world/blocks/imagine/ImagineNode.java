package Sequence.world.blocks.imagine;

import Sequence.world.meta.imagine.BuildingIEc;
import Sequence.world.util.struct.Pair;
import Sequence.world.blocks.imagine.ImagineCenter.ImagineCenterBuild;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class ImagineNode extends ImagineBlock {
    public int linkRange = 3;

    public ImagineNode(String name) {
        super(name);
    }

    Pair<Building, Integer> findNode(int x, int y, int direction) {
        int maxLen = linkRange + size / 2;
        Building dest = null;
        int dy = Geometry.d4y(direction);
        int dx = Geometry.d4x(direction);
        int offset = size / 2;
        for (int j = 1 + offset; j <= linkRange + offset; ++j) {
            Building other = world.build(x + j * dx, y + j * dy);
            if (other == null) continue;
            if (other instanceof ImagineNodeBuild || other instanceof ImagineCenter.ImagineCenterBuild) {
                maxLen = j;
                dest = other;
                break;
            }
        }
        return new Pair<>(dest, maxLen);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        if (tile.x == 60 && tile.y == 60) return true;
        for (int i = 0; i < 4; ++i) {
            if (findNode(tile.x, tile.y, i).getKey() != null) return true;
        }
        return false;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        for (int i = 0; i < 4; ++i) {
            var res = findNode(x, y, i);
            int dx = Geometry.d4x(i), dy = Geometry.d4y(i), maxLen = res.getValue();
            Building dest = res.getKey();
            Drawf.dashLine(Pal.placing,
                    x * tilesize + dx * (tilesize * size / 2f + 2),
                    y * tilesize + dy * (tilesize * size / 2f + 2),
                    x * tilesize + dx * maxLen * tilesize,
                    y * tilesize + dy * maxLen * tilesize
            );
            if (dest != null) {
                Drawf.square(dest.x, dest.y, dest.block.size * tilesize / 2f + 2.5f, 0f);
            }
        }
    }

    public class ImagineNodeBuild extends Building implements BuildingIEc {
        public ImagineCenterBuild center = null;
        protected boolean found = false;

        @Override
        public float capacity() {
            return 0;
        }

        @Override
        public void onRemoved() {
            if (center != null) {
                center.linkedIE.remove(this);
                Seq<Building> all = new Seq<>();
                Queue<Building> queue = new Queue<>();
                queue.add(center);
                while (!queue.isEmpty()) {
                    var e = queue.first();
                    queue.removeLast();
                    for (int i = 0; i < 4; ++i) {
                        Building build = findNode(e.tileX(), e.tileY(), i).getKey();
                        if (build == this) continue;
                        if (build == null) continue;
                        if (all.contains(build)) continue;
                        all.add(build);
                        queue.add(build);
                    }
                }
                for (Building building : center.linkedIE) {
                    if (!all.contains(building)) building.kill();
                }
            }
        }

        @Override
        public void updateTile() {
            if (center == null) {
                if (found) kill();
                else {
                    found = true;
                    findCenter();
                }
            }
            super.updateTile();
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            float sin = Mathf.sin(Time.time / 15f);
            Drawf.arrow(x, y, center.x, center.y, size * tilesize + sin, 4f + sin);
            Drawf.square(center.x, center.y, center.block.size * tilesize / 2f + 2.5f, 0f);
        }

        private void findCenter() {
            ImagineCenterBuild ce = null;
            for (int i = 0; i < 4; ++i) {
                Building build = findNode(tileX(), tileY(), i).getKey();
                if (build instanceof ImagineCenterBuild c) {
                    if (ce != null && ce != c) kill();
                    ce = c;
                } else if (build instanceof ImagineNodeBuild n) {
                    if (ce != null && ce != n.center) kill();
                    ce = n.center;
                }
            }
            if (ce == null) kill();
            center = ce;
            if (center != null) {
                center.linkedIE.add(this);
            }
        }
    }
}
