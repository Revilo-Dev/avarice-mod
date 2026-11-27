package com.revilodev.avarice.client.devtools;

import com.revilodev.avarice.Avarice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Avarice.MODID, value = Dist.CLIENT)
public final class ScreenInspectorOverlay {
    private static boolean enabled;
    private static GuiEventListener selected;

    private ScreenInspectorOverlay() { }

    public static void toggle() { enabled = !enabled; if (!enabled) selected = null; }
    public static boolean enabled() { return enabled; }

    public static void renderCursorCoordinates(GuiGraphicsExtractor graphics) {
        if (!enabled) return;
        Minecraft minecraft = Minecraft.getInstance();
        int mouseX = (int) minecraft.mouseHandler.getScaledXPos(minecraft.getWindow());
        int mouseY = (int) minecraft.mouseHandler.getScaledYPos(minecraft.getWindow());
        String value = "%d, %d".formatted(mouseX, mouseY);
        int textX = mouseX - minecraft.font.width(value) / 2;
        int textY = Math.max(2, mouseY - 12);
        graphics.fill(textX - 2, textY - 2, textX + minecraft.font.width(value) + 2, textY + 10, 0xB0000000);
        graphics.text(minecraft.font, value, textX, textY, 0xFFFFFFFF, false);
    }

    @SubscribeEvent
    public static void render(ScreenEvent.Render.Post event) {
        if (!enabled) return;
        GuiEventListener hovered = hovered(event.getScreen(), event.getMouseX(), event.getMouseY());
        if (hovered instanceof LayoutElement element) event.getGuiGraphics().outline(element.getX(), element.getY(), element.getWidth(), element.getHeight(), 0xFFA78BFA);
        if (selected instanceof LayoutElement element) event.getGuiGraphics().outline(element.getX(), element.getY(), element.getWidth(), element.getHeight(), 0xFFFFFF00);
        event.getGuiGraphics().horizontalLine(event.getMouseX() - 4, event.getMouseX() + 4, event.getMouseY(), 0xFFFFFFFF);
        event.getGuiGraphics().verticalLine(event.getMouseX(), event.getMouseY() - 4, event.getMouseY() + 4, 0xFFFFFFFF);
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        String details = "- inspector | gui %d,%d raw %.0f,%.0f scale %.1f".formatted(event.getMouseX(), event.getMouseY(), event.getMouseX() * scale, event.getMouseY() * scale, scale);
        if (hovered != null) details += " | hover " + describe(hovered);
        if (selected != null) details += " | selected " + describe(selected);
        int textX = event.getMouseX() - Minecraft.getInstance().font.width(details) / 2;
        int textY = Math.max(2, event.getMouseY() - 12);
        event.getGuiGraphics().fill(textX - 2, textY - 2, textX + Minecraft.getInstance().font.width(details) + 2, textY + 10, 0xB0000000);
        event.getGuiGraphics().text(Minecraft.getInstance().font, details, textX, textY, 0xFFFFFFFF, false);
    }

    @SubscribeEvent
    public static void click(ScreenEvent.MouseButtonPressed.Pre event) {
        if (!enabled || event.getButton() != 0) return;
        GuiEventListener hovered = hovered(event.getScreen(), event.getMouseX(), event.getMouseY());
        if (hovered == null) return;
        selected = hovered;
        event.setCanceled(true);
    }

    private static GuiEventListener hovered(Screen screen, double mouseX, double mouseY) {
        return screen.children().stream().filter(child -> child.isMouseOver(mouseX, mouseY)).reduce((first, second) -> second).orElse(null);
    }

    private static String describe(GuiEventListener listener) {
        if (listener instanceof LayoutElement element) return "%s %d,%d %dx%d".formatted(listener.getClass().getSimpleName(), element.getX(), element.getY(), element.getWidth(), element.getHeight());
        return listener.getClass().getSimpleName();
    }
}
