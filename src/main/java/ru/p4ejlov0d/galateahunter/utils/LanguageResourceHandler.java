package ru.p4ejlov0d.galateahunter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;

import java.io.BufferedReader;
import java.util.*;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class LanguageResourceHandler implements SimpleSynchronousResourceReloadListener {
    private static final Map<String, Resource> LANG_FILES;
    private static final Map<String, String> NAME_TO_CODE;
    private static LanguageResourceHandler instance;
    private static String currentLangCode;

    static {
        LANG_FILES = new LinkedHashMap<>();
        currentLangCode = null;
        NAME_TO_CODE = new HashMap<>();
    }

    private LanguageResourceHandler() {
    }

    public static LanguageResourceHandler getInstance() {
        if (instance == null) {
            instance = new LanguageResourceHandler();
        }
        return instance;
    }

    public String[] loadLangNames() {
        List<String> langNamesList = new ArrayList<>();

        LOGGER.debug("Loading language names");

        for (Map.Entry<String, Resource> entry : LANG_FILES.entrySet()) {
            Resource resource = entry.getValue();
            String resourceName = entry.getKey();
            String line;

            try (BufferedReader reader = resource.getReader()) {
                while ((line = reader.readLine()) != null) {
                    if (line.contains("galateahunter.lang_name")) {

                        LOGGER.debug("Loading language name from {}", resourceName);

                        String langName = line.split(":")[1]
                                .replaceAll("\"", "")
                                .replace(",", "")
                                .trim();

                        langNamesList.add(langName);
                        NAME_TO_CODE.put(langName, resourceName);

                        LOGGER.debug("Loaded \"{}\" language name from {}", langName, resourceName);
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to read line in {}", resourceName);
                throw new RuntimeException(e);
            }
        }

        LOGGER.debug("Finished loading language names with {} size", langNamesList.size());

        return langNamesList.toArray(new String[LANG_FILES.size()]);
    }

    public void changeLangCodeByLangName(String langName) {
        String langCode = NAME_TO_CODE.get(langName);

        LOGGER.debug("Changed language code from {} to {}", currentLangCode, langCode);

        currentLangCode = langCode;
    }

    public LanguageModel getLanguageModel() {
        Resource resource = Optional.ofNullable(
                LANG_FILES.get(
                        Optional.ofNullable(currentLangCode).orElseGet(() -> {
                            if (MinecraftClient.getInstance() != null) {
                                return MinecraftClient.getInstance().getLanguageManager().getLanguage();
                            }
                            return null;
                        })
                )
        ).orElse(LANG_FILES.get("en_us"));

        try (BufferedReader reader = resource.getReader()) {
            StringBuilder json = new StringBuilder();
            String s;
            ObjectMapper mapper = new ObjectMapper();

            while ((s = reader.readLine()) != null) {
                json.append(s);
            }

            LOGGER.debug("Deserializing json string: {}", json);

            return mapper.readValue(json.toString(), LanguageModel.class);
        } catch (Exception e) {
            LOGGER.warn("Failed to deserialize object, caused by {}, language code: {}", e.getMessage(), currentLangCode);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.tryParse(MOD_ID, "load_lang");
    }

    @Override
    public void reload(ResourceManager manager) {
        LOGGER.debug("Starting to load language resources");

        for (Identifier id : manager.findResources("lang", path -> path.toString().endsWith(".json")).keySet()) {
            if (id.getNamespace().equals(MOD_ID)) {
                LANG_FILES.put(id.getPath().split("/")[1].split("\\.")[0], manager.getResource(id).get());

                LOGGER.debug("Loaded language resource: {}", id);
            }
        }
        LOGGER.debug("Finished loading language resources with {} loaded resources", LANG_FILES.size());
    }
}
