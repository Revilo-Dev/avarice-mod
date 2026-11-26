package com.revilodev.avarice.registry;

import com.revilodev.avarice.Avarice;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AvariceDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Avarice.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> DEFINITION_ID = COMPONENTS.registerComponentType("definition_id", builder -> builder.persistent(Codec.STRING));
    private AvariceDataComponents() { }
}
