package com.datdeveloper.datmoddingapi.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class to simplify sending notifications to the player
 */
public class NotificationHelper {
    private NotificationHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Display a notification for the given player in the middle of their screen
     * @param player The player to send the notification to
     * @param title The main title of the notification
     * @param subTitle The subtitle of the notification
     * @param fadeIn The number of ticks to spend fading the message in
     * @param stay The number of ticks for the message to spend on the screen
     * @param fadeOut The number of ticks to spend fading out
     */
    public static void titleNotification(@NotNull final ServerPlayer player,
                                  @NotNull final Component title,
                                  @Nullable final Component subTitle,
                                  final int fadeIn,
                                  final int stay,
                                  final int fadeOut) {

        player.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));

        if (subTitle != null) player.connection.send(new ClientboundSetSubtitleTextPacket(subTitle));
        player.connection.send(new ClientboundSetTitleTextPacket(title));
    }

    /**
     * Display a notification for the given player in the middle of their screen
     * <br>
     * Uses 10, 40, 10 for fadeIn, stay, and fadeOut respectively
     * @param player The player to send the notification to
     * @param title The main title of the notification
     * @param subTitle The subtitle of the notification
     */
    public static void titleNotification(final ServerPlayer player,
                                  final Component title,
                                  @Nullable final Component subTitle) {
        titleNotification(player, title, subTitle, 10, 40, 10);
    }

    /**
     * Display a notification for the given player just above their hotbar
     * @param player The player to send the notification to
     * @param message The content of the notification
     */
    public static void hotbarNotification(final ServerPlayer player,
                                   final Component message) {
        player.connection.send(new ClientboundSetActionBarTextPacket(message));
    }

    /**
     * Display a notification for the given player in their chat
     * <br>
     * This is just a wrapper around {@link ServerPlayer#sendSystemMessage(Component)}
     * @see ServerPlayer#sendSystemMessage(Component)
     * @param player The player to send the notification to
     * @param message The content of the notification
     */
    public static void chatNotification(final ServerPlayer player,
                                 final Component message) {
        player.sendSystemMessage(message);
    }
}
