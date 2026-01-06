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
        @JsonProperty("galateahunter.hunting_descriptions") String[] huntingDescriptions
) {
    public static MutableText[] parseTexts(String[] descriptions) {
        MutableText[] texts = new MutableText[descriptions.length];

        for (int i = 0; i < descriptions.length; i++) {
            texts[i] = Text.literal(descriptions[i]);
        }

        return texts;
    }
}