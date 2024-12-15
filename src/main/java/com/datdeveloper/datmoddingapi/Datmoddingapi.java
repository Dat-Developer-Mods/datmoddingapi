package com.datdeveloper.datmoddingapi;

import com.datdeveloper.datmoddingapi.concurrentTask.ConcurrentHandler;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

/**
 * The entry point for the mod
 */
@Mod(Datmoddingapi.MODID)
public class Datmoddingapi {

    /** The ID of the mod */
    public static final String MODID = "datmoddingapi";

    /** Logger for the mod */
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Entrypoint for mod
     * @param modEventBus The event bus for the mod
     * @param container Container that wraps this mod
     */
    public Datmoddingapi(final IEventBus modEventBus, final ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, DatConfig.SPEC);

        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

    }

    /**
     * Client and server side setup for the mod
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        ConcurrentHandler.initialise();
    }
}
