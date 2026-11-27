package com.revilodev.avarice.datagen;

import com.revilodev.avarice.Avarice;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class AvariceTags {
    private AvariceTags() { }

    public static final class Blocks {
        public static final TagKey<Block> DEVELOPMENT_BLOCKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Avarice.MODID, "development_blocks"));

        private Blocks() { }
    }

    public static final class Items {
        public static final TagKey<Item> DEVELOPMENT_ITEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Avarice.MODID, "development_items"));

        private Items() { }
    }
}
