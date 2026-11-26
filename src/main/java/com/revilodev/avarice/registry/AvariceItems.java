package com.revilodev.avarice.registry;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.devtools.items.ReloadableDefinitionItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AvariceItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Avarice.MODID);
    public static final DeferredHolder<Item, ReloadableDefinitionItem> DEV_RELIC = ITEMS.register("dev_relic", id -> new ReloadableDefinitionItem(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<BlockItem> DUNGEON_GATEWAY = ITEMS.registerSimpleBlockItem(AvariceBlocks.DUNGEON_GATEWAY);
    private AvariceItems() { }
}
