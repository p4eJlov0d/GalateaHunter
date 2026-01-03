package ru.p4ejlov0d.galateahunter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LanguageModel(
        @JsonProperty("galateahunter.lang") String lang,
        @JsonProperty("galateahunter.btn") String btn
) {}