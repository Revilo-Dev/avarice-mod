package com.revilodev.avarice.client.ui;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.registry.AvariceMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Avarice.MODID, value = Dist.CLIENT)
public final class AvariceMenuScreens {
    private AvariceMenuScreens() { }
    @SubscribeEvent public static void register(RegisterMenuScreensEvent event) { event.register(AvariceMenus.INVENTORY.get(), InventoryOverrideScreen::new); }
}
