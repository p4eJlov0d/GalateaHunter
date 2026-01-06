package ru.p4ejlov0d.galateahunter.utils;

import net.minecraft.resource.Resource;
import org.junit.jupiter.api.Test;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageResourceHandlerTest {
    @Test
    void loadLangNames() {
        String[] excepted = {"English"};

        Resource resource = new Resource(null, () -> new ByteArrayInputStream("{\"galateahunter.general_category\": \"General\",\n\"galateahunter.lang\": \"Language\",\n\"galateahunter.lang_name\": \"English\",\n\"galateahunter.language_tooltip\": \"Select a language\",\n\"galateahunter.reset\": \"Reset\",\n\"galateahunter.hunting_category\": \"Hunting\",\n\"galateahunter.general_descriptions\": null,\n\"galateahunter.hunting_descriptions\": null,\n\"galateahunter.tracking\": \"Tracking\",\n\"galateahunter.tracking_tooltip\": \"Toggle hunting box/attribute menu tracking\",\n\"galateahunter.hunting_box\": \"Hunting box\",\n\"galateahunter.hunting_box_tooltip\": \"Toggle hunting box tracking.Tracking includes: adding shards,\n deleting\",\n\"galateahunter.attribute_menu\": \"Attribute menu\",\n\"galateahunter.attribute_menu_tooltip\": \"Toggle attribute menu tracking.Tracking includes: disabled attributes,\n attribute level\",\n\"galateahunter.enabled_bool\": \"Enabled\",\n\"galateahunter.disabled_bool\": \"Disabled\",\n\"galateahunter.beautiful_bazaar\": \"Beautiful shard category in bazaar\",\n\"galateahunter.beautiful_bazaar_tooltip\": \"Change the shard category in the bazaar to a more beautiful appearance.Coming soon\"}".getBytes(StandardCharsets.UTF_8)));

        try {
            Field LANG_FILES = LanguageResourceHandler.class.getDeclaredField("LANG_FILES");
            Field NAME_TO_CODE = LanguageResourceHandler.class.getDeclaredField("NAME_TO_CODE");

            LANG_FILES.setAccessible(true);
            NAME_TO_CODE.setAccessible(true);

            LANG_FILES.getType().getDeclaredMethod("put", Object.class, Object.class).invoke(LANG_FILES.get(LANG_FILES), "en_us", resource);

            assertEquals(excepted[0], LanguageResourceHandler.getInstance().loadLangNames()[0]);
            assertEquals("en_us", NAME_TO_CODE.getType().getDeclaredMethod("get", Object.class).invoke(NAME_TO_CODE.get(NAME_TO_CODE), "English"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void changeLangCodeByLangName() {
        String excepted = "en_us";
        LanguageResourceHandler languageResourceHandler = LanguageResourceHandler.getInstance();

        try {
            Field currentLangCode = languageResourceHandler.getClass().getDeclaredField("currentLangCode");
            Field NAME_TO_CODE = languageResourceHandler.getClass().getDeclaredField("NAME_TO_CODE");

            NAME_TO_CODE.setAccessible(true);
            currentLangCode.setAccessible(true);

            Method put = NAME_TO_CODE.getType().getDeclaredMethod("put", Object.class, Object.class);
            Object hashMap = NAME_TO_CODE.get(NAME_TO_CODE);

            put.invoke(hashMap, "English", "en_us");

            languageResourceHandler.changeLangCodeByLangName("English");

            assertEquals(excepted, currentLangCode.get(languageResourceHandler));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getLanguageModel() {
        LanguageResourceHandler languageResourceHandler = LanguageResourceHandler.getInstance();
        LanguageModel expected = new LanguageModel("General",
                "Language",
                "English",
                "Select a language",
                "Reset",
                "Hunting",
                null,
                null,
                "Tracking",
                "Toggle hunting box/attribute menu tracking",
                "Hunting box",
                "Toggle hunting box tracking.Tracking includes: adding shards, deleting",
                "Attribute menu",
                "Toggle attribute menu tracking.Tracking includes: disabled attributes, attribute level",
                "Enabled",
                "Disabled",
                "Beautiful shard category in bazaar",
                "Change the shard category in the bazaar to a more beautiful appearance.Coming soon"
        );

        try {
            Field currentLangCode = languageResourceHandler.getClass().getDeclaredField("currentLangCode");
            Field LANG_FILES = languageResourceHandler.getClass().getDeclaredField("LANG_FILES");

            LANG_FILES.setAccessible(true);
            currentLangCode.setAccessible(true);

            currentLangCode.set(currentLangCode, "en_us");
            LANG_FILES.getType().getDeclaredMethod("put", Object.class, Object.class)
                    .invoke(LANG_FILES.get(LANG_FILES), "en_us", new Resource(null, () -> new ByteArrayInputStream("{\"galateahunter.general_category\": \"General\",\"galateahunter.lang\": \"Language\",\"galateahunter.lang_name\": \"English\",\"galateahunter.language_tooltip\": \"Select a language\",\"galateahunter.reset\": \"Reset\",\"galateahunter.hunting_category\": \"Hunting\",\"galateahunter.general_descriptions\": null,\"galateahunter.hunting_descriptions\": null,\"galateahunter.tracking\": \"Tracking\",\"galateahunter.tracking_tooltip\": \"Toggle hunting box/attribute menu tracking\",\"galateahunter.hunting_box\": \"Hunting box\",\"galateahunter.hunting_box_tooltip\": \"Toggle hunting box tracking.Tracking includes: adding shards, deleting\",\"galateahunter.attribute_menu\": \"Attribute menu\",\"galateahunter.attribute_menu_tooltip\": \"Toggle attribute menu tracking.Tracking includes: disabled attributes, attribute level\",\"galateahunter.enabled_bool\": \"Enabled\",\"galateahunter.disabled_bool\": \"Disabled\",\"galateahunter.beautiful_bazaar\": \"Beautiful shard category in bazaar\",\"galateahunter.beautiful_bazaar_tooltip\": \"Change the shard category in the bazaar to a more beautiful appearance.Coming soon\"}".getBytes(StandardCharsets.UTF_8))));

            assertEquals(expected, languageResourceHandler.getLanguageModel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}