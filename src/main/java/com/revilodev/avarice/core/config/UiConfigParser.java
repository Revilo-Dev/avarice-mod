package com.revilodev.avarice.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Parses and validates the versioned Avarice UI JSON format. */
public final class UiConfigParser {
    public UiConfig parse(String json, String source) throws ConfigException {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            int version = integer(root, "schema_version", 1, source);
            if (version != 1) throw new ConfigException(source + ": $.schema_version: unsupported version " + version);
            JsonObject theme = object(root, "theme", source);
            Map<String, Integer> colors = colors(object(theme, "colors", source), source);
            Map<String, Double> spacing = numbers(object(theme, "spacing", source), source + ".theme.spacing");
            double typographyScale = number(object(theme, "typography", source), "default_scale", 1, source);
            double uiScale = number(theme, "ui_scale", 1, source);
            if (typographyScale <= 0 || uiScale <= 0) throw new ConfigException(source + ": scales must be positive");
            Map<String, UiConfig.Style> styles = styles(optionalObject(theme, "styles"), colors, source);
            JsonObject animations = object(root, "animations", source);
            UiConfig.Animations animationConfig = new UiConfig.Animations(
                    integer(animations, "screen_open_ms", 300, source), integer(animations, "screen_close_ms", 200, source),
                    integer(animations, "hover_ms", 140, source), string(animations, "easing", "ease_out_cubic", source));
            validateAnimation(animationConfig, source);
            Map<String, UiConfig.ScreenDefinition> screens = screens(object(root, "screens", source), styles.keySet(), source);
            return new UiConfig(version, new UiConfig.Theme(Map.copyOf(colors), Map.copyOf(spacing), typographyScale, uiScale, Map.copyOf(styles)), animationConfig, Map.copyOf(screens));
        } catch (IllegalStateException | IllegalArgumentException exception) {
            throw new ConfigException(source + ": invalid JSON structure: " + exception.getMessage(), exception);
        }
    }

    private static Map<String, Integer> colors(JsonObject input, String source) throws ConfigException {
        Map<String, Integer> output = new LinkedHashMap<>();
        for (var entry : input.entrySet()) output.put(entry.getKey(), color(entry.getValue().getAsString(), input, source, Set.of()));
        return output;
    }
    private static int color(String raw, JsonObject colors, String source, Set<String> resolving) throws ConfigException {
        if (raw.startsWith("$theme.colors.")) {
            String name = raw.substring("$theme.colors.".length());
            if (!colors.has(name)) throw new ConfigException(source + ": unknown theme reference " + raw);
            if (resolving.contains(name)) throw new ConfigException(source + ": circular theme reference at " + raw);
            java.util.HashSet<String> next = new java.util.HashSet<>(resolving); next.add(name);
            return color(colors.get(name).getAsString(), colors, source, next);
        }
        String hex = raw.startsWith("#") ? raw.substring(1) : raw;
        if (!hex.matches("[0-9a-fA-F]{6}([0-9a-fA-F]{2})?")) throw new ConfigException(source + ": invalid colour " + raw);
        long value = Long.parseLong(hex, 16);
        return hex.length() == 6 ? (int) (0xFF000000L | value) : (int) value;
    }
    private static Map<String, UiConfig.Style> styles(JsonObject input, Map<String, Integer> colors, String source) throws ConfigException {
        if (input == null) return Map.of();
        Map<String, UiConfig.Style> output = new LinkedHashMap<>();
        for (var entry : input.entrySet()) {
            JsonObject style = entry.getValue().getAsJsonObject();
            Integer background = style.has("background") ? resolveColor(style.get("background").getAsString(), colors, source) : null;
            Integer border = style.has("border") ? resolveColor(style.get("border").getAsString(), colors, source) : null;
            output.put(entry.getKey(), new UiConfig.Style(background, border, integer(style, "border_width", 0, source), integer(style, "corner_radius", 0, source), number(style, "opacity", 1, source), integer(style, "padding", 0, source), integer(style, "margin", 0, source)));
        }
        return output;
    }
    private static Integer resolveColor(String raw, Map<String, Integer> colors, String source) throws ConfigException {
        if (raw.startsWith("$theme.colors.")) { Integer result = colors.get(raw.substring(14)); if (result == null) throw new ConfigException(source + ": unknown theme reference " + raw); return result; }
        return color(raw, new JsonObject(), source, Set.of());
    }
    private static Map<String, UiConfig.ScreenDefinition> screens(JsonObject input, Set<String> styles, String source) throws ConfigException {
        Map<String, UiConfig.ScreenDefinition> output = new LinkedHashMap<>();
        for (var screenEntry : input.entrySet()) {
            JsonObject screen = screenEntry.getValue().getAsJsonObject();
            int width = integer(screen, "width", 320, source), height = integer(screen, "height", 180, source);
            if (width <= 0 || height <= 0) throw new ConfigException(source + ": screen dimensions must be positive");
            Map<String, UiConfig.ElementDefinition> elements = new LinkedHashMap<>();
            JsonObject rawElements = object(screen, "elements", source);
            for (var elementEntry : rawElements.entrySet()) {
                JsonObject element = elementEntry.getValue().getAsJsonObject();
                String style = string(element, "style", "", source);
                if (!style.isEmpty() && !styles.contains(style)) throw new ConfigException(source + ": unknown style " + style);
                double scale = number(element, "scale", 1, source);
                double pivotX = number(element, "pivot_x", 0.5, source), pivotY = number(element, "pivot_y", 0.5, source);
                if (scale <= 0 || pivotX < 0 || pivotX > 1 || pivotY < 0 || pivotY > 1) throw new ConfigException(source + ": invalid element scale or pivot");
                elements.put(elementEntry.getKey(), new UiConfig.ElementDefinition(elementEntry.getKey(), string(element, "parent", "", source), UiConfig.Anchor.parse(string(element, "anchor", "top_left", source)), number(element, "x", 0, source), number(element, "y", 0, source), dimension(element, "width", source), dimension(element, "height", source), scale, pivotX, pivotY, !element.has("visible") || element.get("visible").getAsBoolean(), style));
            }
            validateParents(elements, source); output.put(screenEntry.getKey(), new UiConfig.ScreenDefinition(width, height, Map.copyOf(elements)));
        }
        return output;
    }
    private static UiConfig.Dimension dimension(JsonObject object, String key, String source) throws ConfigException {
        JsonElement element = object.get(key); if (element == null) throw new ConfigException(source + ": missing $." + key);
        String value = element.getAsString(); boolean percent = value.endsWith("%"); double parsed = Double.parseDouble(percent ? value.substring(0, value.length() - 1) : value);
        if (parsed < 0) throw new ConfigException(source + ": " + key + " cannot be negative");
        return new UiConfig.Dimension(parsed, percent ? UiConfig.Dimension.Unit.PERCENT : UiConfig.Dimension.Unit.PIXELS, number(object, "min_" + key, 0, source), number(object, "max_" + key, Double.MAX_VALUE, source));
    }
    private static void validateParents(Map<String, UiConfig.ElementDefinition> elements, String source) throws ConfigException { for (UiConfig.ElementDefinition element : elements.values()) visit(element, elements, new java.util.HashSet<>(), source); }
    private static void visit(UiConfig.ElementDefinition element, Map<String, UiConfig.ElementDefinition> elements, Set<String> stack, String source) throws ConfigException { if (element.parent().isEmpty()) return; if (!elements.containsKey(element.parent())) throw new ConfigException(source + ": element " + element.id() + " has missing parent " + element.parent()); if (!stack.add(element.id())) throw new ConfigException(source + ": circular layout parent at " + element.id()); visit(elements.get(element.parent()), elements, stack, source); stack.remove(element.id()); }
    private static void validateAnimation(UiConfig.Animations animations, String source) throws ConfigException { if (animations.screenOpenMs() < 0 || animations.screenCloseMs() < 0 || animations.hoverMs() < 0) throw new ConfigException(source + ": animation durations cannot be negative"); }
    private static JsonObject object(JsonObject parent, String name, String source) throws ConfigException { JsonObject result = optionalObject(parent, name); if (result == null) throw new ConfigException(source + ": missing $." + name); return result; }
    private static JsonObject optionalObject(JsonObject parent, String name) { return parent.has(name) && parent.get(name).isJsonObject() ? parent.getAsJsonObject(name) : null; }
    private static String string(JsonObject object, String name, String fallback, String source) { return object.has(name) ? object.get(name).getAsString() : fallback; }
    private static int integer(JsonObject object, String name, int fallback, String source) { return object.has(name) ? object.get(name).getAsInt() : fallback; }
    private static double number(JsonObject object, String name, double fallback, String source) { return object.has(name) ? object.get(name).getAsDouble() : fallback; }
    private static Map<String, Double> numbers(JsonObject input, String location) { Map<String, Double> result = new LinkedHashMap<>(); for (var entry : input.entrySet()) result.put(entry.getKey(), entry.getValue().getAsDouble()); return result; }
}
