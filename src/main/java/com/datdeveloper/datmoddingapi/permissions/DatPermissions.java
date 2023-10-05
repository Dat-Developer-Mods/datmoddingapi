package com.datdeveloper.datmoddingapi.permissions;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A utility class for checking permissions
 */
public class DatPermissions {
    private DatPermissions() {
    }

    /* ========================================= */
    /* Resolvers                                 */
    /* ========================================= */

    /** Player has permission level Owner */
    public static final PermissionNode.PermissionResolver<Boolean> PLAYER_OWNER_RESOLVER = (player, playerUUID, context) -> player != null && player.hasPermissions(Commands.LEVEL_OWNERS);
    /** Player has permission level Admin */
    public static final PermissionNode.PermissionResolver<Boolean> PLAYER_ADMIN_RESOLVER = (player, playerUUID, context) -> player != null && player.hasPermissions(Commands.LEVEL_ADMINS);
    /** Player has permission level Op */
    public static final PermissionNode.PermissionResolver<Boolean> PLAYER_OP_RESOLVER = (player, playerUUID, context) -> player != null && player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
    /** Player has permission level Mod */
    public static final PermissionNode.PermissionResolver<Boolean> PLAYER_MOD_RESOLVER = (player, playerUUID, context) -> player != null && player.hasPermissions(Commands.LEVEL_MODERATORS);
    /** Player has permission level All */
    public static final PermissionNode.PermissionResolver<Boolean> PLAYER_ALL_RESOLVER = (player, playerUUID, context) -> player != null && player.hasPermissions(Commands.LEVEL_ALL);

    /* ========================================= */

    /** Integer literal 0 */
    public static final PermissionNode.PermissionResolver<Integer> INTEGER_ZERO_RESOLVER = (player, playerUUID, context) -> 0;

    /**
     * Build an integer resolver that returns a constant number
     * @param literal The constant for the resolver
     * @return A permission resolver that returns the given constant
     */
    public static PermissionNode.PermissionResolver<Integer> getIntegerLiteralResolver(final int literal) {
        return (player, playerUUID, context) -> literal;
    }

    /* ========================================= */

    /** Empty String literal */
    public static final PermissionNode.PermissionResolver<String> STRING_EMPTY_RESOLVER = (player, playerUUID, context) -> "";

    /**
     * Build a string resolver that returns a constant string
     * @param literal The constant for the resolver
     * @return A permission resolver that returns the given constant
     */
    public static PermissionNode.PermissionResolver<String> getStringLiteralResolver(final String literal) {
        return (player, playerUUID, context) -> literal;
    }

    /* ========================================= */

    /** Empty Component literal */
    public static final PermissionNode.PermissionResolver<Component> COMPONENT_EMPTY_RESOLVER = (player, playerUUID, context) -> Component.empty();

    /**
     * Build a component resolver that returns a literal component
     * @param literal The component for the resolver to return
     * @return A permission resolver that returns the given component
     */
    public static PermissionNode.PermissionResolver<Component> getComponentLiteralResolver(final Component literal) {
        return (player, playerUUID, context) -> literal;
    }

    /**
     * Build a component resolver that returns a literal component
     * @param literal The constant for the resolver
     * @return A permission resolver that returns the given string as a component
     */
    public static PermissionNode.PermissionResolver<Component> getComponentLiteralResolver(final String literal) {
        return (player, playerUUID, context) -> Component.literal(literal);
    }

    /* ========================================= */
    /* Permission Node Builders                  */
    /* ========================================= */

    /**
     * A shortcut to build a boolean node
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param resolver The default resolver to use for the node
     * @return A new boolean permission node
     */
    public static PermissionNode<Boolean> booleanNodeBuilder(final String modId, final String node, final PermissionNode.PermissionResolver<Boolean> resolver) {
        return new PermissionNode<>(modId, node, PermissionTypes.BOOLEAN, resolver);
    }

    /**
     * Create a boolean node that defaults to being available for all players
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return A boolean node for players
     */
    public static PermissionNode<Boolean> createBasicNode(final String modId, final String node) {
        return booleanNodeBuilder(modId, node, PLAYER_ALL_RESOLVER);
    }

    /**
     * Create a boolean node that defaults to being available for moderators
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return A boolean node for moderators
     */
    public static PermissionNode<Boolean> createModNode(final String modId, final String node) {
        return booleanNodeBuilder(modId, node, PLAYER_MOD_RESOLVER);
    }

    /**
     * Create a boolean node that defaults to being available for ops
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return A boolean node for ops
     */
    public static PermissionNode<Boolean> createOpNode(final String modId, final String node) {
        return booleanNodeBuilder(modId, node, PLAYER_OP_RESOLVER);
    }

    /**
     * Create a boolean node that defaults to being available for admins
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return A boolean node for admins
     */
    public static PermissionNode<Boolean> createAdminNode(final String modId, final String node) {
        return booleanNodeBuilder(modId, node, PLAYER_ADMIN_RESOLVER);
    }

    /**
     * Create a boolean node that defaults to being available for owners
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return A boolean node for owners
     */
    public static PermissionNode<Boolean> createOwnerNode(final String modId, final String node) {
        return booleanNodeBuilder(modId, node, PLAYER_OWNER_RESOLVER);
    }

