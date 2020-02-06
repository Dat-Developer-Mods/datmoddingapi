package com.demmodders.datmoddingapi.structures;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

    /**
     * Convert the Location class to a blockpos class
     * @return The equivalent Block pos
     */
    public BlockPos toBlockPos(){
        return new BlockPos(x,y,z);
    }

    /**
     * Check to see if the given block
     * @param Dim
     * @param theBlockPos
     * @return
     */
    public static boolean checkLocationSafety(int Dim, BlockPos theBlockPos){
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(Dim);
        if (!world.getBlockState(theBlockPos.up()).getMaterial().blocksMovement()) {
            for (int i = 0; i < 3; i++) {
                if(world.getBlockState(theBlockPos.up()).getMaterial() == Material.AIR) return true;
            }
        }
        return false;
    }

    public boolean checkLocationSafety(){
        return checkLocationSafety(dim, this.toBlockPos());
    }
}

