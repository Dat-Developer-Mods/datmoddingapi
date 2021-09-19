package com.demmodders.datmoddingapi.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockPosUtil {
    /**
     * Finds a safe location along the z axis
     * Warning, thuis function isn't very good or well written, I'd avoid it
     * @param Dim The dimension the position is in
     * @param blockPos The starting position
     * @param maxDistance The maximum distance to search
     * @return A safe blockpos, or null
     */
    public static BlockPos findSafeZ(int Dim, BlockPos blockPos, int maxDistance){
        BlockPos test = new BlockPos(blockPos);
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(Dim);
        if (world.getBlockState(blockPos).getMaterial().blocksMovement()) {
            while (test.getY() < blockPos.getY() + maxDistance){
                test = test.up();
                if (!world.getBlockState(test).getMaterial().blocksMovement()){
                    if (!world.getBlockState(test.up()).getMaterial().blocksMovement()){
                        return test;
                    } else {
                        test = test.up();
                    }
                }
            }
        } else if (!world.getBlockState(blockPos.down()).getMaterial().blocksMovement() && !world.getBlockState(blockPos.down()).getMaterial().isLiquid()){
            while (test.getY() > blockPos.getY() - maxDistance){
                test = test.down();
                if (test.getY() < 0) {
                    return null;
                } else if (!world.getBlockState(test).getMaterial().blocksMovement()){
                    if (!world.getBlockState(test.down()).getMaterial().blocksMovement()){
                        return test.up();
                    } else {
                        test = test.down();
                    }
                }
            }
        } else {
            return blockPos;
        }
        return null;
    }
}
