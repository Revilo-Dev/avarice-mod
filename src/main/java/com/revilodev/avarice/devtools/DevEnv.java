package com.revilodev.avarice.devtools;

import net.neoforged.fml.loading.FMLEnvironment;

public final class DevEnv {
    private DevEnv() { }

    public static boolean enabled() {
        return !FMLEnvironment.isProduction();
    }
}
