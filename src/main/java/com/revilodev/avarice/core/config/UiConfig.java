package com.revilodev.avarice.core.config;

import java.util.Map;

/** Immutable, fully resolved UI configuration snapshot. */
public record UiConfig(int schemaVersion, Theme theme, Animations animations, Map<String, ScreenDefinition> screens) {
    public record Theme(Map<String, Integer> colors, Map<String, Double> spacing, double typographyScale,
                        double uiScale, Map<String, Style> styles) { }
    public record Style(Integer background, Integer border, int borderWidth, int cornerRadius, double opacity,
                        int padding, int margin) { }
    public record Animations(int screenOpenMs, int screenCloseMs, int hoverMs, String easing) { }
    public record ScreenDefinition(int width, int height, Map<String, ElementDefinition> elements) { }
    public record ElementDefinition(String id, String parent, Anchor anchor, double x, double y,
                                    Dimension width, Dimension height, double scale, double pivotX, double pivotY,
                                    boolean visible, String style) { }
    public record Dimension(double value, Unit unit, double min, double max) {
        public enum Unit { PIXELS, PERCENT }
    }
    public enum Anchor {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, CENTER, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT;
        public static Anchor parse(String value) {
            return Anchor.valueOf(value.replace('-', '_').replace(' ', '_').toUpperCase(java.util.Locale.ROOT));
        }
    }
}
