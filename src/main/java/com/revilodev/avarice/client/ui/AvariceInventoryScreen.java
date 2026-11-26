package com.revilodev.avarice.client.ui;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.inventory.AvariceInventoryMenu;
import com.revilodev.avarice.inventory.AvariceInventorySlots;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public final class AvariceInventoryScreen extends AbstractContainerScreen<AvariceInventoryMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Avarice.MODID, "textures/gui/inventory.png");
    private static final Identifier HELMET = sprite("helmet");
    private static final Identifier CHESTPLATE = sprite("chestplate");
    private static final Identifier LEGGINGS = sprite("leggings");
    private static final Identifier BOOTS = sprite("boots");
    private static final Identifier OFFHAND = sprite("offhand");
    private static final Identifier MAGNET = sprite("magnet");
    private float mouseX;
    private float mouseY;

    public AvariceInventoryScreen(AvariceInventoryMenu menu, Inventory inventory, Component title) { super(menu, inventory, title, 178, 165); }
    @Override protected void init() { super.init(); titleLabelX = 102; titleLabelY = 11; inventoryLabelX = 7; inventoryLabelY = 72; }
    @Override protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, Component.literal("Stats"), 102, 11, 0xFFFFFFFF, false);
        graphics.text(font, Component.literal("Attack speed +%.0f%%".formatted(menu.getSlot(34).hasItem() ? 5.0 : 0.0)), 102, 24, 0xFFA78BFA, false);
        long cooldown = Math.max(AvariceInventorySlots.cooldownRemaining(minecraft.player, AvariceInventorySlots.Role.PRIMARY), Math.max(AvariceInventorySlots.cooldownRemaining(minecraft.player, AvariceInventorySlots.Role.SECONDARY), AvariceInventorySlots.cooldownRemaining(minecraft.player, AvariceInventorySlots.Role.TRINKET)));
        if (cooldown > 0) graphics.text(font, Component.literal("Slot cooldown %.1fs".formatted(cooldown / 20.0)), 102, 36, 0xFFFFAA00, false);
    }
    @Override public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) { this.mouseX = mouseX; this.mouseY = mouseY; super.extractRenderState(graphics, mouseX, mouseY, partialTick); }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, leftPos + 27, topPos + 11, leftPos + 77, topPos + 77, 30, 0.0625F, this.mouseX, this.mouseY, minecraft.player);
        drawSprite(graphics, HELMET, 8, 9);
        drawSprite(graphics, CHESTPLATE, 8, 27);
        drawSprite(graphics, LEGGINGS, 8, 45);
        drawSprite(graphics, BOOTS, 8, 63);
        drawSprite(graphics, OFFHAND, 81, 63);
        drawSprite(graphics, MAGNET, 81, 45);
    }
    private void drawSprite(GuiGraphicsExtractor graphics, Identifier sprite, int x, int y) { graphics.blit(RenderPipelines.GUI_TEXTURED, sprite, leftPos + x, topPos + y, 0, 0, 16, 16, 16, 16); }
    private static Identifier sprite(String name) { return Identifier.fromNamespaceAndPath(Avarice.MODID, "textures/gui/sprites/" + name + ".png"); }
}
