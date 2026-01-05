package ru.p4ejlov0d.galateahunter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LanguageModel(
        @JsonProperty("galateahunter.general_category") String generalCategory,
        @JsonProperty("galateahunter.lang") String lang,
        @JsonProperty("galateahunter.lang_name") String langName,
        @JsonProperty("galateahunter.language_tooltip") String languageTooltip,
        @JsonProperty("galateahunter.reset") String reset
) {
}