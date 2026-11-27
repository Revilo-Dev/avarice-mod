package com.revilodev.avarice.datagen.server;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.datagen.AvariceTags;
import com.revilodev.avarice.registry.AvariceBlocks;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

public final class AvariceBlockTagsProvider extends BlockTagsProvider {
    public AvariceBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Avarice.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(AvariceTags.Blocks.DEVELOPMENT_BLOCKS).add(net.minecraft.resources.ResourceKey.create(Registries.BLOCK, AvariceBlocks.DUNGEON_GATEWAY.getId()));
    }
}
