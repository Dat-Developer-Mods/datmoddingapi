package com.datdeveloper.datmoddingapi.delayedEvents;

import com.datdeveloper.datmoddingapi.util.DatChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

/**
 * A Delayed Event to teleport the player to a specific location after a set amount of time
 * Cancels if the player moves
 * @see DelayedEventsHandler
 */
public class DelayedTeleportEvent extends BaseDelayedEvent {
    /**
     * The position the player will teleport to
     */
    public BlockPos destinationPos;

    /**
     * The level the player will teleport to
     */
    public ResourceKey<Level> destinationWorld;

    /**
     * The Player being teleported
     */
    public ServerPlayer player;

    /**
     * The starting position of the player
     * Used to calculate if the event should cancel for the player moving
     */
    public BlockPos startingPos;

    public DelayedTeleportEvent(BlockPos destinationPos, ResourceKey<Level> destinationWorld, ServerPlayer player, int Delay) {
        super(Delay);
        this.destinationPos = destinationPos;
        this.destinationWorld = destinationWorld;
        this.player = player;

        this.startingPos = player.getOnPos();
    }

    @Override
    public void execute() {
        @SuppressWarnings("ConstantConditions")
        ServerLevel level = player.getServer().getLevel(destinationWorld);
        if (level == null) {
            player.sendSystemMessage(Component.literal(ChatFormatting.RED + "Failed to find level"));
            return;
        }

        player.teleportTo(level, (double) destinationPos.getX() + 0.5f, (double) destinationPos.getY() + 0.5f, (double) destinationPos.getZ() + 0.5f, player.getXRot(), player.getYRot());
    }

    @Override
    public boolean shouldRequeue(boolean hasFinished) {
        if (startingPos.distToCenterSqr(player.position()) > 1) {
            player.sendSystemMessage(Component.literal(DatChatFormatting.TextColour.ERROR + "Teleport cancelled"));
            return false;
        }

        return super.shouldRequeue(hasFinished);
    }
}
