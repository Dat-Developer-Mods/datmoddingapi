package com.demmodders.datmoddingapi.delayedexecution.delayedevents;

import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.datmoddingapi.util.DatTeleporter;
import com.demmodders.datmoddingapi.util.DemConstants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class DelayedTeleportEvent extends BaseDelayedEvent {
    public boolean cancelled = false;
    public Location destination;
    public EntityPlayerMP player;

    // The player's Starting coordinates
    public double startX;
    public double startZ;

    public DelayedTeleportEvent(Location Destination, EntityPlayerMP Player, int Delay) {
        super(Delay);
        destination = Destination;
        player = Player;
        startX = player.posX;
        startZ = player.posZ;
    }

    @Override
    public void execute(){
        // Handle different dimensions
        if (destination.dim != player.dimension){
            player.changeDimension(destination.dim, new DatTeleporter(destination));
        } else {
            player.connection.setPlayerLocation(destination.x, destination.y, destination.z, destination.yaw, destination.pitch);
        }
    }

    @Override
    public boolean canExecute(){
        // Ensure the player is still on the server, and hasn't moved more than a block away from their start point
        if (player.isDead || player.hasDisconnected() || Math.abs(Math.pow(player.posX - startX, 2) + Math.pow(player.posZ - startZ, 2)) > 1){
            cancelled = true;
            if(!player.hasDisconnected()) player.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "Teleport Cancelled"));
        }
        return super.canExecute() && !cancelled;
    }

    @Override
    public boolean shouldRequeue(boolean hasFinished) {
        return super.shouldRequeue(hasFinished) && !cancelled;
    }
}
