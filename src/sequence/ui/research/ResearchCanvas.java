package sequence.ui.research;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.event.ElementGestureListener;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Align;
import mindustry.graphics.Pal;
import org.jetbrains.annotations.NotNull;
import sequence.ui.ResizeButton;

public class ResearchCanvas extends WidgetGroup {
    public static final int
//            objWidth = 5, objHeight = 2,
            bounds = 1000;
    public float unitSize = Scl.scl(48f);

    public View view;

    public ResearchCanvas() {
        setFillParent(true);

        addChild(view = new View());

        touchable = Touchable.enabled;

        addCaptureListener(new ElementGestureListener() {
            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                view.x = Mathf.clamp(view.x + deltaX, -bounds * unitSize + width, bounds * unitSize);
                view.y = Mathf.clamp(view.y + deltaY, -bounds * unitSize + height, bounds * unitSize);
            }

            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                if (view.lastZoom < 0) {
                    view.lastZoom = view.scaleX;
                }

                view.setScale(Mathf.clamp(distance / initialDistance * view.lastZoom, 0.25f, 1f));
                view.setOrigin(Align.center);
                view.setTransform(true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
                view.lastZoom = view.scaleX;
            }
        });

        addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                view.setScale(Mathf.clamp(view.scaleX - amountY / 10f * view.scaleX, 0.25f, 1f));
                view.setOrigin(Align.center);
                view.setTransform(true);
                return true;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                view.requestScroll();
                return super.mouseMoved(event, x, y);
            }
        });
    }

    public class View extends WidgetGroup {
        public float lastZoom = -1;
        public boolean moved = false;

        public View() {
            setTransform(false);
            setSize(getPrefWidth(), getPrefHeight());

            addChild(getResizeButton(15));
        }

        @NotNull
        private ResizeButton getResizeButton(int loop) {
            int i = 30 + loop * 10;
            ResizeButton button = new ResizeButton(i, i, i + 90, i + 90);
            button.speed = 0.05f;
            button.inter = Interp.circleOut;
            button.align = new Integer[]{1, 1 << 1, 1 << 2, 1 << 3, 1 << 4, Align.bottomLeft, Align.bottomRight, Align.topLeft, Align.topRight}[Mathf.random(8)];
            if (loop > 1) {
                button.add(getResizeButton(loop - 1));
            }
            return button;
        }

        @Override
        public void draw() {
            validate();
            int minX = Math.max(Mathf.floor((x - width - 1f) / getUnitSize()), -bounds), minY = Math.max(Mathf.floor((y - height - 1f) / getUnitSize()), -bounds),
                    maxX = Math.min(Mathf.ceil((x + width + 1f) / getUnitSize()), bounds), maxY = Math.min(Mathf.ceil((y + height + 1f) / getUnitSize()), bounds);
            float progX = x % getUnitSize(), progY = y % getUnitSize();

            Lines.stroke(3f);
            Draw.color(Pal.darkestGray, parentAlpha);

            for (int x = minX; x <= maxX; x++)
                Lines.line(progX + x * getUnitSize(), minY * getUnitSize(), progX + x * getUnitSize(), maxY * getUnitSize());
            for (int y = minY; y <= maxY; y++)
                Lines.line(minX * getUnitSize(), progY + y * getUnitSize(), maxX * getUnitSize(), progY + y * getUnitSize());

            drawChildren();
        }

        private float getUnitSize() {
            return unitSize * scaleX;
        }

        @Override
        public float getPrefWidth() {
            return bounds * getUnitSize();
        }

        @Override
        public float getPrefHeight() {
            return bounds * getUnitSize();
        }
    }
}
