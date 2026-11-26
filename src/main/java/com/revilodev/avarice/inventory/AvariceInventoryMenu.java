package com.revilodev.avarice.inventory;

import com.revilodev.avarice.registry.AvariceMenus;
import java.util.Objects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class AvariceInventoryMenu extends AbstractContainerMenu {
    private static final Identifier PRIMARY_ATTACK_SPEED = Identifier.fromNamespaceAndPath("avarice", "primary_weapon_attack_speed");
    private static final AttributeModifier PRIMARY_ATTACK_SPEED_MODIFIER = new AttributeModifier(PRIMARY_ATTACK_SPEED, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private final Player player;

    public AvariceInventoryMenu(int containerId, Inventory inventory) {
        super(AvariceMenus.INVENTORY.get(), containerId);
        player = inventory.player;
        addArmorSlots(inventory);
        addMainInventorySlots(inventory);
        addHotbarSlots(inventory);
        addSlot(new Slot(inventory, 40, 81, 63) {
            @Override public void setByPlayer(ItemStack itemStack, ItemStack previous) { player.onEquipItem(EquipmentSlot.OFFHAND, previous, itemStack); super.setByPlayer(itemStack, previous); }
        });
    }

    private void addArmorSlots(Inventory inventory) {
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        for (int index = 0; index < slots.length; index++) {
            EquipmentSlot equipmentSlot = slots[index];
            addSlot(new Slot(inventory, 39 - index, 8, 9 + index * 18) {
                @Override public boolean mayPlace(ItemStack stack) { return player.getEquipmentSlotForItem(stack) == equipmentSlot; }
                @Override public int getMaxStackSize() { return 1; }
            });
        }
    }

    private void addMainInventorySlots(Inventory inventory) {
        for (int row = 0; row < 3; row++) for (int column = 0; column < 9; column++) addSlot(new Slot(inventory, column + (row + 1) * 9, 7 + column * 18, 83 + row * 18));
    }

    private void addHotbarSlots(Inventory inventory) {
        for (int index = 0; index < 9; index++) {
            AvariceInventorySlots.Role role = switch (index) { case 3 -> AvariceInventorySlots.Role.PRIMARY; case 4 -> AvariceInventorySlots.Role.SECONDARY; case 5 -> AvariceInventorySlots.Role.TRINKET; default -> AvariceInventorySlots.Role.GENERAL; };
            addSlot(new RestrictedSlot(inventory, index, 7 + index * 18, 142, player, role));
        }
    }

    @Override public void broadcastChanges() { super.broadcastChanges(); updatePrimaryAttackSpeed(); }
    @Override public boolean stillValid(Player player) { return Objects.equals(this.player, player); }
    @Override public ItemStack quickMoveStack(Player player, int slotIndex) { return ItemStack.EMPTY; }
    @Override public void removed(Player player) { removePrimaryAttackSpeed(); super.removed(player); }

    private void updatePrimaryAttackSpeed() {
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_SPEED);
        if (attribute == null) return;
        attribute.removeModifier(PRIMARY_ATTACK_SPEED);
        if (!getSlot(34).getItem().isEmpty() && getSlot(34).getItem().is(AvariceInventorySlots.WEAPONS)) attribute.addTransientModifier(PRIMARY_ATTACK_SPEED_MODIFIER);
    }
    private void removePrimaryAttackSpeed() { AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_SPEED); if (attribute != null) attribute.removeModifier(PRIMARY_ATTACK_SPEED); }

    private static final class RestrictedSlot extends Slot {
        private final Player player;
        private final AvariceInventorySlots.Role role;
        private RestrictedSlot(Inventory inventory, int index, int x, int y, Player player, AvariceInventorySlots.Role role) { super(inventory, index, x, y); this.player = player; this.role = role; }
        @Override public boolean mayPlace(ItemStack stack) { return AvariceInventorySlots.mayPlace(player, role, stack); }
        @Override public void setByPlayer(ItemStack stack, ItemStack previous) { super.setByPlayer(stack, previous); AvariceInventorySlots.markPlaced(player, role, previous, stack); }
    }
}
