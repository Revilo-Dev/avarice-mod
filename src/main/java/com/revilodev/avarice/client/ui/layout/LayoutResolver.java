package com.revilodev.avarice.client.ui.layout;

import com.revilodev.avarice.core.config.UiConfig;
import java.util.LinkedHashMap;
import java.util.Map;

/** Resolves anchored GUI-scaled bounds independently from a specific screen implementation. */
public final class LayoutResolver {
    public record Bounds(double x, double y, double width, double height) { }
    public Map<String, Bounds> resolve(UiConfig.ScreenDefinition screen, Bounds viewport) {
        Map<String, Bounds> resolved = new LinkedHashMap<>();
        for (UiConfig.ElementDefinition element : screen.elements().values()) resolve(element, screen, viewport, resolved);
        return Map.copyOf(resolved);
    }
    private Bounds resolve(UiConfig.ElementDefinition element, UiConfig.ScreenDefinition screen, Bounds viewport, Map<String, Bounds> resolved) {
        Bounds present = resolved.get(element.id()); if (present != null) return present;
        Bounds parent = element.parent().isEmpty() ? viewport : resolve(screen.elements().get(element.parent()), screen, viewport, resolved);
        double width = dimension(element.width(), parent.width()) * element.scale();
        double height = dimension(element.height(), parent.height()) * element.scale();
        double anchorX = parent.x() + switch (element.anchor()) { case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> 0; case TOP_CENTER, CENTER, BOTTOM_CENTER -> parent.width() / 2; default -> parent.width(); };
        double anchorY = parent.y() + switch (element.anchor()) { case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 0; case CENTER_LEFT, CENTER, CENTER_RIGHT -> parent.height() / 2; default -> parent.height(); };
        Bounds result = new Bounds(anchorX + element.x() - width * element.pivotX(), anchorY + element.y() - height * element.pivotY(), width, height); resolved.put(element.id(), result); return result;
    }
    private static double dimension(UiConfig.Dimension dimension, double parent) { double value = dimension.unit() == UiConfig.Dimension.Unit.PERCENT ? parent * dimension.value() / 100.0 : dimension.value(); return Math.clamp(value, dimension.min(), dimension.max()); }
}
