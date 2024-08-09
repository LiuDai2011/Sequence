package Sequence.world.meta.imagine;

import mindustry.gen.Building;

public interface BuildingIEc extends IEc {
    boolean acceptImagineEnergy(boolean active, float activity, float instability);
    void handleImagineEnergy(Building source, float amount, boolean active, float activity, float instability);
}
