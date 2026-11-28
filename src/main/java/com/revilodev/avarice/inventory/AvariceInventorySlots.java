package com.revilodev.avarice.inventory;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.world.AvariceDimensions;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AvariceInventorySlots {
    public static final TagKey<Item> WEAPONS = TagKey.create(net.minecraft.core.registries.Registries.ITEM, Identifier.fromNamespaceAndPath(Avarice.MODID, "weapons"));
    public static final TagKey<Item> TRINKETS = TagKey.create(net.minecraft.core.registries.Registries.ITEM, Identifier.fromNamespaceAndPath(Avarice.MODID, "trinkets"));
    public static final TagKey<Item> MAGNETS = TagKey.create(net.minecraft.core.registries.Registries.ITEM, Identifier.fromNamespaceAndPath(Avarice.MODID, "magnets"));
    public static final long REPLACEMENT_COOLDOWN_TICKS = 20L * 10L;
    private static final Map<UUID, long[]> COOLDOWNS = new ConcurrentHashMap<>();

    private AvariceInventorySlots() { }

    public static boolean mayPlace(Player player, Role role, ItemStack stack) {
        if (stack.isEmpty()) return true;
        return switch (role) {
            case GENERAL -> !stack.is(WEAPONS) && !stack.is(TRINKETS);
            case PRIMARY, SECONDARY -> stack.is(WEAPONS) && !onCooldown(player, role);
            case TRINKET -> stack.is(TRINKETS) && !onCooldown(player, role);
        };
    }

    public static void markPlaced(Player player, Role role, ItemStack previous, ItemStack current) {
        if (!isCooldownRole(role) || current.isEmpty() || ItemStack.isSameItemSameComponents(previous, current) || !inDungeon(player)) return;
        COOLDOWNS.computeIfAbsent(player.getUUID(), ignored -> new long[3])[cooldownIndex(role)] = player.level().getGameTime() + REPLACEMENT_COOLDOWN_TICKS;
    }

    public static boolean onCooldown(Player player, Role role) {
        if (!isCooldownRole(role) || !inDungeon(player)) return false;
        long[] cooldowns = COOLDOWNS.get(player.getUUID());
        return cooldowns != null && cooldowns[cooldownIndex(role)] > player.level().getGameTime();
    }

    public static long cooldownRemaining(Player player, Role role) {
        if (!isCooldownRole(role)) return 0;
        long[] cooldowns = COOLDOWNS.get(player.getUUID());
        return cooldowns == null ? 0 : Math.max(0, cooldowns[cooldownIndex(role)] - player.level().getGameTime());
    }

    private static boolean inDungeon(Player player) { return player.level().dimension().equals(AvariceDimensions.DUNGEON); }
    private static boolean isCooldownRole(Role role) { return role != Role.GENERAL; }
    private static int cooldownIndex(Role role) { return switch (role) { case PRIMARY -> 0; case SECONDARY -> 1; case TRINKET -> 2; case GENERAL -> throw new IllegalArgumentException("General slots have no cooldown"); }; }

    public enum Role { GENERAL, PRIMARY, SECONDARY, TRINKET }
}
