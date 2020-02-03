package com.demmodders.datmoddingapi.structures;

public class ChunkLocation {
    public int dim, x,z;

    public ChunkLocation(int Dim, int X, int Z){
        dim = Dim;
        x = X;
        z = Z;
    }

    /**
     * Generates a chunk location from block coordinates
     * @param Dim The dimension the chunk is in
     * @param X The X coord of the block
     * @param Z The Z coord of the block
     * @return The chunk location of the chunk containing the block
     */
    public static ChunkLocation coordsToChunkCoords(int Dim, double X,  double Z){
        return new ChunkLocation(Dim, ((int) X) >> 4, ((int) Z) >> 4);
    }
}
