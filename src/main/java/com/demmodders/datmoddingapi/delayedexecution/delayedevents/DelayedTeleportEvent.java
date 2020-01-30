package com.demmodders.datmoddingapi.delayedexecution.delayedevents;

import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.datmoddingapi.util.DatTeleporter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DelayedTeleportEvent extends BaseDelayedEvent {
    static final TextFormatting textCancelledColour = TextFormatting.RED;
    boolean cancelled = false;
    Location destination;
    EntityPlayerMP player;
    double startX;
    double startZ;

    public DelayedTeleportEvent(Location Destination, EntityPlayerMP Player, int Delay) {
        super(Delay);
        destination = Destination;
        player = Player;
        startX = player.posX;
        startZ = player.posZ;
    }

    @Override
    public void execute(){
        if (destination.dim != player.dimension){
            player.changeDimension(destination.dim, new DatTeleporter(destination));
        } else {
            player.connection.setPlayerLocation(destination.x, destination.y, destination.z, destination.yaw, destination.pitch);
        }
    }

    @Override
    public boolean canExecute(){
        if (player.isDead || player.hasDisconnected() || Math.pow(player.posX, 2) + Math.pow(player.posZ, 2) > 1){
            cancelled = true;
            if(!player.hasDisconnected()) player.sendMessage(new TextComponentString(textCancelledColour + "Teleport Cancelled"));
        }
        return super.canExecute() && !cancelled;
    }

    @Override
    public boolean shouldRequeue(boolean hasFinished) {
        return super.shouldRequeue(hasFinished) && !cancelled;
    }
}
