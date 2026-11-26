package com.avarice.client.ui.animation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.revilodev.avarice.client.ui.animation.AnimationEngine;
import com.revilodev.avarice.client.ui.animation.Easing;
import org.junit.jupiter.api.Test;

class AnimationEngineTest {
    @Test void interpolatesUsingFrameTime() { double[] value = {0}; AnimationEngine engine = new AnimationEngine(); engine.start("test", 0, 10, 100, 0, Easing.LINEAR, false, false, next -> value[0] = next, null); engine.advance(50_000_000); assertEquals(5, value[0]); }
    @Test void waitsForDelay() { double[] value = {0}; AnimationEngine engine = new AnimationEngine(); engine.start("test", 0, 10, 100, 50, Easing.LINEAR, false, false, next -> value[0] = next, null); engine.advance(40_000_000); assertEquals(0, value[0]); }
}
