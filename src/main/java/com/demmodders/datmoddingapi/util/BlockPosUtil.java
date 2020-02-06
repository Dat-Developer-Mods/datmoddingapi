package com.demmodders.datmoddingapi.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockPosUtil {
    public static BlockPos findSafeZ(int Dim, BlockPos blockPos, int maxDistance){
        BlockPos test = new BlockPos(blockPos);
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(Dim);
        if (world.getBlockState(blockPos).getMaterial().blocksMovement()) {
            test = test.up();
            while (test.getY() < blockPos.getY() + maxDistance){
                if (!world.getBlockState(test).getMaterial().blocksMovement()){
                    if (!world.getBlockState(test.up()).getMaterial().blocksMovement()){
                        return test;
                    } else {
                        test = test.up();
                    }
                    test = test.up();
                }
            }
        } else if (!world.getBlockState(blockPos.down()).getMaterial().blocksMovement()){
            test = test.down();
            while (test.getY() < blockPos.getY() + maxDistance){
                if (!world.getBlockState(test).getMaterial().blocksMovement()){
                    if (!world.getBlockState(test.down()).getMaterial().blocksMovement()){
                        return test;
                    } else {
                        test = test.down();
                    }
                    test = test.down();
                }
            }
        }
        return null;
    }
}
