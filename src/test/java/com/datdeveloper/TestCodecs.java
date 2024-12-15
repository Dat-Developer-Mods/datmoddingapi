package com.datdeveloper;

import com.datdeveloper.datmoddingapi.util.DatCodec;
import com.datdeveloper.datmoddingapi.util.ENotificationType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.ChunkPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Tests to ensure functionality of CODECs
 */
public class TestCodecs {

    /**
     * Test enum correctly converts to json
     */
    @Test
    void testEnumToJson() {
        final ENotificationType test = ENotificationType.ACTIONBAR;

        final Optional<JsonElement> result = DatCodec.getEnumCodec(ENotificationType.class)
                                                     .encodeStart(JsonOps.INSTANCE, test)
                                                     .result();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(test.name(), result.get().getAsString());
    }

    /**
     * Test Json correctly converts into Enum
     */
    @Test
    void testJsonToEnum() {
        final ENotificationType test = ENotificationType.ACTIONBAR;

        final Gson gson = new Gson();
        final JsonElement object = gson.toJsonTree(test.name());

        final Optional<ENotificationType> result = DatCodec.getEnumCodec(ENotificationType.class)
                                                           .parse(JsonOps.INSTANCE, object)
                                                           .result();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(test, result.get());
    }

    /**
     * Test Json to enum correctly handles bad enum
     */
    @Test
    void testBadJsonToEnum() {
        final Gson gson = new Gson();
        final JsonElement object = gson.toJsonTree("Totally not a real ENotificationType");

        final Optional<ENotificationType> result = DatCodec.getEnumCodec(ENotificationType.class)
                                                           .parse(JsonOps.INSTANCE, object)
                                                           .result();

        Assertions.assertFalse(result.isPresent());
    }

    /**
     * Test ChunkPos correctly converts to json
     */
    @Test
    void testChunkPosToJson() {
        final ChunkPos chunkPos = new ChunkPos(5, 12);

        final Optional<JsonElement> result = DatCodec.CHUNKPOS.encodeStart(JsonOps.INSTANCE, chunkPos).result();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("[" + chunkPos.x + "," + chunkPos.z + "]", result.get().toString());
    }

    /**
     * Test Json correctly converts into ChunkPos
     */
    @Test
    void testJsonToChunkPos() {

        final ChunkPos chunkPos = new ChunkPos(5, 12);
        final Gson gson = new Gson();
        final JsonElement object = gson.fromJson("[%d,%d]".formatted(chunkPos.x, chunkPos.z), JsonArray.class);

        final Optional<ChunkPos> result = DatCodec.CHUNKPOS.parse(JsonOps.INSTANCE, object).result();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(chunkPos, result.get());
    }

    /**
     * Test Json to enum correctly handles bad enum
     */
    @Test
    void testBadJsonToBlockPos() {
        final Gson gson = new Gson();
        final JsonElement object = gson.fromJson("[1,2,3,4,5,6]", JsonArray.class);

        final Optional<ChunkPos> result = DatCodec.CHUNKPOS.parse(JsonOps.INSTANCE, object).result();

        Assertions.assertFalse(result.isPresent());
    }
}
