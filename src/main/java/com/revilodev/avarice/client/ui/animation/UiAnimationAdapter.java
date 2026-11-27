package com.revilodev.avarice.client.ui.animation;

import com.revilodev.avarice.devtools.DevelopmentProfiler;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.gui.components.AbstractWidget;

public final class UiAnimationAdapter {
    public enum Property { X, Y, WIDTH, HEIGHT, SCALE, OPACITY, COLOR }

    private final AnimationEngine engine;
    private final Map<Object, Bounds> bounds = new IdentityHashMap<>();

    public UiAnimationAdapter(AnimationEngine engine) {
        this.engine = engine;
    }

    public void animate(String id, Object target, Property property, double from, double to, long durationMs, long delayMs, Easing easing, boolean loop, boolean reverse, Runnable completion) {
        Bounds initial = bounds.computeIfAbsent(target, UiAnimationAdapter::bounds);
        engine.start(id, from, to, durationMs, delayMs, easing, loop, reverse, value -> apply(target, initial, property, value), completion);
    }

    public void advanceFrame() {
        long start = System.nanoTime();
        engine.advanceFrame();
        DevelopmentProfiler.record("ui.animations", start);
    }

    public void cancel(String id) {
        engine.cancel(id);
    }

    public void clearTargets() {
        bounds.clear();
    }

    public boolean pause(String id) {
        return engine.pause(id);
    }

    public boolean resume(String id) {
        return engine.resume(id);
    }

    public boolean restart(String id) {
        return engine.restart(id);
    }

    public boolean reverse(String id) {
        return engine.reverse(id);
    }

    private static Bounds bounds(Object target) {
        if (target instanceof AbstractWidget widget) return new Bounds(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
        throw new IllegalArgumentException("Unsupported UI animation target: " + target.getClass().getName());
    }

    private static void apply(Object target, Bounds initial, Property property, double value) {
        if (target instanceof AbstractWidget widget) applyWidget(widget, initial, property, value);
    }

    private static void applyWidget(AbstractWidget widget, Bounds initial, Property property, double value) {
        switch (property) {
            case X -> widget.setX((int) Math.round(value));
            case Y -> widget.setY((int) Math.round(value));
            case WIDTH -> widget.setWidth(Math.max(0, (int) Math.round(value)));
            case HEIGHT -> widget.setHeight(Math.max(0, (int) Math.round(value)));
            case SCALE -> {
                int width = Math.max(1, (int) Math.round(initial.width * value));
                int height = Math.max(1, (int) Math.round(initial.height * value));
                widget.setRectangle(initial.x + (initial.width - width) / 2, initial.y + (initial.height - height) / 2, width, height);
            }
            case OPACITY -> widget.setAlpha((float) Math.clamp(value, 0, 1));
            case COLOR -> { }
        }
    }

    private record Bounds(int x, int y, int width, int height) { }
}
