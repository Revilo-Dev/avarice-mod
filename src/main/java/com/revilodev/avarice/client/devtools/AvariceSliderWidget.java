package com.revilodev.avarice.client.devtools;

import java.util.function.DoubleConsumer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public final class AvariceSliderWidget extends AbstractSliderButton {
    private final DoubleConsumer changed;

    public AvariceSliderWidget(int x, int y, int width, Component label, double value, DoubleConsumer changed) {
        super(x, y, width, DEFAULT_HEIGHT, label, Math.clamp(value, 0, 1));
        this.changed = changed;
        updateMessage();
    }

    public void setNormalizedValue(double value) {
        setValue(value);
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal("%.0f%%".formatted(value * 100)));
    }

    @Override
    protected void applyValue() {
        changed.accept(value);
    }
}
