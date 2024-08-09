package Sequence.world.meta.imagine;

import mindustry.gen.Building;
import mindustry.world.blocks.defense.Wall;

public class Test extends Wall {
    public Test(String name) {
        super(name);
        buildType = WB::new;
        update = true;
    }

    public class WB extends WallBuild implements BuildingIEc {
        private final ImagineEnergyGraph graph = new SingleModuleImagineEnergyGraph();

        @Override
        public boolean acceptImagineEnergy(boolean active, float activity, float instability) {
            return true;
        }

        @Override
        public void handleImagineEnergy(Building source, float amount, boolean active, float activity, float instability) {

        }

        @Override
        public ImagineEnergyGraph IEG() {
            return graph;
        }
    }
}
