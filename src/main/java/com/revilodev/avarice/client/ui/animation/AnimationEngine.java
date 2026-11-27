package com.revilodev.avarice.client.ui.animation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.DoubleConsumer;

public final class AnimationEngine {
    private final Map<String, Animation> running = new LinkedHashMap<>();
    private long lastFrameNanos = System.nanoTime();
    public void start(String id, double from, double to, long durationMs, long delayMs, Easing easing, boolean loop, boolean reverse, DoubleConsumer consumer, Runnable completion) { running.put(id, new Animation(from, to, Math.max(0, durationMs) * 1_000_000L, Math.max(0, delayMs) * 1_000_000L, easing, loop, reverse, consumer, completion)); }
    public void cancel(String id) { running.remove(id); }
    public void clear() { running.clear(); }
    public void advance(long deltaNanos) { for (var iterator = running.entrySet().iterator(); iterator.hasNext();) { Animation animation = iterator.next().getValue(); if (animation.advance(deltaNanos)) iterator.remove(); } }
    public void advanceFrame() { long now = System.nanoTime(); advance(now - lastFrameNanos); lastFrameNanos = now; }
    public boolean pause(String id) { Animation animation = running.get(id); if (animation == null) return false; animation.paused = true; return true; }
    public boolean resume(String id) { Animation animation = running.get(id); if (animation == null) return false; animation.paused = false; return true; }
    public boolean restart(String id) { Animation animation = running.get(id); if (animation == null) return false; animation.elapsed = 0; return true; }
    public boolean reverse(String id) { Animation animation = running.get(id); if (animation == null) return false; animation.reversed = !animation.reversed; return true; }

    private static final class Animation {
        private final double from, to; private final long duration, delay; private final Easing easing; private final boolean loop, reverse; private final DoubleConsumer consumer; private final Runnable completion; private long elapsed; private boolean paused, reversed;
        private Animation(double from, double to, long duration, long delay, Easing easing, boolean loop, boolean reverse, DoubleConsumer consumer, Runnable completion) { this.from = from; this.to = to; this.duration = duration; this.delay = delay; this.easing = easing; this.loop = loop; this.reverse = reverse; this.consumer = consumer; this.completion = completion; }
        private boolean advance(long delta) { if (paused) return false; elapsed += Math.max(0, delta); if (elapsed <= delay) return false; long active = elapsed - delay; double progress = duration == 0 ? 1 : (double) active / duration; if (loop) progress %= 1; else progress = Math.min(1, progress); if ((reverse ^ reversed) && ((active / Math.max(1, duration)) & 1) == 1) progress = 1 - progress; consumer.accept(from + (to - from) * easing.apply(progress)); if (!loop && active >= duration) { if (completion != null) completion.run(); return true; } return false; }
    }
}
