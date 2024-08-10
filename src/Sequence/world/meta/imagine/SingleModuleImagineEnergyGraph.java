package Sequence.world.meta.imagine;

import Sequence.world.util.BuildingGraph;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;

public class SingleModuleImagineEnergyGraph extends ImagineEnergyGraph {
    private static final ImagineEnergyModule module = new ImagineEnergyModule(null);
    private final BuildingGraph graph = new BuildingGraph();

    @Override
    public ImagineEnergyModule getModule(Building build) {
        return module;
    }

    @Override
    public void update() {
        module.update();
    }

    @Override
    public void read(Reads read) {
        module.read(read);
        graph.read(read);
    }

    @Override
    public void write(Writes write) {
        module.write(write);
        graph.write(write);
    }
}
