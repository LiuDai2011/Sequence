package Sequence.world.blocks;

import Sequence.core.SqLog;
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

import static mindustry.Vars.world;

public class BlockTile {
    public Block block;
    public int x;
    public int y;
    public float alpha;

    public BlockTile(Block block, int x, int y, float alpha) {
        this.block = block;
        this.x = x;
        this.y = y;
        this.alpha = alpha;
    }

    public BlockTile(Block block, int x, int y) {
        this(block, x, y, 0.4f);
    }

    public boolean valid(int tileX, int tileY) {
        int offsetX = x + tileX;
        int offsetY = y + tileY;

        for(int x = 0; x < block.size; ++x) {
            for(int y = 0; y < block.size; ++y) {
                Tile tile = world.tile(offsetX + x, offsetY + y);
                if (tile == null || tile.block() != block) {
                    return false;
                }
            }
        }

        return true;
    }

    public void draw(int tileX, int tileY) {
        int offsetX = x + tileX;
        int offsetY = y + tileY;
        Vec2 mouse = Core.input.mouseWorld();//.scl(0.125f);
        float minX = offsetX - 0.5f;
        float minY = offsetY - 0.5f;
        Draw.color(Color.white, 0.4f);
        float cx = (minX + block.size / 2f) * 8;
        float cy = (minY + block.size / 2f) * 8;
        Draw.rect(block.fullIcon, cx, cy);
        Draw.flush();
        SqLog.info("(@, @) - (@, @), (@, @)", minX, minY, minX + block.size, minY + block.size, mouse.x, mouse.y);
        if (mouse.x > minX && mouse.x < minX + block.size && mouse.y > minY && mouse.y < minY + block.size) {
            drawName(cx, cy);
        }
    }

    private void drawName(float offsetX, float offsetY) {
        Color color = Pal.accent;
        String text = block.localizedName;
        Font font = Fonts.outline;
        GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
        boolean ints = font.usesIntegerPositions();
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.25f / Scl.scl(1));
        layout.setText(font, text);
        float width = layout.width;
        font.setColor(color);
        float dy = offsetY + block.size * 4f + 3;
        font.draw(text, offsetX, dy + layout.height + 1, 1);
        --dy;
        Lines.stroke(2, Color.darkGray);
        Lines.line(offsetX - width / 2 - 2, dy, offsetX + width / 2 + 1.5f, dy);
        Lines.stroke(1, color);
        Lines.line(offsetX - width / 2 - 2, dy, offsetX + width / 2 + 1.5f, dy);
        font.setUseIntegerPositions(ints);
        font.setColor(Color.white);
        font.getData().setScale(1);
        Draw.reset();
        Pools.free(layout);
        Draw.flush();
    }
}
