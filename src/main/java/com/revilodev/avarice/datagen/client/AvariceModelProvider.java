package com.revilodev.avarice.datagen.client;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.registry.AvariceBlocks;
import com.revilodev.avarice.registry.AvariceItems;
import java.util.stream.Stream;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class AvariceModelProvider extends ModelProvider {
    private static final Material GATEWAY_TEXTURE = new Material(Identifier.withDefaultNamespace("block/crying_obsidian"));

    public AvariceModelProvider(PackOutput output) {
        super(output, Avarice.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialBlock(
                AvariceBlocks.DUNGEON_GATEWAY.get(),
                TexturedModel.CUBE.updateTexture(mapping -> mapping.put(TextureSlot.ALL, GATEWAY_TEXTURE))
        );
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.of(AvariceBlocks.DUNGEON_GATEWAY);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.of(AvariceItems.DUNGEON_GATEWAY);
    }
}
