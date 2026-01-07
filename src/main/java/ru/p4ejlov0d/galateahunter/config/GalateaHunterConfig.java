package ru.p4ejlov0d.galateahunter.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

@Config(name = MOD_ID)
public class GalateaHunterConfig implements ConfigData {
    @Comment("Default: null")
    private String languageCode = null;
    @Comment("Default: true")
    private boolean isBeautifulBazaarCategoryEnabled = true;

    private Tracking tracking = new Tracking();

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isBeautifulBazaarCategoryEnabled() {
        return isBeautifulBazaarCategoryEnabled;
    }

    public void setBeautifulBazaarCategoryEnabled(boolean beautifulBazaarCategoryEnabled) {
        isBeautifulBazaarCategoryEnabled = beautifulBazaarCategoryEnabled;
    }

    public boolean isHuntingBoxEnabled() {
        return tracking.isHuntingBoxEnabled;
    }

    public void setHuntingBoxEnabled(boolean huntingBoxEnabled) {
        tracking.isHuntingBoxEnabled = huntingBoxEnabled;
    }

    public boolean isAttributeMenuEnabled() {
        return tracking.isAttributeMenuEnabled;
    }

    public void setAttributeMenuEnabled(boolean attributeMenuEnabled) {
        tracking.isAttributeMenuEnabled = attributeMenuEnabled;
    }

    private static class Tracking {
        @Comment("Default: true")
        private boolean isHuntingBoxEnabled = true;

        @Comment("Default: true")
        private boolean isAttributeMenuEnabled = true;
    }
}