    /* ========================================= */

    /**
     * A shortcut to build an integer node
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param resolver The default resolver to use for the node
     * @return A new integer permission node
     */
    public static PermissionNode<Integer> integerNodeBuilder(final String modId, final String node, final PermissionNode.PermissionResolver<Integer> resolver) {
        return new PermissionNode<>(modId, node, PermissionTypes.INTEGER, resolver);
    }

    /**
     * Create an integer node that defaults to 0
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return An integer node that defaults to 0
     */
    public static PermissionNode<Integer> createZeroNode(final String modId, final String node) {
        return integerNodeBuilder(modId, node, INTEGER_ZERO_RESOLVER);
    }

    /**
     * Create an integer node that defaults to the given value
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param literal the default value of the node
     * @return An integer node that defaults to the given value
     */
    public static PermissionNode<Integer> createIntegerLiteralNode(final String modId, final String node, final int literal) {
        return integerNodeBuilder(modId, node, getIntegerLiteralResolver(literal));
    }

    /* ========================================= */

    /**
     * A shortcut to build a string node
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param resolver The default resolver to use for the node
     * @return A new integer permission node
     */
    public static PermissionNode<String> stringNodeBuilder(final String modId, final String node, final PermissionNode.PermissionResolver<String> resolver) {
        return new PermissionNode<>(modId, node, PermissionTypes.STRING, resolver);
    }

    /**
     * Create a string node that defaults to empty
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return A string node that defaults to empty
     */
    public static PermissionNode<String> createEmptyStringNode(final String modId, final String node) {
        return stringNodeBuilder(modId, node, STRING_EMPTY_RESOLVER);
    }

    /**
     * Create a string node that defaults to the given value
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param literal the default value of the node
     * @return A component node that defaults to the given value
     */
    public static PermissionNode<String> createStringLiteralNode(final String modId, final String node, final String literal) {
        return stringNodeBuilder(modId, node, getStringLiteralResolver(literal));
    }

    /* ========================================= */

    /**
     * A shortcut to build a component node
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param resolver The default resolver to use for the node
     * @return A new integer permission node
     */
    public static PermissionNode<Component> componentNodeBuilder(final String modId, final String node, final PermissionNode.PermissionResolver<Component> resolver) {
        return new PermissionNode<>(modId, node, PermissionTypes.COMPONENT, resolver);
    }

    /**
     * Create a component node that defaults to empty
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @return A string node that defaults to empty
     */
    public static PermissionNode<Component> createEmptyComponentNode(final String modId, final String node) {
        return componentNodeBuilder(modId, node, COMPONENT_EMPTY_RESOLVER);
    }

    /**
     * Create a component node that defaults to the given value
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param literal the default value of the node
     * @return A component node that defaults to the given value
     */
    public static PermissionNode<Component> createComponentLiteralNode(final String modId, final String node, final Component literal) {
        return componentNodeBuilder(modId, node, getComponentLiteralResolver(literal));
    }

    /**
     * Create a component node that defaults to the given value
     * @param modId The modId of the mod the node belongs to
     * @param node The identifier of the node
     * @param literal the default value of the node
     * @return A component node that defaults to the given value
     */
    public static PermissionNode<Component> createComponentLiteralNode(final String modId, final String node, final String literal) {
        return componentNodeBuilder(modId, node, getComponentLiteralResolver(literal));
    }

    /* ========================================= */
    /* Permission Checks                         */
    /* ========================================= */

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
                .anyMatch(Boolean::booleanValue);
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
                .allMatch(Boolean::booleanValue);
    }

    private static Stream<Boolean> hasPermissions(final CommandSource source, final Stream<PermissionNode<Boolean>> permissionNodes) {
        if (source instanceof final ServerPlayer player) {
            return permissionNodes.map(node -> PermissionAPI.getPermission(player, node));
        }
        return permissionNodes.map(node -> true);
    }

    /* ========================================= */
    /* Permission Predicates                     */
    /* ========================================= */

    /**
     * Generate a predicate that checks the CommandSourceStack has the given permission
     * @param node The permission node to check
     * @return A predicate that checks for the given permission node
     */
    public static Predicate<CommandSourceStack> hasPermission(final PermissionNode<Boolean> node) {
        return (source) -> hasPermission(source.source, node);
    }

    /**
     * Generate a predicate that checks the CommandSourceStack has any of the given permissions
     * @param nodes The permission nodes to check
     * @return A predicate that checks for any of the given permission nodes
     */
    @SafeVarargs
    public static Predicate<CommandSourceStack> hasAnyPermissions(final PermissionNode<Boolean>... nodes) {
        return (source) -> hasAnyPermissions(source.source, nodes);
    }

    /**
     * Generate a predicate that checks the CommandSourceStack has all the given permissions
     * @param nodes The permission nodes to check
     * @return A predicate that checks for all the given permission nodes
     */
    @SafeVarargs
    public static Predicate<CommandSourceStack> hasAllPermissions(final PermissionNode<Boolean>... nodes) {
        return (source) -> hasAllPermissions(source.source, nodes);
    }
}
