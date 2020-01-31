package com.demmodders.datmoddingapi.structures;

import net.minecraft.util.math.BlockPos;

public class Location {
    public int dim;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;

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

    public Location(int Dim, BlockPos Blockpos){
        this.dim = Dim;
        this.x = Blockpos.getX();
        this.y = Blockpos.getY();
        this.z = Blockpos.getZ();
        this.pitch = 0;
        this.yaw = 0;
    }
}

