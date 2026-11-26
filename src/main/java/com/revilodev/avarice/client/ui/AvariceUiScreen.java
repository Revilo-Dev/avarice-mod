package com.revilodev.avarice.client.ui;

import com.daqem.uilib.gui.AbstractScreen;
import com.daqem.uilib.gui.component.color.ColorComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.uilib.gui.widget.ButtonWidget;
import com.revilodev.avarice.client.ui.layout.LayoutResolver;
import com.revilodev.avarice.core.config.UiConfig;
import com.revilodev.avarice.core.config.UiConfigService;
import com.revilodev.avarice.core.reload.AvariceReloads;
import com.revilodev.avarice.core.reload.ReloadManager;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public final class AvariceUiScreen extends AbstractScreen {
    private boolean inspectorEnabled;
    private String status = "- inspector · + reload";
    private double mouseX;
    private double mouseY;
    private String selectedId;
    private final Map<String, InspectedElement> inspected = new LinkedHashMap<>();

    public AvariceUiScreen() { super(Component.literal("Avarice Development UI")); }
    @Override protected void init() { rebuild(); }

    public void rebuild() {
        clear();
        UiConfig config = UiConfigService.current();
        UiConfig.ScreenDefinition screen = config.screens().get("development_demo");
        if (screen == null) return;
        Map<String, LayoutResolver.Bounds> layout = new LayoutResolver().resolve(screen, new LayoutResolver.Bounds(0, 0, width, height));
        inspected.clear();
        screen.elements().forEach((id, element) -> inspected.put(id, new InspectedElement(id, element.parent(), element, layout.get(id))));
        LayoutResolver.Bounds bounds = layout.get("demo_panel");
        UiConfig.Style style = config.theme().styles().get(screen.elements().get("demo_panel").style());
        int panelColor = style != null && style.background() != null ? alpha(style.background(), style.opacity()) : config.theme().colors().get("surface");
        addComponent(new ColorComponent((int) bounds.x(), (int) bounds.y(), (int) bounds.width(), (int) bounds.height(), panelColor));
        if (style != null && style.border() != null && style.borderWidth() > 0) addBorder(bounds, style.border(), style.borderWidth());
        addComponent(new TextComponent((int) bounds.x() + 12, (int) bounds.y() + 12, Component.literal("Avarice · Development Foundation"), config.theme().colors().get("text")));
        addComponent(new TextComponent((int) bounds.x() + 12, (int) bounds.y() + 34, Component.literal(status), config.theme().colors().get("primary")));
        addWidget(new ButtonWidget((int) bounds.x() + 12, (int) bounds.y() + 60, 112, 20, Component.literal("Reload UI"), button -> reloadUi()));
        addWidget(new ButtonWidget((int) bounds.x() + 136, (int) bounds.y() + 60, 112, 20, Component.literal("Close"), button -> ClientScreenAccess.open(null)));
        addWidget(new ButtonWidget((int) bounds.x() + 12, (int) bounds.y() + 86, 112, 20, Component.literal("Clear selection"), button -> { selectedId = null; rebuild(); }));
        addWidget(new ButtonWidget((int) bounds.x() + 136, (int) bounds.y() + 86, 112, 20, Component.literal("Copy inspector"), button -> copyInspector()));
        if (inspectorEnabled) addInspector(config, bounds);
    }

    public void toggleInspector() { inspectorEnabled = !inspectorEnabled; status = inspectorEnabled ? "Inspector enabled (-)" : "Inspector disabled (-)"; rebuild(); }
    public void reloadUi() { applyReload(AvariceReloads.reload(AvariceReloads.UI)); }
    public void applyReload(ReloadManager.ReloadDiagnostic result) { status = result.success() ? "UI configuration reloaded" : "Reload failed: " + result.message(); rebuild(); }
    @Override public void mouseMoved(double x, double y) { mouseX = x; mouseY = y; if (inspectorEnabled) rebuild(); super.mouseMoved(x, y); }
    @Override public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) { if (inspectorEnabled && event.button() == 0) { InspectedElement hovered = hovered(); if (hovered != null) { selectedId = hovered.id(); rebuild(); return true; } } return super.mouseClicked(event, doubleClick); }

    private void addInspector(UiConfig config, LayoutResolver.Bounds panel) {
        InspectedElement hovered = hovered();
        InspectedElement selected = selectedId == null ? null : inspected.get(selectedId);
        if (hovered != null) addBorder(hovered.bounds(), config.theme().colors().get("primary"), 1);
        if (selected != null) addBorder(selected.bounds(), 0xFFFFFF00, 2);
        addComponent(new ColorComponent((int) mouseX - 4, (int) mouseY, 9, 1, 0xFFFFFFFF));
        addComponent(new ColorComponent((int) mouseX, (int) mouseY - 4, 1, 9, 0xFFFFFFFF));
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        String details = "mouse %.0f,%.0f raw %.0f,%.0f scale %.1f screen %dx%d".formatted(mouseX, mouseY, mouseX * scale, mouseY * scale, scale, width, height);
        details += hovered == null ? " | hover none" : " | hover " + describe(hovered);
        if (selected != null) details += " | selected " + selected.id();
        addComponent(new TextComponent((int) panel.x(), (int) panel.y() - 12, Component.literal(details), config.theme().colors().get("text")));
    }
    private InspectedElement hovered() { return inspected.values().stream().filter(element -> contains(element.bounds(), mouseX, mouseY)).reduce((first, second) -> second).orElse(null); }
    private static boolean contains(LayoutResolver.Bounds bounds, double x, double y) { return bounds != null && x >= bounds.x() && x <= bounds.x() + bounds.width() && y >= bounds.y() && y <= bounds.y() + bounds.height(); }
    private String describe(InspectedElement element) { LayoutResolver.Bounds bounds = element.bounds(); return "%s parent=%s bounds=%.0f,%.0f %.0fx%.0f anchor=%s pivot=%.2f,%.2f scale=%.2f".formatted(element.id(), element.parent().isEmpty() ? "screen" : element.parent(), bounds.x(), bounds.y(), bounds.width(), bounds.height(), element.definition().anchor(), element.definition().pivotX(), element.definition().pivotY(), element.definition().scale()); }
    private void copyInspector() { InspectedElement element = selectedId == null ? hovered() : inspected.get(selectedId); Minecraft.getInstance().keyboardHandler.setClipboard(element == null ? "mouse=" + mouseX + "," + mouseY : describe(element)); status = "Inspector copied"; rebuild(); }
    private void addBorder(LayoutResolver.Bounds bounds, int color, int thickness) { int x = (int) bounds.x(), y = (int) bounds.y(), w = (int) bounds.width(), h = (int) bounds.height(); addComponent(new ColorComponent(x, y, w, thickness, color)); addComponent(new ColorComponent(x, y + h - thickness, w, thickness, color)); addComponent(new ColorComponent(x, y, thickness, h, color)); addComponent(new ColorComponent(x + w - thickness, y, thickness, h, color)); }
    private static int alpha(int color, double opacity) { return ((int) (Math.clamp(opacity, 0, 1) * 255) << 24) | (color & 0xFFFFFF); }
    private record InspectedElement(String id, String parent, UiConfig.ElementDefinition definition, LayoutResolver.Bounds bounds) { }
}
