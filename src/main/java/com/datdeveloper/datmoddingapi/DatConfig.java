package com.datdeveloper.datmoddingapi;

import com.datdeveloper.datmoddingapi.permissions.EPermissionSystem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class DatConfig {

    private static ConfigValue<Integer> delayedEventsPerTick;
    private static ConfigValue<EPermissionSystem> permissionSystem;
    DatConfig(ForgeConfigSpec.Builder builder) {
        delayedEventsPerTick = builder
                .comment("The amount of delayed events that are processed a tick, less is quicker")
                .defineInRange("DelayedEventsPerTick", 1, 0, Integer.MAX_VALUE);

        permissionSystem = builder
                .comment("Force Dat Mods to use a specific permissions API")
                .defineEnum("ForcePermissionSystem", EPermissionSystem.AUTO);
    }

    public static int getDelayedEventsPerTick() {
        return delayedEventsPerTick.get();
    }

    public static EPermissionSystem getPermissionSystem() {
        return permissionSystem.get();
    }
}
