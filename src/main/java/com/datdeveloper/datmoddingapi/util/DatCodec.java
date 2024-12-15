package com.datdeveloper.datmoddingapi.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;

import java.util.UUID;
import java.util.stream.IntStream;

/**
 * A utility class containing various useful codecs
 */
public class DatCodec {
    /**
     * A Codec for handling UUID
     * <br>
     * Converts it into a string for storage
     *
     * @deprecated Use {@link net.minecraft.core.UUIDUtil#CODEC} instead
     */
    @Deprecated(since = "1.9.0")
    public static final Codec<UUID> UUID_CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(UUID.fromString(s));
        } catch (final IllegalArgumentException e) {
            return DataResult.error(() -> s + " is not a UUID.");
        }
    }, UUID::toString);

    private DatCodec() {
    }

    /**
     * A helper function to create a Codec that handles the given enum class
     * <br>
     * The resulting codec will convert the enum into a string for storage
     * @param enumClass The class of the enum
     * @param <E> The Enum Type
     * @return A Codec that handles the given enum class
     */
    public static <E extends Enum<E>> Codec<E> getEnumCodec(final Class<E> enumClass) {
        return Codec.STRING.comapFlatMap(string -> {
            try {
                return DataResult.success(E.valueOf(enumClass, string));
            } catch (final IllegalArgumentException e) {
                return DataResult.error(() -> string + " is not an instance of the enum");
            }
        }, E::name);
    }

    /**
     * A Codec for handling ChunkPos
     * <br>
     * This converts the chunkpos into an intstream, similar to {@link net.minecraft.core.BlockPos}
     * @see net.minecraft.core.BlockPos
     */
    public static final Codec<ChunkPos> CHUNKPOS = Codec.INT_STREAM.comapFlatMap(stream ->
            Util.fixedSize(stream, 2).map((ints -> new ChunkPos(ints[0], ints[1]))),
            chunkPos -> IntStream.of(chunkPos.x, chunkPos.z)
    );
}
