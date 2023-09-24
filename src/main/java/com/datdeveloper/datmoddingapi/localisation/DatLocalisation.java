package com.datdeveloper.datmoddingapi.localisation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A utility for serverside localisation
 * <br>
 * Does not yet support providing arguments
 */
public class DatLocalisation {
    private static final Logger LOGGER = LogUtils.getLogger();

    /** The available translations */
    private final Map<String, String> translations;

    /**
     * A pattern for valid keys
     * <br>
     * The gist is that a key is made of 2 or more parts consisting of numbers, letters, underscore and dashes, where
     * each part is separated by a ".". The first part is intended to be an identifier for the mod that uses the
     * translation, however this is not enforced.
     * <br>
     * Valid keys include:<br>
     * datmoddingapi.key, minecraft.LO_NG.key, forge.really.really.li-ke.I.mean.really.long.key.42
     * <br><br>
     * Invalid keys include:<br>
     * mymod, my&.test, %test.(you).doesn't.work
     */
    private static final Pattern KEY_PATTERN = Pattern.compile("^([\\w-])\\.([\\w-](?:\\.[\\w-])+)$");

    private DatLocalisation() {
        translations = new HashMap<>();
    }

    private static final DatLocalisation INSTANCE = new DatLocalisation();

    /**
     * Get the DatLocalisation instance with the translations loaded
     * @return The DatLocalisation instance
     */
    public static DatLocalisation getInstance() {
        return INSTANCE;
    }

    /**
     * Load in a JSON file of localisations from a file
     * <br>
     * First checks filepath, then checks classpath, if no file is found then an error will be logged
     * <br>
     * Keys already in the table will be overridden.
     * <br>
     * Bad keys will be discarded with an error printed to console
     * <br>
     * The locale file is expected to be a json file, containing a single object that maps strings to strings
     * <br>
     * The keys of this locale file are expected to match the regex: ^([\w-])\.([\w-](?:\.[\w-])+)$
     * <br>
     * Essentially, a key is made of 2 or more parts consisting of numbers, letters, underscore and dashes, where
     * each part is separated by a ".". The first part is intended to be an identifier for the mod that uses the
     * translation, however this is not enforced.
     * @param localeFilePath The path on the classpath to the locale file containing the translations
     */
    public void loadLocalisations(final String localeFilePath) {
        try {
            if (Files.exists(Path.of(localeFilePath))) {
                try (final InputStream inputStream = new FileInputStream(localeFilePath)) {
                    loadLocalisationsFromStream(inputStream);
                }
            } else if (DatLocalisation.class.getResource(localeFilePath) != null) {
                try (final InputStream inputStream = DatLocalisation.class.getResourceAsStream(localeFilePath)) {
                    loadLocalisationsFromStream(inputStream);
                }
            } else {
                LOGGER.warn("Failed to find locale file: {}", localeFilePath);
            }
        } catch (final IOException e) {
            LOGGER.warn("Failed to load locale file: {}\n{}", localeFilePath, e.getMessage());
        } catch (final JsonSyntaxException e) {
            LOGGER.warn("Failed to parse locale file: {}\n{}", localeFilePath, e.getMessage());
        }
    }

    @SuppressWarnings("java:S4449")
    private void loadLocalisationsFromStream(final InputStream localisationsFile) {
        final Gson gson = new Gson();

        final JsonObject json = gson.fromJson(new InputStreamReader(localisationsFile, StandardCharsets.UTF_8), JsonObject.class);

        for(final Map.Entry<String, JsonElement> entry : json.entrySet()) {
            addLocalisation(entry.getKey(), GsonHelper.convertToString(entry.getValue(), null));
        }
    }

    /**
     * Add a localisation to the available translations.
     * <br>
     * If the key is already in the table then it will be overridden.
     * @param key The key of the translation
     * @param translation The translation
     */
    public void addLocalisation(final String key, final String translation) {
        if (!KEY_PATTERN.matcher(key).find()) {
            LOGGER.warn("Attempted to add bad key: {}, discarding", key);
            return;
        }

        translations.put(key, translation);
    }

    /**
     * Get a localised string from the store
     * @param key The key of the localised string
     * @return The localised string
     */
    public String getLocalisation(final String key) {
        return translations.get(key);
    }

    /**
     * Get a localised string from the store, returning the fallback if it doesn't exist
     * @param key The key of the localised string
     * @param fallback The string to return if the key isn't in the store
     * @return The localised string or the fallback
     */
    public String getLocalisationOrDefault(final String key, final String fallback) {
        return translations.getOrDefault(key, fallback);
    }
}