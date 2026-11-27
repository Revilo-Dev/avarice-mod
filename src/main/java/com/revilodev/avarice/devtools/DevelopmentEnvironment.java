package com.revilodev.avarice.devtools;

import net.neoforged.fml.loading.FMLEnvironment;

public final class DevelopmentEnvironment {
    private DevelopmentEnvironment() { }

    public static boolean enabled() {
        return !FMLEnvironment.isProduction();
    }
}
