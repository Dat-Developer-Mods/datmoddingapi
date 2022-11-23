package com.datdeveloper.datmoddingapi.permissions;

import com.datdeveloper.datmoddingapi.DatConfig;
import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;

/**
 * A permissionAPI agnostic system that allows for testing permission nodes without knowledge of the underlying PermissionAPI
 * Currently handles Forge's permission api and sponges permission api
 */
public class DatPermissions {
    /**
     * Is sponge loaded?
     */
    public static boolean spongeLoaded = false;

    /**
     * Checks if the given CommandSource has the given permission
     * @param source The CommandSource being tested
     * @param permissionNode The permission node to test
     * @param context Extra context to the permission
     * @return true if the CommandSource has permission
     */
    public static boolean hasPermission(final CommandSource source, final PermissionNode<Boolean> permissionNode, final PermissionDynamicContext<?>... context) {
        final EPermissionSystem api;

        if (DatConfig.getPermissionSystem() == EPermissionSystem.AUTO) api = (spongeLoaded ? EPermissionSystem.SPONGE : EPermissionSystem.FORGE);
        else api = DatConfig.getPermissionSystem();

        if (api == EPermissionSystem.FORGE) {
            if (!(source instanceof ServerPlayer player)) return true;
            return PermissionAPI.getPermission(player, permissionNode, context);
        } else {
            if (!(source instanceof Player)) return true;
            final Subject subject = (Subject) source;
            return subject.hasPermission(permissionNode.getNodeName());
        }
    }
}
