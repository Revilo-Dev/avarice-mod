package com.revilodev.avarice.client.ui.animation;

import java.util.Locale;
import java.util.function.DoubleUnaryOperator;

public enum Easing {
    LINEAR(value -> value), EASE_IN_QUAD(value -> value * value), EASE_OUT_QUAD(value -> 1 - (1 - value) * (1 - value)),
    EASE_IN_OUT_QUAD(value -> value < .5 ? 2 * value * value : 1 - Math.pow(-2 * value + 2, 2) / 2),
    EASE_OUT_CUBIC(value -> 1 - Math.pow(1 - value, 3)), EASE_OUT_QUART(value -> 1 - Math.pow(1 - value, 4)),
    BACK(value -> 1 + 2.70158 * Math.pow(value - 1, 3) + 1.70158 * Math.pow(value - 1, 2)),
    ELASTIC(value -> value == 0 || value == 1 ? value : Math.pow(2, -10 * value) * Math.sin((value * 10 - .75) * (2 * Math.PI / 3)) + 1),
    BOUNCE(Easing::bounce);

    private final DoubleUnaryOperator function;
    Easing(DoubleUnaryOperator function) { this.function = function; }
    public double apply(double progress) { return function.applyAsDouble(Math.clamp(progress, 0, 1)); }
    public static Easing parse(String value) { try { return valueOf(value.toUpperCase(Locale.ROOT).replace('-', '_')); } catch (IllegalArgumentException exception) { return EASE_OUT_CUBIC; } }
    private static double bounce(double value) { double n = 7.5625, d = 2.75; if (value < 1 / d) return n * value * value; if (value < 2 / d) { value -= 1.5 / d; return n * value * value + .75; } if (value < 2.5 / d) { value -= 2.25 / d; return n * value * value + .9375; } value -= 2.625 / d; return n * value * value + .984375; }
}
