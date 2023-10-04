package com.datdeveloper.datmoddingapi;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

/**
 * The config for DatModdingAPI
 */
@SuppressWarnings("HideUtilityClassConstructor")
public class DatConfig {
    private static ConfigValue<Integer> delayedEventsPerTick;
    private static ConfigValue<Integer> maxConcurrentThreadCount;
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
    }

    private DatConfig() {
    }

    public static int getDelayedEventsPerTick() {
        return delayedEventsPerTick.get();
    }

    public static int getMaxConcurrentThreadCount() {
        return maxConcurrentThreadCount.get();
    }
}
