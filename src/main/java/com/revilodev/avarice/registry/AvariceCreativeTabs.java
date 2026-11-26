package com.revilodev.avarice.registry;

import com.revilodev.avarice.Avarice;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AvariceCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Avarice.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AVARICE = TABS.register(
            "avarice",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.avarice"))
                    .icon(() -> new ItemStack(AvariceItems.DUNGEON_GATEWAY.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(AvariceItems.DUNGEON_GATEWAY.get());
                        output.accept(AvariceItems.DEV_RELIC.get());
                    })
                    .build()
    );

    private AvariceCreativeTabs() { }
}
