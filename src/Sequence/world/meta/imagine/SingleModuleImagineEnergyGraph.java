package Sequence.world.meta.imagine;

import Sequence.world.util.BuildingGraph;
import Sequence.world.util.Graph;
import arc.struct.Seq;
import mindustry.gen.Building;

public class SingleModuleImagineEnergyGraph extends ImagineEnergyGraph {
    public BuildingGraph graph = new BuildingGraph();
    public ImagineEnergyModule module = new ImagineEnergyModule(null);

    @Override
    public ImagineEnergyModule getModule(Building build) {
        return module;
    }

    @Override
    public void update() {
        module.update();
    }

    public SingleModuleImagineEnergyGraph merge(SingleModuleImagineEnergyGraph other) {
        graph.merge(other.graph);
        other.graph = graph;
        module.merge(other.module);
        other.module = module;
        return this;
    }

    public Seq<SingleModuleImagineEnergyGraph> delete(Building build) {
        Seq<Graph<Building>> r = graph.delete(graph.get(build));
        Seq<ImagineEnergyModule> iems = module.split(r.size);
        Seq<SingleModuleImagineEnergyGraph> res = new Seq<>(r.size);
        for (int i = 0; i < r.size; ++i) {
            SingleModuleImagineEnergyGraph value = new SingleModuleImagineEnergyGraph();
            value.module = iems.get(i);
            value.graph = BuildingGraph.fromGraph(r.get(i));
            res.set(i, value);
        }
        return res;
    }
}
