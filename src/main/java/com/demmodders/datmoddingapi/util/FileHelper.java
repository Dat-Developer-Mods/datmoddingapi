package com.demmodders.datmoddingapi.util;

import com.demmodders.datmoddingapi.DatModdingAPI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class FileHelper {
    static final Logger LOGGER = LogManager.getLogger(DatModdingAPI.MODID);

    /**
     * Safely get the config subdirectory for the given modID
     * @param modID The ID of the mod who's config subdirectory is being fetched
     * @return A file open as a directory
     */
    public static File getConfigSubDir(String modID){
        File dir;
        if (FMLCommonHandler.instance().getMinecraftServerInstance() != null && FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer())
        {
            dir = new File("./config/", modID);
        } else {
            dir = new File(Minecraft.getMinecraft().mcDataDir, modID);
        }
        if(!dir.exists()){
            boolean success = dir.mkdirs();
            assert success : "Unable to create faction Directory";
        }
        return dir;
    }

    /**
     * Safely opens the given file, creating the required directories and file if needed
     * @param theFile The file to open
     * @return The opened file
     */
    public static File openFile(File theFile){
        try {
            boolean success = true;
            if (!theFile.getParentFile().exists()) success = theFile.getParentFile().mkdirs();
            if (!theFile.exists()) success = theFile.createNewFile();
            if (!success) throw new IOException();
            return theFile;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(DatModdingAPI.MODID + " Unable to create file " + theFile.getPath() + "\n This faction data will not persist past a server restart");
        }
        return null;
    }

    /**
     * Gets the base name of the file (the name without the extension)
     * @param FileName The name of the file with the extension
     * @return The name of the file without the extension
     */
    public static String getBaseName(String FileName){
        int index = FileName.lastIndexOf('.');
        return (index != -1 ? FileName.substring(0, index) : FileName);
    }
}
