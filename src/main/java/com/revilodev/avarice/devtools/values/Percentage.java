package com.revilodev.avarice.devtools.values;

/** Explicit percentage semantics avoid confusing percentage-point and relative adjustments. */
public record Percentage(double fraction) {
    public static Percentage points(double percent) { return new Percentage(percent / 100.0); }
    public Percentage addPoints(double points) { return new Percentage(fraction + points / 100.0); }
    public Percentage increaseRelative(double percent) { return new Percentage(fraction * (1 + percent / 100.0)); }
    public double asPercent() { return fraction * 100.0; }
}
