package Sequence.world.util;

import arc.struct.IntMap;
import arc.struct.IntSet;
import arc.struct.ObjectIntMap;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;

public class BuildingGraph extends Graph<Building> {
    public BuildingGraph() {
        super(Building.class);
    }

    @Override
    public void read(Reads read) {
        ;
    }

    @Override
    public void write(Writes write) {
        ;
    }

    public static BuildingGraph fromGraph(Graph<Building> graph) {
        BuildingGraph res = new BuildingGraph();
        for (ObjectIntMap.Entry<Building> entry : graph.ids) {
            res.ids.put(entry.key, entry.value);
        }
        for (IntMap.Entry<Building> entry : graph.nodes) {
            res.nodes.put(entry.key, entry.value);
        }
        for (IntMap.Entry<IntSet> entry : graph.graph) {
            IntSet.IntSetIterator iter = entry.value.iterator();
            while (iter.hasNext) {
                int v = iter.next();
                res.addEdge(res.nodes.get(entry.key), res.nodes.get(v), false);
            }
        }
        return res;
    }
}
