package com.revilodev.avarice.datagen.server;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.datagen.AvariceTags;
import com.revilodev.avarice.registry.AvariceItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

public final class AvariceItemTagsProvider extends ItemTagsProvider {
    public AvariceItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Avarice.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(AvariceTags.Items.DEVELOPMENT_ITEMS).add(
                net.minecraft.resources.ResourceKey.create(Registries.ITEM, AvariceItems.DEV_RELIC.getId()),
                net.minecraft.resources.ResourceKey.create(Registries.ITEM, AvariceItems.DUNGEON_GATEWAY.getId()),
                net.minecraft.resources.ResourceKey.create(Registries.ITEM, AvariceItems.COIN_PILE.getId())
        );
        tag(com.revilodev.avarice.inventory.AvariceInventorySlots.WEAPONS).add(
                key(AvariceItems.MANA_STEEL_SWORD), key(AvariceItems.ELIXRITE_SWORD), key(AvariceItems.ASTRITE_SWORD), key(AvariceItems.LUNARIUM_SWORD), key(AvariceItems.IGNITE_SWORD), key(AvariceItems.IRIDIUM_SWORD), key(AvariceItems.MYTHRIL_SWORD), key(AvariceItems.ARCANIUM_SWORD), key(AvariceItems.PRISMATIC_STEEL_SWORD),
                key(AvariceItems.MANA_STEEL_PAXEL), key(AvariceItems.ELIXRITE_PAXEL), key(AvariceItems.ASTRITE_PAXEL), key(AvariceItems.LUNARIUM_PAXEL), key(AvariceItems.IGNITE_PAXEL), key(AvariceItems.IRIDIUM_PAXEL), key(AvariceItems.MYTHRIL_PAXEL), key(AvariceItems.ARCANIUM_PAXEL), key(AvariceItems.PRISMATIC_STEEL_PAXEL)
        );
        tag(com.revilodev.avarice.inventory.AvariceInventorySlots.MAGNETS).add(
                key(AvariceItems.MANA_STEEL_MAGNET), key(AvariceItems.DUNGEON_MAGNET), key(AvariceItems.ELIXRITE_MAGNET), key(AvariceItems.ASTRITE_MAGNET), key(AvariceItems.LUNARIUM_MAGNET), key(AvariceItems.IGNITE_MAGNET), key(AvariceItems.IRIDIUM_MAGNET), key(AvariceItems.MYTHRIL_MAGNET), key(AvariceItems.ARCANIUM_MAGNET), key(AvariceItems.PRISMATIC_STEEL_MAGNET)
        );
    }

    private static net.minecraft.resources.ResourceKey<Item> key(net.neoforged.neoforge.registries.DeferredItem<? extends Item> item) {
        return net.minecraft.resources.ResourceKey.create(Registries.ITEM, item.getId());
    }
}
