package com.datdeveloper.datmoddingapi.permissions;

import com.datdeveloper.datmoddingapi.DatConfig;
import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;

public class DatPermissions {
    public static boolean spongeLoaded = false;

    public static boolean hasPermission(CommandSource source, PermissionNode<Boolean> permissionNode, PermissionDynamicContext<?>... context) {
        EPermissionSystem api;

        if (DatConfig.getPermissionSystem() == EPermissionSystem.AUTO) api = (spongeLoaded ? EPermissionSystem.SPONGE : EPermissionSystem.FORGE);
        else api = DatConfig.getPermissionSystem();

        if (api == EPermissionSystem.FORGE) {
            if (!(source instanceof ServerPlayer player)) return true;
            return PermissionAPI.getPermission(player, permissionNode, context);
        } else {
            if (!(source instanceof Player)) return true;
            Subject subject = (Subject) source;
            return subject.hasPermission(permissionNode.getNodeName());
        }
    }
}
