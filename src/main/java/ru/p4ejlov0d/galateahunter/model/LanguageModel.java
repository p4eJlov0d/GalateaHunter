package ru.p4ejlov0d.galateahunter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public record LanguageModel(
        @JsonProperty("galateahunter.general_category") String generalCategory,
        @JsonProperty("galateahunter.lang") String lang,
        @JsonProperty("galateahunter.lang_name") String langName,
        @JsonProperty("galateahunter.language_tooltip") String languageTooltip,
        @JsonProperty("galateahunter.reset") String reset,
        @JsonProperty("galateahunter.hunting_category") String huntingCategory,
        @JsonProperty("galateahunter.general_descriptions") String[] generalDescriptions,
        @JsonProperty("galateahunter.hunting_descriptions") String[] huntingDescriptions,
        @JsonProperty("galateahunter.tracking") String tracking,
        @JsonProperty("galateahunter.tracking_tooltip") String trackingTooltip,
        @JsonProperty("galateahunter.hunting_box") String huntingBox,
        @JsonProperty("galateahunter.hunting_box_tooltip") String huntingBoxTooltip,
        @JsonProperty("galateahunter.attribute_menu") String attributeMenu,
        @JsonProperty("galateahunter.attribute_menu_tooltip") String attributeMenuTooltip,
        @JsonProperty("galateahunter.enabled_bool") String enabled,
        @JsonProperty("galateahunter.disabled_bool") String disabled,
        @JsonProperty("galateahunter.beautiful_bazaar") String beautifulBazaar,
        @JsonProperty("galateahunter.beautiful_bazaar_tooltip") String beautifulBazaarTooltip
) {
    public static MutableText[] parseTexts(String[] descriptions) {
        MutableText[] texts = new MutableText[descriptions.length];

        for (int i = 0; i < descriptions.length; i++) {
            texts[i] = Text.literal(descriptions[i]);
        }

        return texts;
    }
}