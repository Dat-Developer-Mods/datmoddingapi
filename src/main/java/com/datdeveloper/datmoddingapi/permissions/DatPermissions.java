package com.datdeveloper.datmoddingapi.permissions;

import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * A utility class for checking permissions
 */
public class DatPermissions {
    private DatPermissions() {
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
        return hasPermissions(source, Arrays.stream(permissionNodes))
                .anyMatch(permResult -> permResult);
    }

    /**
     * Checks if the given CommandSource has all the given permission
     * @param source The CommandSource being tested
     * @param permissionNodes The permission nodes to test
     * @return true if the CommandSource has all the given permissions
     */
    @SafeVarargs
    public static boolean hasAllPermissions(final CommandSource source, final PermissionNode<Boolean>... permissionNodes) {
        return hasPermissions(source, Arrays.stream(permissionNodes))
                .allMatch(permResult -> permResult);
    }

    private static Stream<Boolean> hasPermissions(final CommandSource source, final Stream<PermissionNode<Boolean>> permissionNodes) {
        if (source instanceof final ServerPlayer player) {
            return permissionNodes.map(node -> PermissionAPI.getPermission(player, node));
        }
        return permissionNodes.map(node -> true);
    }
}
