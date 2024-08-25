package Sequence.world.blocks.imagine;

import Sequence.world.meta.imagine.BuildingIEc;
import Sequence.world.meta.imagine.ImagineEnergyModule;
import arc.struct.Seq;
import mindustry.gen.Building;

public class ImagineCenter extends ImagineBlock {
    public ImagineCenter(String name) {
        super(name);
        imagineCapacity = 100;
        scaledHealth = 170;
        buildType = ImagineCenterBuild::new;
    }

    public class ImagineCenterBuild extends Building implements BuildingIEc {
        public ImagineEnergyModule iem = new ImagineEnergyModule(this);
        public final Seq<Building> linkedIE = new Seq<>();
        public boolean valid = true;

        @Override
        public float capacity() {
            return imagineCapacity;
        }

        @Override
        public void updateTile() {
            float cap = 0;
            for (Building building : linkedIE) {
                if (building instanceof BuildingIEc iec) {
                    cap += iec.capacity();
                }
            }
            iem.capacity(cap);
        }

        @Override
        public void onRemoved() {
            super.onRemoved();
            valid = false;
        }
    }
}
