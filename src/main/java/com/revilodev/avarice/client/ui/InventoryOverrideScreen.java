package com.revilodev.avarice.client.ui;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.inventory.AvariceInventoryMenu;
import com.revilodev.avarice.inventory.AvariceInventorySlots;
import com.revilodev.avarice.network.ClientDevelopmentState;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public final class InventoryOverrideScreen extends AbstractContainerScreen<AvariceInventoryMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Avarice.MODID, "textures/gui/inventory.png");
    private static final Identifier HELMET = sprite("helmet");
    private static final Identifier CHESTPLATE = sprite("chestplate");
    private static final Identifier LEGGINGS = sprite("leggings");
    private static final Identifier BOOTS = sprite("boots");
    private static final Identifier OFFHAND = sprite("offhand");
    private static final Identifier MAGNET = sprite("magnet");
    private static final Identifier SWORD = sprite("sword");
    private static final Identifier ARTIFACT = sprite("artifact");
    private long invalidWeaponAt = Long.MIN_VALUE;
    private int invalidSlot = -1;
    private float mouseX;
    private float mouseY;

    public InventoryOverrideScreen(AvariceInventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) { }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, leftPos + 26, topPos + 8, leftPos + 75, topPos + 75, 30, 0.0625F, this.mouseX, this.mouseY, minecraft.player);
        drawEmptySlotSprite(graphics, HELMET, 0, 8, 8);
        drawEmptySlotSprite(graphics, CHESTPLATE, 1, 8, 26);
        drawEmptySlotSprite(graphics, LEGGINGS, 2, 8, 44);
        drawEmptySlotSprite(graphics, BOOTS, 3, 8, 62);
        drawSlotIcon(graphics, OFFHAND, 41);
        drawSlotIcon(graphics, MAGNET, 40);
        drawEmptySlotSprite(graphics, SWORD, 34, AvariceInventoryMenu.hotbarSlotX(3), AvariceInventoryMenu.HOTBAR_SLOT_Y);
        drawEmptySlotSprite(graphics, SWORD, 35, AvariceInventoryMenu.hotbarSlotX(4), AvariceInventoryMenu.HOTBAR_SLOT_Y);
        drawEmptySlotSprite(graphics, ARTIFACT, 36, AvariceInventoryMenu.hotbarSlotX(5), AvariceInventoryMenu.HOTBAR_SLOT_Y);
        extractCoins(graphics);
        extractRoleTooltip(graphics);
        extractInvalidWeaponFeedback(graphics);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        Slot slot = getSlotAt(event.x(), event.y());
        if (slot != null && isWeaponSlot(slot.index) && !menu.getCarried().isEmpty() && !AvariceInventorySlots.mayPlace(minecraft.player, weaponRole(slot.index), menu.getCarried())) {
            invalidSlot = slot.index;
            invalidWeaponAt = minecraft.level.getGameTime();
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void extractInvalidWeaponFeedback(GuiGraphicsExtractor graphics) {
        if (invalidSlot < 0 || minecraft.level == null) return;
        long elapsed = minecraft.level.getGameTime() - invalidWeaponAt;
        if (elapsed < 0 || elapsed > 20) {
            invalidSlot = -1;
            return;
        }
        Slot slot = menu.getSlot(invalidSlot);
        int shake = (int) (Math.sin(elapsed * 3.5D) * (20 - elapsed) / 5.0D);
        int alpha = (int) ((20 - elapsed) * (140 + (int) (115 * (Math.sin(elapsed * 1.8D) + 1.0D) / 2.0D)) / 20);
        int x = leftPos + slot.x + shake;
        int y = topPos + slot.y;
        graphics.fill(x - 1, y - 1, x + 17, y + 17, alpha << 24 | 0x00FF3030);
        String message = "Weapons only";
        graphics.text(font, message, (int) mouseX - font.width(message) / 2 + shake, (int) mouseY - 18 - (int) elapsed, alpha << 24 | 0x00FF3030, false);
    }

    private void extractRoleTooltip(GuiGraphicsExtractor graphics) {
        Slot slot = getSlotAt(mouseX, mouseY);
        if (slot == null || !slot.getItem().isEmpty()) return;
        String message = switch (slot.index) {
            case 31 -> "Secondary weapon";
            case 32 -> "Primary weapon";
            case 33 -> "Artifact";
            default -> null;
        };
        if (message == null) return;
        int x = (int) mouseX + 12;
        int y = (int) mouseY - 12;
        graphics.fill(x - 3, y - 3, x + font.width(message) + 3, y + 11, 0xE0101010);
        graphics.text(font, message, x, y, 0xFFFFFFFF, false);
    }

    private Slot getSlotAt(double mouseX, double mouseY) {
        for (Slot slot : menu.slots) if (isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY)) return slot;
        return null;
    }

    private void extractCoins(GuiGraphicsExtractor graphics) {
        long coins = ClientDevelopmentState.current()
                .filter(state -> minecraft.player != null && state.playerId().equals(minecraft.player.getUUID()))
                .map(ClientDevelopmentState.Snapshot::coins)
                .orElse(0L);
        String value = "Coins: " + coins;
        graphics.pose().pushMatrix();
        graphics.pose().translate(leftPos + 51, topPos + 66);
        graphics.pose().scale(0.5F);
        graphics.text(font, value, -font.width(value) / 2, 0, 0xFFFFD760, false);
        graphics.pose().popMatrix();
    }

    private static boolean isWeaponSlot(int index) {
        return index == 31 || index == 32;
    }

    private static AvariceInventorySlots.Role weaponRole(int index) {
        return index == 31 ? AvariceInventorySlots.Role.SECONDARY : AvariceInventorySlots.Role.PRIMARY;
    }

    private void drawSprite(GuiGraphicsExtractor graphics, Identifier sprite, int x, int y) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, sprite, leftPos + x, topPos + y, 0, 0, 16, 16, 16, 16);
    }

    private void drawEmptySlotSprite(GuiGraphicsExtractor graphics, Identifier sprite, int slotIndex, int x, int y) {
        if (menu.getSlot(slotIndex).getItem().isEmpty()) drawSprite(graphics, sprite, x, y);
    }

    private void drawSlotIcon(GuiGraphicsExtractor graphics, Identifier sprite, int slotIndex) {
        Slot slot = menu.getSlot(slotIndex);
        if (slot.getItem().isEmpty()) drawSprite(graphics, sprite, slot.x - 1, slot.y);
    }

    private static Identifier sprite(String name) {
        return Identifier.fromNamespaceAndPath(Avarice.MODID, "textures/gui/sprites/" + name + ".png");
    }
}
