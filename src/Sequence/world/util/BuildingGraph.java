package Sequence.world.util;

import Sequence.world.meta.IO;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;

public class BuildingGraph extends Graph<BuildingGraph.BuildingEntry> {
    public static class BuildingEntry implements IO {
        public final Building value;

        public BuildingEntry(Building value) {
            this.value = value;
        }

        @Override
        public void read(Reads read) {

        }

        @Override
        public void write(Writes write) {

        }
    }
}
