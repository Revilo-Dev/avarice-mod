package com.revilodev.avarice.client.ui;

import com.revilodev.avarice.Avarice;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Avarice.MODID, value = Dist.CLIENT)
public final class AvariceHudEvents {
    private AvariceHudEvents() { }

    @SubscribeEvent
    public static void registerLayers(RegisterGuiLayersEvent event) {
        event.wrapLayer(VanillaGuiLayers.HOTBAR, original -> (graphics, deltaTracker) -> AvariceHud.renderOrVanilla(original, graphics, deltaTracker));
        event.wrapLayer(VanillaGuiLayers.PLAYER_HEALTH, original -> (graphics, deltaTracker) -> AvariceHud.renderVanillaInCreative(original, graphics, deltaTracker));
        event.wrapLayer(VanillaGuiLayers.ARMOR_LEVEL, original -> (graphics, deltaTracker) -> AvariceHud.renderVanillaInCreative(original, graphics, deltaTracker));
        event.wrapLayer(VanillaGuiLayers.FOOD_LEVEL, original -> (graphics, deltaTracker) -> AvariceHud.renderVanillaInCreative(original, graphics, deltaTracker));
        event.wrapLayer(VanillaGuiLayers.EXPERIENCE_LEVEL, original -> (graphics, deltaTracker) -> AvariceHud.renderVanillaInCreative(original, graphics, deltaTracker));
        event.wrapLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR_BACKGROUND, original -> (graphics, deltaTracker) -> AvariceHud.renderVanillaInCreative(original, graphics, deltaTracker));
        event.wrapLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR, original -> (graphics, deltaTracker) -> AvariceHud.renderVanillaInCreative(original, graphics, deltaTracker));
    }
}
