package com.demmodders.datmoddingapi.structures;

import net.minecraft.util.math.BlockPos;

public class Location {
    public int dim;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;

    // Empty constructor for GSON
    public Location(){

    }

    public Location(int Dim, double x, double y, double z, float pitch, float yaw){
        this.dim = Dim;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Converts a given block position to the Location Standard
     * @param Dim The dimension the block is in
     * @param Blockpos The BlockPos
     */
    public Location(int Dim, BlockPos Blockpos){
        this.dim = Dim;
        this.x = Blockpos.getX();
        this.y = Blockpos.getY();
        this.z = Blockpos.getZ();
        this.pitch = 0;
        this.yaw = 0;
    }
}

