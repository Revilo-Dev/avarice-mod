package com.revilodev.avarice.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public final class ClientScreenAccess {
    private ClientScreenAccess() { }
    public static void open(Screen screen) { Minecraft.getInstance().gui.setScreen(screen); }
    public static Screen current() { return Minecraft.getInstance().gui.screen(); }
}
