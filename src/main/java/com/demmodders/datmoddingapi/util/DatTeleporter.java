package com.demmodders.datmoddingapi.util;

import com.demmodders.datmoddingapi.structures.Location;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class DatTeleporter implements ITeleporter {
    Location location;

    public DatTeleporter(Location Destination){
        location = Destination;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        if (entity instanceof EntityPlayerMP){
            // We only need to update their position
            ((EntityPlayerMP)entity).connection.setPlayerLocation(location.x, location.y, location.z, location.yaw, location.pitch);
        }
    }
}
