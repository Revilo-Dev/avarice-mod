package com.revilodev.avarice.devtools.scaling;

/** Safe, predefined formula: (base + growth * level) * multiplier, clamped. */
public record ScalingFormula(double base, double growth, double multiplier, double minimum, double maximum) {
    public double evaluate(double level) { return Math.clamp((base + growth * level) * multiplier, minimum, maximum); }
}
