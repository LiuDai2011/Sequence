package Sequence.world.blocks.imagine;

import Sequence.world.meta.imagine.BuildingIEc;
import Sequence.world.meta.imagine.ImagineEnergyModule;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.ChainedBuilding;
import mindustry.world.meta.BlockGroup;

public class ImagineDuct extends ImagineBlock {
    public float imagineCapacity = 4f;
    public float speed = 1f;

    public ImagineDuct(String name) {
        super(name);
        group = BlockGroup.transportation;
        drawArrow = true;
        rotate = true;
        rotateDraw = true;
        underBullets = true;
        conveyorPlacement = true;
        noUpdateDisabled = true;
        canOverdrive = false;
        priority = TargetPriority.transport;
        buildType = ImagineDuctBuild::new;
    }

    public class ImagineDuctBuild extends Building implements BuildingIEc, ChainedBuilding {
        public final Seq<Building> links = new Seq<>(new Building[]{this});
        public final ImagineEnergyModule iem = new ImagineEnergyModule(this);

        @Override
        public void updateTile() {
            super.updateTile();
            if (next() != null) {
                if (((BuildingIEc) next()).acceptImagineEnergy(false, 0, 0)) {
                    float amount = Math.min(speed * Time.delta, iem.amount());
                    ((BuildingIEc) next()).handleImagineEnergy(this, amount, false, 0, 0);
                    iem.remove(amount);
                }
            }
        }

        @Override
        public float capacity() {
            return imagineCapacity;
        }

        @Override
        public boolean coreAcceptImagineEnergy(boolean active, float activity, float instability) {
            return !active && !iem.full();
        }

        @Override
        public void coreHandleImagineEnergy(Building source, float amount, boolean active, float activity, float instability) {
            iem.add(amount, activity, instability);
        }

        @Override
        public boolean isCore() {
            return true;
        }

        @Override
        public Building getCore() {
            return this;
        }

        @Override
        public Building setCore(Building core) {
            return this;
        }

        @Override
        public Seq<Building> linkedIEMBuilding() {
            return links;
        }

        @Override
        public void clear() {

        }

        @Override
        public ImagineEnergyModule coreGetIEM() {
            return iem;
        }

        @Override
        public boolean linkable() {
            return false;
        }

        @Nullable
        @Override
        public Building next() {
            Tile next = tile.nearby(rotation);
            if(next != null && next.build instanceof ImagineDuctBuild){
                return next.build;
            }
            return null;
        }
    }
}
