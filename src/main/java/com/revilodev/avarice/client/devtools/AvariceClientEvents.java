package com.revilodev.avarice.client.devtools;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.client.ui.AvariceUiScreen;
import com.revilodev.avarice.client.ui.ClientScreenAccess;
import com.revilodev.avarice.core.config.UiConfigStore;
import com.revilodev.avarice.core.reload.AvariceReloads;
import com.revilodev.avarice.core.reload.ReloadManager;
import com.revilodev.avarice.core.reload.ConfigFileWatcher;
import com.revilodev.avarice.devtools.values.Values;
import com.revilodev.avarice.network.OpenAvariceInventoryPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.commands.Commands;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStoppingEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

/** Client-only registration keeps Minecraft client types away from dedicated servers. */
@EventBusSubscriber(modid = Avarice.MODID, value = Dist.CLIENT)
public final class AvariceClientEvents {
    private static ConfigFileWatcher watcher;
    static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Avarice.MODID, "development"));
    static final KeyMapping INSPECT = new KeyMapping("key.avarice.inspect", InputConstants.KEY_MINUS, CATEGORY);
    static final KeyMapping RELOAD_UI = new KeyMapping("key.avarice.reload_ui", InputConstants.KEY_F9, CATEGORY);
    static final KeyMapping INVENTORY = new KeyMapping("key.avarice.inventory", InputConstants.KEY_E, CATEGORY);
    private static boolean inspectKeyDown;
    private AvariceClientEvents() { }
    @SubscribeEvent public static void registerKeys(RegisterKeyMappingsEvent event) { event.registerCategory(CATEGORY); event.register(INSPECT); event.register(RELOAD_UI); event.register(INVENTORY); }
    @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        while (ClientScreenAccess.current() == null && INVENTORY.consumeClick()) {
            if (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode().isCreative()) ClientScreenAccess.open(new InventoryScreen(minecraft.player));
            else net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(new OpenAvariceInventoryPayload());
        }
        boolean inspectDown = InputConstants.isKeyDown(InputConstants.KEY_MINUS);
        if (inspectDown && !inspectKeyDown) toggleInspector();
        inspectKeyDown = inspectDown;
        if (!developmentEnabled()) return;
        while (RELOAD_UI.consumeClick()) applyUiReload(AvariceReloads.reload(AvariceReloads.UI));
    }
    @SubscribeEvent public static void registerCommand(RegisterClientCommandsEvent event) {
        if (!developmentEnabled()) return;
        event.getDispatcher().register(Commands.literal("avarice").then(Commands.literal("dev").then(Commands.literal("ui").executes(context -> { ClientScreenAccess.open(new AvariceUiScreen()); return 1; })).then(Commands.literal("values").executes(context -> { boolean success = Values.reload(); notify(success ? "Values reloaded" : "Values reload failed"); return success ? 1 : 0; }))));
    }
    @SubscribeEvent public static void startWatcher(ClientStartedEvent event) {
        event.getClient().options.keyInventory.setKey(InputConstants.UNKNOWN);
        INSPECT.setKey(InputConstants.getKey("key.keyboard.minus"));
        KeyMapping.resetMapping();
        event.getClient().options.save();
        if (!developmentEnabled() || watcher != null) return;
        try { watcher = new ConfigFileWatcher(UiConfigStore.FILE.getParent(), event.getClient()::execute, AvariceClientEvents::reloadChanged); }
        catch (java.io.IOException exception) { Avarice.LOGGER.warn("Avarice automatic configuration reload is unavailable", exception); }
    }
    @SubscribeEvent public static void stopWatcher(ClientStoppingEvent event) {
        if (watcher == null) return;
        try { watcher.close(); } catch (java.io.IOException exception) { Avarice.LOGGER.warn("Could not close Avarice configuration watcher", exception); } finally { watcher = null; }
    }
    private static void reloadChanged(java.util.Set<java.nio.file.Path> paths) { AvariceReloads.reloadChanged(paths).forEach(result -> { if (result.id().equals(AvariceReloads.UI)) applyUiReload(result); else notify(result.message()); }); }
    private static void applyUiReload(ReloadManager.ReloadDiagnostic result) { if (ClientScreenAccess.current() instanceof AvariceUiScreen screen) screen.applyReload(result); else notify(result.message()); }
    private static void toggleInspector() { ScreenInspectorOverlay.toggle(); if (ClientScreenAccess.current() instanceof AvariceUiScreen screen) screen.toggleInspector(); }
    private static boolean developmentEnabled() { return Boolean.getBoolean("avarice.devtools"); }
    private static void notify(String message) { Avarice.LOGGER.info("[Avarice] {}", message); }
}
