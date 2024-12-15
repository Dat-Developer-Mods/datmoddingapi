package com.datdeveloper.datmoddingapi.delayedEvents;

import com.datdeveloper.datmoddingapi.util.DatChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

/**
 * A Delayed Event to teleport the player to a specific location after a set amount of time
 * Cancels if the player moves
 * @see DelayedEventsHandler
 */
public class DelayedTeleportEvent extends TimeDelayedEvent {
    /**
     * The position the player will teleport to
     */
    protected BlockPos destinationPos;

    /**
     * The level the player will teleport to
     */
    protected ResourceKey<Level> destinationWorld;

    /**
     * The Player being teleported
     */
    protected ServerPlayer player;

    /**
     * The starting position of the player
     * Used to calculate if the event should cancel for the player moving
     */
    protected BlockPos startingPos;

    /**
     * @param destinationPos   The position the player will teleport to
     * @param destinationWorld The level the player will teleport to
     * @param player           The player being teleported
     * @param delay            The delay in seconds before the player teleports
     */
    public DelayedTeleportEvent(final BlockPos destinationPos,
                                final ResourceKey<Level> destinationWorld,
                                final ServerPlayer player,
                                final int delay) {
        super(delay);
        this.destinationPos = destinationPos;
        this.destinationWorld = destinationWorld;
        this.player = player;

        this.startingPos = player.getOnPos();
    }

    @Override
    public void execute() {
        //noinspection DataFlowIssue
        final ServerLevel level = player.getServer().getLevel(destinationWorld);
        if (level == null) {
            player.sendSystemMessage(Component.literal("Failed to find level")
                                              .withStyle(DatChatFormatting.TextColour.ERROR));
            return;
        }

        final Vec3 centre = destinationPos.getCenter();

        player.teleportTo(
                level,
                centre.x,
                centre.y,
                centre.z,
                Set.of(),
                player.getXRot(),
                player.getYRot(),
                false
        );
    }

    @Override
    public boolean shouldRequeue(final boolean hasFinished) {
        if (!hasFinished && startingPos.distToCenterSqr(player.position()) > 1) {
            player.sendSystemMessage(Component.literal(DatChatFormatting.TextColour.ERROR + "Teleport cancelled"));
            return false;
        }

        return super.shouldRequeue(hasFinished);
    }
}
