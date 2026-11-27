package com.revilodev.avarice.client.devtools;

import com.revilodev.avarice.devtools.values.Percentage;
import com.revilodev.avarice.devtools.values.ValueDefinition;
import com.revilodev.avarice.devtools.values.Values;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class AvariceValueEditorScreen extends Screen {
    private static final DecimalFormat NUMBER = new DecimalFormat("0.####");
    private final Map<String, ValueDefinition> draft = new LinkedHashMap<>();
    private String selectedId;
    private EditBox input;
    private AvariceSliderWidget slider;
    private String feedback = "";
    private boolean updatingInput;

    public AvariceValueEditorScreen() {
        super(Component.translatable("screen.avarice.values"));
    }

    @Override
    protected void init() {
        if (draft.isEmpty()) draft.putAll(Values.current());
        selectedId = draft.keySet().stream().findFirst().orElse(null);
        ValueDefinition value = selected();
        if (value == null) return;
        int panelX = width / 2 - 170;
        int panelY = height / 2 - 110;
        input = new EditBox(font, panelX + 12, panelY + 88, 104, 20, Component.translatable("screen.avarice.values.input"));
        input.setValue(NUMBER.format(value.currentValue()));
        input.setResponder(this::parseInput);
        addRenderableWidget(input);
        slider = addRenderableWidget(new AvariceSliderWidget(panelX + 124, panelY + 88, 120, Component.empty(), normalized(value), this::setNormalized));
        addRenderableWidget(Button.builder(Component.literal("-"), button -> adjust(-step(value))).bounds(panelX + 252, panelY + 88, 28, 20).build());
        addRenderableWidget(Button.builder(Component.literal("+"), button -> adjust(step(value))).bounds(panelX + 288, panelY + 88, 28, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("button.avarice.reset"), button -> setValue(value.defaultValue())).bounds(panelX + 12, panelY + 114, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("button.avarice.add_points"), button -> specialAdjustment()).bounds(panelX + 116, panelY + 114, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("button.avarice.apply"), button -> applyDraft()).bounds(panelX + 220, panelY + 114, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("button.avarice.save"), button -> saveDraft()).bounds(panelX + 12, panelY + 190, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose()).bounds(panelX + 220, panelY + 190, 96, 20).build());
    }

    private void parseInput(String text) { if (!updatingInput) try { setValue(Double.parseDouble(text)); feedback = "Preview updated"; } catch (RuntimeException exception) { feedback = "Enter a valid number"; } }
    private void setNormalized(double normalized) { ValueDefinition value = selected(); if (value != null) setValue(value.minimum() + normalized * (value.maximum() - value.minimum())); }
    private void adjust(double amount) { ValueDefinition value = selected(); if (value != null) setValue(value.currentValue() + amount); }
    private void specialAdjustment() { ValueDefinition value = selected(); if (value == null) return; setValue(value.kind() == ValueDefinition.Kind.PERCENTAGE ? Percentage.points(value.currentValue()).addPoints(5).asPercent() : value.currentValue() + step(value)); }
    private void setValue(double value) { ValueDefinition current = selected(); if (current == null) return; draft.put(selectedId, current.withCurrentValue(value)); refreshInput(); }
    private void applyDraft() { try { Values.apply(draft); feedback = "Values applied"; } catch (RuntimeException exception) { feedback = "Values are invalid"; } }
    private void saveDraft() { try { Values.save(draft); feedback = "Values saved"; } catch (IOException | RuntimeException exception) { feedback = "Could not save values"; } }
    private void refreshInput() { ValueDefinition value = selected(); if (value == null || input == null) return; updatingInput = true; input.setValue(NUMBER.format(value.currentValue())); slider.setNormalizedValue(normalized(value)); updatingInput = false; }
    private ValueDefinition selected() { return selectedId == null ? null : draft.get(selectedId); }
    private static double normalized(ValueDefinition value) { return value.maximum() == value.minimum() ? 0 : Math.clamp((value.currentValue() - value.minimum()) / (value.maximum() - value.minimum()), 0, 1); }
    private static double step(ValueDefinition value) { return Math.max(.01, (value.maximum() - value.minimum()) / 100); }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int panelX = width / 2 - 170;
        int panelY = height / 2 - 110;
        graphics.fill(panelX, panelY, panelX + 340, panelY + 220, 0xEE232936);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        ValueDefinition value = selected();
        graphics.text(font, title, panelX + 12, panelY + 12, 0xFFFFFFFF, false);
        if (value == null) { graphics.text(font, "No values are available", panelX + 12, panelY + 36, 0xFFFF8080, false); return; }
        graphics.text(font, value.id() + " · " + value.kind(), panelX + 12, panelY + 36, 0xFFA78BFA, false);
        graphics.text(font, "Limits: " + NUMBER.format(value.minimum()) + " to " + NUMBER.format(value.maximum()), panelX + 12, panelY + 52, 0xFFCCCCCC, false);
        graphics.text(font, "Preview: " + NUMBER.format(value.currentValue()), panelX + 12, panelY + 144, 0xFFFFFFFF, false);
        graphics.text(font, feedback, panelX + 12, panelY + 160, 0xFF80FF80, false);
    }
}
