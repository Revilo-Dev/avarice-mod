package com.revilodev.avarice.devtools.values;

import com.revilodev.avarice.devtools.scaling.ScalingFormula;

/** Immutable numeric definition and its validation limits. */
public record ValueDefinition(String id, double defaultValue, double currentValue, double minimum, double maximum, Kind kind, ScalingFormula formula) {
    public enum Kind { FIXED, PERCENTAGE, MULTIPLIER }
    public ValueDefinition { if (id.isBlank() || minimum > maximum || currentValue < minimum || currentValue > maximum) throw new IllegalArgumentException("Invalid value definition " + id); }
    public ValueDefinition withCurrentValue(double value) { return new ValueDefinition(id, defaultValue, Math.clamp(value, minimum, maximum), minimum, maximum, kind, formula); }
}
