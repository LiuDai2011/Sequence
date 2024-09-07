package sequence.world.drawer;

import arc.Core;
import mindustry.world.Block;
import mindustry.world.draw.DrawLiquidRegion;

public class NoCheckDrawLiquidRegion extends DrawLiquidRegion {
    @Override
    public void load(Block block) {
        liquid = Core.atlas.find(block.name + suffix);
    }
}
