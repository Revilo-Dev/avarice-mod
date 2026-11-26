package com.revilodev.avarice.devtools.scaling;

import java.util.List;

/** Non-scripted curve definitions suitable for future authoritative data. */
public sealed interface ScalingCurve permits ScalingCurve.Linear, ScalingCurve.Exponential, ScalingCurve.Power, ScalingCurve.PiecewiseLinear {
    double evaluate(double input);
    record Linear(double intercept, double slope) implements ScalingCurve { public double evaluate(double input) { return intercept + slope * input; } }
    record Exponential(double base, double factor) implements ScalingCurve { public double evaluate(double input) { return base * Math.pow(factor, input); } }
    record Power(double coefficient, double exponent) implements ScalingCurve { public double evaluate(double input) { return coefficient * Math.pow(input, exponent); } }
    record PiecewiseLinear(List<Point> points) implements ScalingCurve { public double evaluate(double input) { if (points.isEmpty()) return 0; if (input <= points.getFirst().x()) return points.getFirst().y(); for (int index = 1; index < points.size(); index++) { Point right = points.get(index), left = points.get(index - 1); if (input <= right.x()) return left.y() + (right.y() - left.y()) * (input - left.x()) / (right.x() - left.x()); } return points.getLast().y(); } }
    record Point(double x, double y) { }
}
