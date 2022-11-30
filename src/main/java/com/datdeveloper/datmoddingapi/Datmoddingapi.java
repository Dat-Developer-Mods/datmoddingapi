package com.datdeveloper.datmoddingapi;

import com.datdeveloper.datmoddingapi.asyncTask.AsyncHandler;
import com.datdeveloper.datmoddingapi.permissions.DatPermissions;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Datmoddingapi.MODID)
public class Datmoddingapi {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "datmoddingapi";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Datmoddingapi() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        final DatConfig config = new DatConfig(builder);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());

        DatPermissions.spongeLoaded = ModList.get().isLoaded("spongeforge");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        AsyncHandler.initialise();
    }
}
