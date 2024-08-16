package Sequence.world.util;

import Sequence.world.meta.IO;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;

public class BuildingGraph extends Graph<BuildingGraph.BuildingEntry> {
    public BuildingGraph() {
        super(BuildingEntry.class);
    }

    public static class BuildingEntry implements IO {
        public final Building value;

        public BuildingEntry(Building value) {
            this.value = value;
        }
    }
}
