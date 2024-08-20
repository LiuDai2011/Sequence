package Sequence.world.meta.imagine;

import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.gen.Building;

public interface BuildingIEc extends IEc {
    float capacity();

    default boolean acceptImagineEnergy(boolean active, float activity, float instability) {
        if (isCore()) return coreAcceptImagineEnergy(active, activity, instability);
        return ((BuildingIEc) getCore()).coreAcceptImagineEnergy(active, activity, instability);
    }

    boolean coreAcceptImagineEnergy(boolean active, float activity, float instability);

    default void handleImagineEnergy(Building source, float amount, boolean active, float activity, float instability) {
        if (isCore()) coreHandleImagineEnergy(source, amount, active, activity, instability);
        ((BuildingIEc) getCore()).coreHandleImagineEnergy(source, amount, active, activity, instability);
    }

    void coreHandleImagineEnergy(Building source, float amount, boolean active, float activity, float instability);

    boolean isCore();

    Building getCore();

    Building setCore(Building core);

    @Nullable
    Seq<Building> linkedIEMBuilding();

    default void addLink(Building other) {
        if (other == this || !((BuildingIEc) other).linkable()) return;
        Building core = getCore();
        BuildingIEc iec = (BuildingIEc) other;
        ((BuildingIEc) core).getIEM().addCapacity(((BuildingIEc) other).capacity());
        iec.setCore(core);
        iec.clear();
    }

    default void removeLink(Building other) {
        if (other == this) return;
        if (((BuildingIEc) other).isCore() && !((BuildingIEc) other).linkedIEMBuilding().isEmpty()) {
            BuildingIEc iEc = (BuildingIEc) other;
            BuildingIEc first = (BuildingIEc) iEc.linkedIEMBuilding().first();
            first.getIEM().capacity(first.capacity());
            for (Building building : iEc.linkedIEMBuilding()) {
                BuildingIEc buildingIEc = (BuildingIEc) building;
                if (!buildingIEc.linkable()) continue;
                first.getIEM().addCapacity(buildingIEc.capacity());
                buildingIEc.setCore(iEc.linkedIEMBuilding().first());
            }
        }
        ((BuildingIEc) getCore()).getIEM().addCapacity(-((BuildingIEc) other).capacity());
    }

    void clear();

    @Override
    default ImagineEnergyModule getIEM() {
        return ((BuildingIEc) getCore()).coreGetIEM();
    }

    ImagineEnergyModule coreGetIEM();

    default boolean linkable() {
        return true;
    }
}
