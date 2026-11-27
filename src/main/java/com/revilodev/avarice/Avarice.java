package com.revilodev.avarice;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

import com.revilodev.avarice.core.config.UiConfigStore;
import com.revilodev.avarice.core.reload.AvariceReloads;
import com.revilodev.avarice.devtools.values.Values;
import com.revilodev.avarice.devtools.items.ItemDefs;
import com.revilodev.avarice.registry.AvariceDataComponents;
import com.revilodev.avarice.registry.AvariceBlocks;
import com.revilodev.avarice.registry.AvariceCreativeTabs;
import com.revilodev.avarice.registry.AvariceItems;
import com.revilodev.avarice.registry.AvariceMenus;
import com.revilodev.avarice.datagen.AvariceDataGenerators;
import com.revilodev.avarice.network.Network;

@Mod(Avarice.MODID)
public class Avarice {
    public static final String MODID = "avarice";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Avarice(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(AvariceDataGenerators::gatherData);
        modEventBus.addListener(Network::register);
        AvariceDataComponents.COMPONENTS.register(modEventBus);
        AvariceBlocks.BLOCKS.register(modEventBus);
        AvariceItems.ITEMS.register(modEventBus);
        AvariceMenus.MENUS.register(modEventBus);
        AvariceCreativeTabs.TABS.register(modEventBus);
        UiConfigStore.initialize();
        Values.initialize();
        ItemDefs.initialize();
        AvariceReloads.initialize();
    }
}
