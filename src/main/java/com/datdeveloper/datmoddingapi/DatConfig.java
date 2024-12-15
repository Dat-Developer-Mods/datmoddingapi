package com.datdeveloper.datmoddingapi;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The config for DatModdingAPI
 */
public class DatConfig {
    private DatConfig() {

    }

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue DELAYED_EVENTS_PER_TICK = BUILDER
            .comment("The amount of delayed events that are processed a tick, less is quicker")
            .defineInRange("DelayedEventsPerTick", 1, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<Integer> MAX_CONCURRENT_THREAD_COUNT = BUILDER
            .worldRestart()
            .comment("The maximum number of threads to use for executing concurrent tasks")
            .comment("More threads means more concurrent tasks can be executed at the same time")
            .comment("Too many threads can hog resources, and lots of threads only helps when there are many concurrent"
                             + " tasks")
            .comment("You probably don't want to change this if you don't know it is")
            .defineInRange("MaxAsyncThreadCount", 2, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int getDelayedEventsPerTick() {
        return DELAYED_EVENTS_PER_TICK.get();
    }

    public static int getMaxConcurrentThreadCount() {
        return MAX_CONCURRENT_THREAD_COUNT.get();
    }
}
