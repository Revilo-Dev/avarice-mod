package com.revilodev.avarice.client.ui;

import com.revilodev.avarice.core.reload.AvariceReloads;
import com.revilodev.avarice.core.reload.ReloadManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class AvariceUiScreen extends Screen {
    private String status = "- inspector  + reload";

    public AvariceUiScreen() {
        super(Component.literal("Avarice Development UI"));
    }

    @Override
    protected void init() {
        int panelX = width / 2 - 140;
        int panelY = height / 2 - 60;
        addRenderableWidget(Button.builder(Component.literal("Reload UI"), button -> reloadUi()).bounds(panelX + 12, panelY + 42, 112, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Close"), button -> onClose()).bounds(panelX + 136, panelY + 42, 112, 20).build());
    }

    public void toggleInspector() {
        status = "Inspector " + (status.startsWith("Inspector enabled") ? "disabled" : "enabled") + " (-)";
    }

    public void reloadUi() {
        applyReload(AvariceReloads.reload(AvariceReloads.UI));
    }

    public void applyReload(ReloadManager.ReloadDiagnostic result) {
        status = result.success() ? "UI configuration reloaded" : "Reload failed: " + result.message();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int panelX = width / 2 - 140;
        int panelY = height / 2 - 60;
        graphics.fill(panelX, panelY, panelX + 280, panelY + 120, 0xEE232936);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.text(font, title, panelX + 12, panelY + 12, 0xFFFFFFFF, false);
        graphics.text(font, status, panelX + 12, panelY + 28, 0xFFA78BFA, false);
    }
}
