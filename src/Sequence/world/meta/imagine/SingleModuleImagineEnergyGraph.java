package Sequence.world.meta.imagine;

import Sequence.world.meta.IO;
import Sequence.world.util.BuildingGraph;
import Sequence.world.util.Graph;
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
}
