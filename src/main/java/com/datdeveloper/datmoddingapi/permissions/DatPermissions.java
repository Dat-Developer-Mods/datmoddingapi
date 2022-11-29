package com.datdeveloper.datmoddingapi.permissions;

import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import org.spongepowered.api.service.permission.Subject;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * A permissionAPI agnostic system that allows for testing permission nodes without knowledge of the underlying PermissionAPI
 * Currently handles Forge's permission api and sponges permission api
 */
public class DatPermissions {
    /**
     * Is sponge loaded?
     */
    public static boolean spongeLoaded = false;

    private static EPermissionSystem getPermissionSystem() {
        // Sponge is not supported currently
//        if (DatConfig.getPermissionSystem() == EPermissionSystem.AUTO) return (spongeLoaded ? EPermissionSystem.SPONGE : EPermissionSystem.FORGE);
//        else return DatConfig.getPermissionSystem();
        return EPermissionSystem.FORGE;
    }

    /**
     * Checks if the given CommandSource has the given permission
     * @param source The CommandSource being tested
     * @param permissionNode The permission node to test
     * @return true if the CommandSource has permission
     */
    public static boolean hasPermission(final CommandSource source, final PermissionNode<Boolean> permissionNode) {
        return hasAnyPermissions(source, permissionNode);
    }

    /**
     * Checks if the given CommandSource has any of the given permission
     * @param source The CommandSource being tested
     * @param permissionNodes The permission nodes to test
     * @return true if the CommandSource has any of the given permissions
     */
    @SafeVarargs
    public static boolean hasAnyPermissions(final CommandSource source, final PermissionNode<Boolean>... permissionNodes) {
        return hasPermissions(source, Arrays.stream(permissionNodes).toList())
                .anyMatch(node -> node);
    }

    /**
     * Checks if the given CommandSource has all the given permission
     * @param source The CommandSource being tested
     * @param permissionNodes The permission nodes to test
     * @return true if the CommandSource has all the given permissions
     */
    @SafeVarargs
    public static boolean hasAllPermissions(final CommandSource source, final PermissionNode<Boolean>... permissionNodes) {
        return hasPermissions(source, Arrays.stream(permissionNodes).toList())
                .allMatch(node -> node);
    }

    private static Stream<Boolean> hasPermissions(final CommandSource source, final Collection<PermissionNode<Boolean>> permissionNodes) {
        final EPermissionSystem api = getPermissionSystem();

        if (api == EPermissionSystem.FORGE) {
            final ServerPlayer player;
            if (source instanceof ServerPlayer) player = (ServerPlayer) source;
            else player = null;

            return permissionNodes.stream().map(node -> player == null || PermissionAPI.getPermission(player, node));
        } else {
            final Subject player;
            if (source instanceof Subject) player = (Subject) source;
            else player = null;
            return permissionNodes.stream().map(node -> player == null || player.hasPermission(node.getNodeName()));
        }
    }
}
