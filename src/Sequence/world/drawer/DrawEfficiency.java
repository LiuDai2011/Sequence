package Sequence.world.drawer;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

public class DrawEfficiency extends DrawBlock {
    public String suffix = "-efficiency";
    public TextureRegion region;

    @Override
    public void draw(Building build) {
        Draw.alpha(build.efficiency * build.warmup());
        Draw.rect(region, build.x, build.y);
        Draw.color();
    }

    @Override
    public void load(Block block) {
        region = Core.atlas.find(block.name + suffix);
    }
}
