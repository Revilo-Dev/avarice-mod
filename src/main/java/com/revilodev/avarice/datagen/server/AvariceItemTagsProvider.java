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
                net.minecraft.resources.ResourceKey.create(Registries.ITEM, AvariceItems.DUNGEON_GATEWAY.getId())
        );
    }
}
