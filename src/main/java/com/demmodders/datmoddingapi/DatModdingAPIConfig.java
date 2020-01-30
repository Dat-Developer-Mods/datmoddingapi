package com.demmodders.datmoddingapi;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = DatModdingAPI.MODID)
public class DatModdingAPIConfig {
    @Config.Name("Delayed events per tick")
    @Config.Comment("The number of delayed events that should be executed a tick, less is quicker")
    @Config.RangeInt(min = 1)
    public static int delayedEventsPerTick = 1;

    @Mod.EventBusSubscriber(modid = DatModdingAPI.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void configChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(DatModdingAPI.MODID)) {
                ConfigManager.sync(DatModdingAPI.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
