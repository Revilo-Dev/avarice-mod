package com.revilodev.avarice.client.ui;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.client.devtools.ScreenInspectorOverlay;
import com.revilo.levelup.api.LevelUpClientApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.GuiLayer;

public final class AvariceHud {
    private static final Identifier HUD = texture("hud");
    private static final Identifier OFFHAND = texture("hud-offhand");
    private static final Identifier SELECTED_SLOT = texture("hud-selected_slot");
    private static final Identifier HEALTH = texture("hud-bar_healtht");
    private static final Identifier LEVEL = texture("hud-bar_level");
    private static final Identifier SOULS = texture("hud-bar_souls");
    private static final int HOTBAR_START_X = 2;
    private static final int HOTBAR_SLOT_Y = 21;
    private static final int SLOT_CONTENT_INSET = 1;

    private AvariceHud() { }

    public static void renderOrVanilla(GuiLayer vanillaLayer, GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        if (isCreative()) {
            vanillaLayer.render(graphics, deltaTracker);
            return;
        }
        render(graphics, deltaTracker);
    }

    public static void renderVanillaInCreative(GuiLayer vanillaLayer, GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        if (isCreative()) vanillaLayer.render(graphics, deltaTracker);
    }

    private static void render(GuiGraphicsExtractor graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        int x = (graphics.guiWidth() - 182) / 2;
        int y = graphics.guiHeight() - 48;
        graphics.blit(RenderPipelines.GUI_TEXTURED, HUD, x, y, 0, 0, 182, 44, 182, 44);
        renderBars(graphics, minecraft.font, player, x, y);
        renderHotbar(graphics, minecraft.font, player, x, y + 4);
        ScreenInspectorOverlay.renderCursorCoordinates(graphics);
    }

    private static void renderBars(GuiGraphicsExtractor graphics, Font font, Player player, int x, int y) {
        int soulsWidth = Mth.clamp(Math.round(player.experienceProgress * 84), 0, 84);
        if (soulsWidth > 0) graphics.blit(RenderPipelines.GUI_TEXTURED, SOULS, x + 3, y + 18, 0, 0, soulsWidth, 3, 84, 3);
        var levelDisplay = LevelUpClientApi.getLocalLevelProgressDisplay();
        float levelProgress = levelDisplay == null ? 0.0F : levelDisplay.progress();
        int level = levelDisplay == null ? 0 : levelDisplay.level();
        int levelWidth = Mth.clamp(Math.round(levelProgress * 83), 0, 83);
        if (levelWidth > 0) graphics.blit(RenderPipelines.GUI_TEXTURED, LEVEL, x + 179 - levelWidth, y + 18, 83 - levelWidth, 0, levelWidth, 3, 83, 3);
        float healthRatio = player.getMaxHealth() <= 0 ? 0 : player.getHealth() / player.getMaxHealth();
        int healthHeight = Math.max(1, Math.round(18 * healthRatio));
        if (healthRatio > 0) graphics.blit(RenderPipelines.GUI_TEXTURED, HEALTH, x + 82, y + 21 - healthHeight, 0, 18 - healthHeight, 19, healthHeight, 19, 18);
        outlinedCenteredSmall(graphics, font, Math.round(player.experienceProgress * 100) + "%", x + 43, y + 14, 0xFFE9B8FF);
        outlinedCenteredSmall(graphics, font, Integer.toString(level), x + 139, y + 14, 0xFFB8DEFF);
        outlinedCentered(graphics, font, Integer.toString(Mth.ceil(player.getHealth())), x + 92, y + 9, 0xFFFFB8B8);
    }

    private static void renderHotbar(GuiGraphicsExtractor graphics, Font font, Player player, int x, int y) {
        int selected = player.getInventory().getSelectedSlot();
        int slotY = y + HOTBAR_SLOT_Y;
        graphics.blit(RenderPipelines.GUI_TEXTURED, SELECTED_SLOT, hotbarSlotX(x, selected), slotY - 1, 0, 0, 18, 18, 18, 18);
        for (int index = 0; index < 9; index++) {
            ItemStack stack = player.getInventory().getItem(index);
            int itemY = slotY + SLOT_CONTENT_INSET - 1;
            int slotX = hotbarSlotX(x, index) + SLOT_CONTENT_INSET;
            graphics.item(player, stack, slotX, itemY, 0);
            graphics.itemDecorations(font, stack, slotX, itemY);
        }
        ItemStack offhand = player.getOffhandItem();
        if (!offhand.isEmpty()) {
            int offhandX = x - 24;
            int offhandY = y + 19;
            graphics.blit(RenderPipelines.GUI_TEXTURED, OFFHAND, offhandX, offhandY, 0, 0, 22, 22, 22, 22);
            graphics.item(player, offhand, offhandX + 3, offhandY + 2, 0);
            graphics.itemDecorations(font, offhand, offhandX + 3, offhandY + 2);
        }
    }

    private static int hotbarSlotX(int hudX, int index) {
        return hudX + HOTBAR_START_X + index * 20;
    }

    private static void outlinedCentered(GuiGraphicsExtractor graphics, Font font, String value, int centerX, int y, int color) {
        int x = centerX - font.width(value) / 2;
        graphics.text(font, value, x - 1, y, 0xFF000000, false);
        graphics.text(font, value, x + 1, y, 0xFF000000, false);
        graphics.text(font, value, x, y - 1, 0xFF000000, false);
        graphics.text(font, value, x, y + 1, 0xFF000000, false);
        graphics.text(font, value, x, y, color, false);
    }

    private static void outlinedCenteredSmall(GuiGraphicsExtractor graphics, Font font, String value, int centerX, int y, int color) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(centerX, y);
        graphics.pose().scale(0.8F);
        outlinedCentered(graphics, font, value, 0, 0, color);
        graphics.pose().popMatrix();
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(Avarice.MODID, "textures/gui/" + name + ".png");
    }

    private static boolean isCreative() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.gameMode != null && minecraft.gameMode.getPlayerMode().isCreative();
    }
}
