package com.demmodders.datmoddingapi;

import com.demmodders.datmoddingapi.util.Permissions;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = DatModdingAPI.MODID, version = DatModdingAPI.VERSION, acceptableRemoteVersions  = "*")
public class DatModdingAPI
{
    public static final String MODID = "datmoddingapi";
    public static final String NAME = "Dat Modding API";
    public static final String VERSION = "1.2.0";
    public static final String MC_VERSION = "[1.12.2]";
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        Permissions.spongeLoaded = Loader.isModLoaded("spongeapi");
    }
}
