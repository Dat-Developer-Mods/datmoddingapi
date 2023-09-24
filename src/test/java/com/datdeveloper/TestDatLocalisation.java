package com.datdeveloper;

import com.datdeveloper.datmoddingapi.localisation.DatLocalisation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class TestDatLocalisation {
    void validateKeysPresent(final List<String> keys) {
        final DatLocalisation instance = DatLocalisation.getInstance();

        final Map<String, String> translations = instance.getAllTranslations();
        for (final String key : keys) {
            Assertions.assertTrue(translations.containsKey(key));
        }
    }

    void validateKeysNotPresent(final List<String> keys) {
        final DatLocalisation instance = DatLocalisation.getInstance();

        final Map<String, String> translations = instance.getAllTranslations();
        for (final String key : keys) {
            Assertions.assertFalse(translations.containsKey(key));
        }
    }

    void validateKeysFromFile(final String filePath) throws IOException {
        final Gson gson = new Gson();
        final String json = Files.readString(Path.of(filePath));

        final JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        validateKeysPresent(jsonObject.keySet().stream().toList());
    }

    @BeforeEach
    void clearTranslations() {
        final DatLocalisation instance = DatLocalisation.getInstance();
        instance.clearTranslations();
    }

    /**
     * Test loading translations from the jar
     */
    @Test
    void testLoadTranslationsFromJar() throws IOException {
        final DatLocalisation instance = DatLocalisation.getInstance();
        Assertions.assertDoesNotThrow(() -> instance.loadLocalisations("/test/chatformatting/en_gb.json"));
        validateKeysFromFile("src/test/resources/test/chatformatting/en_gb.json");
    }

    /**
     * Test loading translations from the disk
     */
    @Test
    void testLoadTranslationsFromDisk() throws IOException {
        final DatLocalisation instance = DatLocalisation.getInstance();
        // This should be the path of the resources on disk
        Assertions.assertDoesNotThrow(() -> instance.loadLocalisations("src/test/resources/test/chatformatting/en_gb.json"));
    }

    /**
     * Test bad path for translations file
     */
    @Test
    void testLoadTranslationsBadPath() {
        final DatLocalisation instance = DatLocalisation.getInstance();
        Assertions.assertThrows(FileNotFoundException.class, () -> instance.loadLocalisations("bad/path/to/translations"));
    }

    /**
     * Test adding strings manually
     */
    @Test
    void testAddTranslations() {
        final DatLocalisation instance = DatLocalisation.getInstance();
        instance.addLocalisation("datmoddingapi.test", "testing no really");
        validateKeysPresent(List.of("datmoddingapi.test"));
    }

    /**
     * Test adding strings manually
     */
    @Test
    void testAddBadTranslations() {
        final DatLocalisation instance = DatLocalisation.getInstance();
        instance.addLocalisation("datmoddingapi", "testing no really");
        instance.addLocalisation("datmoddingapi..", "testing no really");
        instance.addLocalisation(".test", "testing no really");
        instance.addLocalisation("", "testing no really");
        validateKeysNotPresent(List.of("datmoddingapi", "datmoddingapi..", ".test", ""));
    }

    /**
     * Test Overriding string
     */
    @Test
    void testOverrideTranslation() {
        final DatLocalisation instance = DatLocalisation.getInstance();
        instance.addLocalisation("datmoddingapi.test", "testing first try");
        Assertions.assertEquals("testing first try", instance.getLocalisation("datmoddingapi.test"));
        instance.addLocalisation("datmoddingapi.test", "testing second try");
        Assertions.assertEquals("testing second try", instance.getLocalisation("datmoddingapi.test"));
    }

    /**
     * Test Default string
     */
    @Test
    void testDefaultTranslation() {
        final DatLocalisation instance = DatLocalisation.getInstance();
        Assertions.assertEquals("Test nothing", instance.getLocalisation("datmoddingapi.test", "Test nothing"));
        instance.addLocalisation("datmoddingapi.test", "testing something");
        Assertions.assertEquals("testing something", instance.getLocalisation("datmoddingapi.test", "Test nothing"));
    }
}
