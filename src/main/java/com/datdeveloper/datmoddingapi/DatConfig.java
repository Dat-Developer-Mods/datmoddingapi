package com.datdeveloper.datmoddingapi;

import com.datdeveloper.datmoddingapi.permissions.EPermissionSystem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class DatConfig {

    private static ConfigValue<Integer> delayedEventsPerTick;
    private static ConfigValue<Integer> maxConcurrentThreadCount;
    private static ConfigValue<EPermissionSystem> permissionSystem;
    DatConfig(final ForgeConfigSpec.Builder builder) {
        delayedEventsPerTick = builder
                .comment("The amount of delayed events that are processed a tick, less is quicker")
                .defineInRange("DelayedEventsPerTick", 1, 0, Integer.MAX_VALUE);

        maxConcurrentThreadCount = builder
                .worldRestart()
                .comment("The maximum number of threads to use for executing concurrent tasks")
                .comment("More threads means more concurrent tasks can be executed at the same time")
                .comment("Too many threads can hog resources, and lots of threads only helps when there are many concurrent tasks")
                .comment("You probably don't want to change this if you don't know it is")
                .defineInRange("MaxAsyncThreadCount", 2, 1, Integer.MAX_VALUE);

        permissionSystem = builder
                .comment("Force Dat Mods to use a specific permissions API")
                .defineEnum("ForcePermissionSystem", EPermissionSystem.AUTO);

    }

    public static int getDelayedEventsPerTick() {
        return delayedEventsPerTick.get();
    }

    public static int getMaxConcurrentThreadCount() {
        return maxConcurrentThreadCount.get();
    }

    public static EPermissionSystem getPermissionSystem() {
        return permissionSystem.get();
    }
}
