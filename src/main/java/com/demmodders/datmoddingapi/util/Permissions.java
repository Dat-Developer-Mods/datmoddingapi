package com.demmodders.datmoddingapi.util;

import com.demmodders.datmoddingapi.DatModdingAPIConfig;
import com.demmodders.datmoddingapi.Enums.PermissionSystem;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.server.permission.PermissionAPI;

public class Permissions {
    public static boolean spongeLoaded = false;

    public static boolean checkPermission(ICommandSender player, String permissionNode, int permissionLevel) {
        PermissionSystem api;
        if (DatModdingAPIConfig.forcedPermissionSystem == PermissionSystem.AUTO) {
            api = (spongeLoaded ? PermissionSystem.SPONGE : PermissionSystem.FORGE);
        } else {
            api = DatModdingAPIConfig.forcedPermissionSystem;
        }

        if (api == PermissionSystem.FORGE) {
            if (player instanceof EntityPlayerMP) {
                return PermissionAPI.hasPermission((EntityPlayerMP) player, permissionNode);
            } else {
                return true;
            }
        } else {
            return player.canUseCommand(permissionLevel, permissionNode);
        }
    }
}
