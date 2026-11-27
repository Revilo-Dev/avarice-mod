package com.revilodev.avarice.devtools.scaling;

import java.util.List;

public record ScalingCurveDefinition(Kind kind, double first, double second, List<ScalingCurve.Point> points) {
    public enum Kind { LINEAR, EXPONENTIAL, POWER, PIECEWISE_LINEAR }

    public ScalingCurveDefinition {
        kind = kind == null ? Kind.LINEAR : kind;
        points = points == null ? List.of() : List.copyOf(points);
        if (!Double.isFinite(first) || !Double.isFinite(second)) throw new IllegalArgumentException("Invalid scaling curve");
        if (kind == Kind.PIECEWISE_LINEAR && points.size() < 2) throw new IllegalArgumentException("Piecewise curves need at least two points");
    }

    public static ScalingCurveDefinition linear() {
        return new ScalingCurveDefinition(Kind.LINEAR, 0, 1, List.of());
    }

    public ScalingCurve toCurve() {
        return switch (kind) {
            case LINEAR -> new ScalingCurve.Linear(first, second);
            case EXPONENTIAL -> new ScalingCurve.Exponential(first, second);
            case POWER -> new ScalingCurve.Power(first, second);
            case PIECEWISE_LINEAR -> new ScalingCurve.PiecewiseLinear(points);
        };
    }
}
