package sequence.world.blocks.imagine;

import sequence.world.meta.imagine.BlockIEc;
import sequence.world.meta.imagine.ImagineBlocks;
import mindustry.world.Block;

public class ImagineBlock extends Block implements BlockIEc {
    public boolean hasImagine = true;
    public float imagineCapacity = 10f;

    public ImagineBlock(String name) {
        super(name);
        update = true;
        solid = true;
    }

    @Override
    public boolean hasImagineEnergy() {
        return hasImagine;
    }

    @Override
    public void setBars() {
        super.setBars();
        ImagineBlocks.bars(this);
    }
}
