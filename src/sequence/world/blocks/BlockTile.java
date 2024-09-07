package sequence.world.blocks;

import sequence.util.Util;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.GlyphLayout;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.util.pooling.Pools;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.Block;
import mindustry.world.Tile;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class BlockTile {
    private static final Vec2 mouse = new Vec2(), min = new Vec2(), center = new Vec2();
    public static Color tipColor = Pal.accent;
    public static Font tipFont = Fonts.outline;

    public Block block;
    public int x;
    public int y;
    public float alpha;
    public float rotation;

    public BlockTile(Block block, int x, int y, float alpha, float rotation) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.alpha = alpha;
        this.rotation = rotation;
    }

    public BlockTile(Block block, int x, int y, float rotation) {
        this(block, x, y, 0.4f, rotation);
    }

    public BlockTile(Block block, int x, int y) {
        this(block, x, y, 0);
    }

    public boolean valid() {
        for (int x = 0; x < block.size; ++x) {
            for (int y = 0; y < block.size; ++y) {
                Tile tile = world.tile(this.x + x, this.y + y);
                if (tile == null || tile.block() != block) {
                    return false;
                }
            }
        }

        return true;
    }

    public void draw() {
        mouse.set(Core.input.mouseWorld()).scl(1f / tilesize);
        min.set(x - 0.5f, y - 0.5f);
        center.set(min).add(block.size / 2f, block.size / 2f).scl(tilesize);

        Draw.color(Color.white, alpha);
        Draw.rect(block.fullIcon, center.x, center.y, rotation);
        Draw.flush();

        if (Util.inZone(min, new Vec2(block.size, block.size), mouse)) {
            drawTip(center.x, center.y);
        }
    }

    public void drawTip(float offsetX, float offsetY) {
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        boolean ints = tipFont.usesIntegerPositions();

        tipFont.setUseIntegerPositions(false);
        tipFont.getData().setScale(0.25f / Scl.scl(1));
        layout.setText(tipFont, block.localizedName);
        float width = layout.width;
        tipFont.setColor(tipColor);
        float dy = offsetY + block.size * tilesize / 2f + 3;
        tipFont.draw(block.localizedName, offsetX, dy + layout.height + 1, 1);
        --dy;
        Lines.stroke(2, Color.darkGray);
        Lines.line(offsetX - width / 2 - 2, dy, offsetX + width / 2 + 1.5f, dy);
        Lines.stroke(1, tipColor);
        Lines.line(offsetX - width / 2 - 2, dy, offsetX + width / 2 + 1.5f, dy);

        tipFont.setUseIntegerPositions(ints);
        tipFont.setColor(Color.white);
        tipFont.getData().setScale(1);
        Draw.reset();
        Pools.free(layout);
        Draw.flush();
    }
}
