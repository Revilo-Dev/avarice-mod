package com.revilodev.avarice.currency;

import com.revilodev.avarice.persistence.PlayerDataStore;
import com.revilodev.avarice.network.Network;
import net.minecraft.server.level.ServerPlayer;

public final class Coins {
    private Coins() { }
    public static long balance(ServerPlayer player) { return PlayerDataStore.get(player.level().getServer()).coins(player.getUUID()); }
    public static void set(ServerPlayer player, long amount) {
        PlayerDataStore store = PlayerDataStore.get(player.level().getServer());
        store.setCoins(player.getUUID(), Math.max(0, amount));
        Network.sendPlayerData(player, store.getOrCreate(player.getUUID()));
    }
    public static void add(ServerPlayer player, long amount) { if (amount > 0) set(player, Math.addExact(balance(player), amount)); }
    public static boolean trySpend(ServerPlayer player, long amount) { if (amount < 0 || balance(player) < amount) return false; set(player, balance(player) - amount); return true; }
    public static boolean transfer(ServerPlayer from, ServerPlayer to, long amount) { if (amount <= 0 || !trySpend(from, amount)) return false; add(to, amount); return true; }
}
