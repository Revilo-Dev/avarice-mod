package com.revilodev.avarice.client.ui;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.inventory.AvariceInventoryMenu;
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
    private static final Identifier SWORD = sprite("sword");
    private static final Identifier ARTIFACT = sprite("artifact");
    private float mouseX;
    private float mouseY;

    public AvariceInventoryScreen(AvariceInventoryMenu menu, Inventory inventory, Component title) { super(menu, inventory, title, 178, 164); }
    @Override protected void init() { super.init(); leftPos--; topPos--; }
    @Override public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) { this.mouseX = mouseX; this.mouseY = mouseY; super.extractRenderState(graphics, mouseX, mouseY, partialTick); }
    @Override protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) { }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, leftPos + 27, topPos + 11, leftPos + 77, topPos + 77, 30, 0.0625F, this.mouseX, this.mouseY, minecraft.player);
        drawSprite(graphics, HELMET, 8, 9);
        drawSprite(graphics, CHESTPLATE, 8, 27);
        drawSprite(graphics, LEGGINGS, 8, 45);
        drawSprite(graphics, BOOTS, 8, 63);
        drawSprite(graphics, OFFHAND, 81, 63);
        drawSlotFrame(graphics, 81, 45);
        drawSprite(graphics, MAGNET, 82, 46);
        drawSprite(graphics, SWORD, AvariceInventoryMenu.hotbarSlotX(3), AvariceInventoryMenu.HOTBAR_SLOT_Y);
        drawSprite(graphics, SWORD, AvariceInventoryMenu.hotbarSlotX(4), AvariceInventoryMenu.HOTBAR_SLOT_Y);
        drawSprite(graphics, ARTIFACT, AvariceInventoryMenu.hotbarSlotX(5), AvariceInventoryMenu.HOTBAR_SLOT_Y);
    }
    private void drawSprite(GuiGraphicsExtractor graphics, Identifier sprite, int x, int y) { graphics.blit(RenderPipelines.GUI_TEXTURED, sprite, leftPos + x, topPos + y, 0, 0, 16, 16, 16, 16); }
    private void drawSlotFrame(GuiGraphicsExtractor graphics, int x, int y) { int left = leftPos + x; int top = topPos + y; graphics.fill(left, top, left + 18, top + 18, 0xFF0B1118); graphics.fill(left + 1, top + 1, left + 17, top + 17, 0xFF1D2834); graphics.fill(left + 2, top + 2, left + 16, top + 16, 0xFF101820); }
    private static Identifier sprite(String name) { return Identifier.fromNamespaceAndPath(Avarice.MODID, "textures/gui/sprites/" + name + ".png"); }
}
