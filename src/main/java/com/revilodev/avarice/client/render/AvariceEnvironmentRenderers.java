package com.revilodev.avarice.client.render;

import com.revilodev.avarice.Avarice;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterCustomEnvironmentEffectRendererEvent;

@EventBusSubscriber(modid = Avarice.MODID, value = Dist.CLIENT)
public final class AvariceEnvironmentRenderers {
    private static final Identifier ABYSS_SKYBOX = Identifier.fromNamespaceAndPath(Avarice.MODID, "abyss");

    private AvariceEnvironmentRenderers() { }

    @SubscribeEvent
    public static void registerSkybox(RegisterCustomEnvironmentEffectRendererEvent event) {
        event.registerSkyboxRenderer(ABYSS_SKYBOX, new AbyssSkyboxRenderer());
    }
}
