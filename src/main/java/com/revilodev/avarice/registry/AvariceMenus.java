package com.revilodev.avarice.registry;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.inventory.AvariceInventoryMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.flag.FeatureFlags;

public final class AvariceMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Avarice.MODID);
    public static final DeferredHolder<MenuType<?>, MenuType<AvariceInventoryMenu>> INVENTORY = MENUS.register("inventory", () -> new MenuType<>(AvariceInventoryMenu::new, FeatureFlags.DEFAULT_FLAGS));

    private AvariceMenus() { }
}
