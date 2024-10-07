package sequence.ui;

import arc.Core;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.Button;
import arc.util.Align;
import sequence.util.Util;

public class ResizeButton extends Button {
    public float originWidth, originHeight, sclWidth, sclHeight;
    public float progress = 0;
    public Interp inter = Interp.linear;
    public float speed = 0.05f;
    public int align = Align.topRight;
    private float oX, oY;

    public ResizeButton(ButtonStyle style, float originWidth, float originHeight, float sclWidth, float sclHeight) {
        super(style);
        init();
        setSizes(originWidth, originHeight, sclWidth, sclHeight);
    }

    public ResizeButton(float originWidth, float originHeight, float sclWidth, float sclHeight) {
        super();
        init();
        setSizes(originWidth, originHeight, sclWidth, sclHeight);
    }

    public ResizeButton(ButtonStyle style) {
        this(style, 32, 32, 64, 64);
    }

    public ResizeButton() {
        this(32, 32, 64, 64);
    }

    private void setSizes(float minWidth, float minHeight, float maxWidth, float maxHeight) {
        originWidth = minWidth;
        originHeight = minHeight;
        sclWidth = maxWidth;
        sclHeight = maxHeight;
    }

    private void init() {
        align = Align.center;
        update(() -> {
            float mouseX = Core.input.mouseX();
            float mouseY = Core.input.mouseY();
            Vec2 vec2 = localToStageCoordinates(new Vec2(x, y));
            float x1 = vec2.x;
            float y1 = vec2.y;
            boolean in = Util.INSTANCE.inZone(x1, y1, x1 + getWidth(), y1 + getHeight(), mouseX, mouseY);

            boolean notZero = !Mathf.zero(progress);

            if (in) progress += speed;
            else progress -= speed;
            progress = Mathf.clamp(progress);
            if (Mathf.zero(progress)) {
                if (notZero) {
                    x = oX;
                    y = oY;
                }
                oX = x;
                oY = y;
            }

            setSize(getPrefWidth(), getPrefHeight());
            float x = oX;
            float y = oY;
            float h = (sclHeight - originHeight) * getAppliedProgress();
            float w = (sclWidth - originWidth) * getAppliedProgress();

            if (Align.isCenterHorizontal(align)) x -= w / 2;
            else if (Align.isRight(align)) x -= w;
            if (Align.isCenterVertical(align)) y -= h / 2;
            else if (Align.isTop(align)) y -= h;

            setPosition(x, y);
            validate();
        });
    }

    @Override
    public float getPrefHeight() {
        return originHeight + (sclHeight - originHeight) * getAppliedProgress();
    }

    @Override
    public float getPrefWidth() {
        return originWidth + (sclWidth - originWidth) * getAppliedProgress();
    }

    private float getAppliedProgress() {
        return inter.apply(progress);
    }
}
