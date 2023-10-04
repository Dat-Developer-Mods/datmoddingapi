package com.datdeveloper.datmoddingapi.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A simple teleporter that just teleports a player to a location in a level
 * Does not produce a portal and does not produce a sound
 */
public class DatTeleporter implements ITeleporter {
    /** The position to teleport the entity to */
    final BlockPos destination;

    /**
     * @param destination The position to teleport the entity to
     */
    public DatTeleporter(final BlockPos destination) {
        this.destination = destination;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(final Entity entity, final ServerLevel destWorld, final Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return new PortalInfo(new Vec3(destination.getX(), destination.getY(), destination.getZ()), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    @Override
    public Entity placeEntity(final Entity entity, final ServerLevel currentWorld, final ServerLevel destWorld, final float yaw, final Function<Boolean, Entity> repositionEntity) {
        return repositionEntity.apply(false);
    }

    @Override
    public boolean playTeleportSound(final ServerPlayer player, final ServerLevel sourceWorld, final ServerLevel destWorld) {
        return false;
    }
}
