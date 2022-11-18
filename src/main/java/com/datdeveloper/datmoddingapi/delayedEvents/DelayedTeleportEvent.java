package com.datdeveloper.datmoddingapi.delayedEvents;

import com.datdeveloper.datmoddingapi.util.DatTeleporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class DelayedTeleportEvent extends BaseDelayedEvent {
    public BlockPos destinationPos;
    public ResourceKey<Level> destinationWorld;

    public ServerPlayer player;

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
        ServerLevel level = player.getServer().getLevel(destinationWorld);
        if (level == null) {
            player.sendSystemMessage(Component.literal(ChatFormatting.RED + "Failed to find level"));
            return;
        }

        player.teleportTo(level, (double) destinationPos.getX() + 0.5f, (double) destinationPos.getY() + 0.5f, (double) destinationPos.getZ() + 0.5f, player.getXRot(), player.getYRot());
    }
}
