package ru.p4ejlov0d.galateahunter.screen;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;
import ru.p4ejlov0d.galateahunter.utils.config.ModConfigHolder;

import java.util.List;
import java.util.Optional;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.*;
import static ru.p4ejlov0d.galateahunter.utils.config.ModConfigHolder.getConfig;

public class GalateaHunterScreen {
    private static final MutableText TITLE;

    static {
        TITLE = Text.literal(NAME + " v" + VERSION);
    }

    private GalateaHunterScreen() {
    }

    public static Screen createGui(Screen parent) {
        return createGui(parent, null);
    }

    public static Screen createGui() {
        return createGui(null, null);
    }

    public static Screen createGui(Screen parent, String category) {
        LanguageResourceHandler languageResourceHandler = LanguageResourceHandler.getInstance();
        LanguageModel languageModel = languageResourceHandler.getLanguageModel();

        LOGGER.debug("Creating main mod GUI");

        ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(TITLE);
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        entryBuilder.setResetButtonKey(Text.literal(languageModel.reset()));

        // categories
        createGeneralCategory(configBuilder, languageModel, entryBuilder, languageResourceHandler);
        createHuntingCategory(configBuilder, languageModel, entryBuilder);

        configBuilder.setSavingRunnable(ModConfigHolder::save);

        Optional.ofNullable(category).ifPresent(string -> {
            if (configBuilder.hasCategory(Text.literal(string)))
                configBuilder.setFallbackCategory(configBuilder.getOrCreateCategory(Text.literal(string)));
        });

        Screen generalScreen = configBuilder.build();

        LOGGER.debug("Successfully created main mod GUI");

        return generalScreen;
    }

    private static void createGeneralCategory(ConfigBuilder configBuilder, LanguageModel languageModel, ConfigEntryBuilder entryBuilder, LanguageResourceHandler languageResourceHandler) {
        LOGGER.debug("Creating general category");

        ConfigCategory general = configBuilder.getOrCreateCategory(Text.literal(languageModel.generalCategory()));
        general.setDescription(LanguageModel.parseTexts(languageModel.generalDescriptions()));

        SelectionListEntry<String> changeLanguage = entryBuilder.startSelector(Text.literal(languageModel.lang()), languageResourceHandler.loadLangNames(), languageModel.langName())
                .setDefaultValue("English")
                .setSaveConsumer(lang -> {
                    LanguageManager langManager = MinecraftClient.getInstance().getLanguageManager();
                    if (getConfig().getLanguageCode() == null && langManager.getLanguage(langManager.getLanguage()).name().equals(lang)) {
                        return;
                    }

                    languageResourceHandler.changeLangCodeByLangName(lang);
                })
                .setTooltip(Text.literal(languageModel.languageTooltip()))
                .build();

        BooleanListEntry beautifulBazaarCategory = entryBuilder.startBooleanToggle(Text.literal(languageModel.beautifulBazaar()), getConfig().isBeautifulBazaarCategoryEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(getConfig()::setBeautifulBazaarCategoryEnabled)
                .setYesNoTextSupplier(bool -> bool ? Text.literal(languageModel.enabled()).withColor(Colors.GREEN) : Text.literal(languageModel.disabled()).withColor(Colors.LIGHT_RED))
                .setTooltip(Text.literal(languageModel.beautifulBazaarTooltip()))
                .build();

        BooleanListEntry resetLanguage = entryBuilder.startBooleanToggle(Text.literal(languageModel.resetLanguage()), false)
                .setDefaultValue(false)
                .setSaveConsumer(bool -> {
                    if (bool) languageResourceHandler.changeLangCodeByLangName(null);
                })
                .setYesNoTextSupplier(bool -> bool ? Text.literal(languageModel.resetTrue()).withColor(Colors.LIGHT_RED) : Text.literal(languageModel.resetFalse()).withColor(Colors.GREEN))
                .setTooltip(Text.literal(languageModel.resetLanguageTooltip()).setStyle(Style.EMPTY.withColor(Colors.LIGHT_YELLOW).withBold(true)))
                .build();

        BooleanListEntry resetSettings = entryBuilder.startBooleanToggle(Text.literal(languageModel.resetSettings()), false)
                .setDefaultValue(false)
                .setSaveConsumer(bool -> {
                    if (bool) {
                        languageResourceHandler.changeLangCodeByLangName(null);
                        ModConfigHolder.reset();
                    }
                })
                .setYesNoTextSupplier(bool -> bool ? Text.literal(languageModel.resetTrue()).styled(style -> style.withColor(Colors.RED).withBold(true)) : Text.literal(languageModel.resetFalse()).withColor(Colors.GREEN))
                .setTooltip(Text.literal(languageModel.resetSettingsTooltip()).setStyle(Style.EMPTY.withColor(Colors.YELLOW).withBold(true)))
                .build();

        general.addEntry(changeLanguage);
        general.addEntry(beautifulBazaarCategory);
        general.addEntry(resetLanguage);
        general.addEntry(resetSettings);

        LOGGER.debug("Successfully created general category");
    }

    private static void createHuntingCategory(ConfigBuilder configBuilder, LanguageModel languageModel, ConfigEntryBuilder entryBuilder) {
        LOGGER.debug("Creating hunting category");

        ConfigCategory hunting = configBuilder.getOrCreateCategory(Text.literal(languageModel.huntingCategory()));
        hunting.setDescription(LanguageModel.parseTexts(languageModel.huntingDescriptions()));

        BooleanListEntry huntingBoxToggle = entryBuilder.startBooleanToggle(Text.literal(languageModel.huntingBox()), getConfig().isHuntingBoxEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(getConfig()::setHuntingBoxEnabled)
                .setYesNoTextSupplier(bool -> bool ? Text.literal(languageModel.enabled()).withColor(Colors.GREEN) : Text.literal(languageModel.disabled()).withColor(Colors.LIGHT_RED))
                .setTooltip(Text.literal(languageModel.huntingBoxTooltip()))
                .build();

        BooleanListEntry attributeMenuToggle = entryBuilder.startBooleanToggle(Text.literal(languageModel.attributeMenu()), getConfig().isAttributeMenuEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(getConfig()::setAttributeMenuEnabled)
                .setYesNoTextSupplier(bool -> bool ? Text.literal(languageModel.enabled()).withColor(Colors.GREEN) : Text.literal(languageModel.disabled()).withColor(Colors.LIGHT_RED))
                .setTooltip(Text.literal(languageModel.attributeMenuTooltip()))
                .build();

        SubCategoryListEntry trackingSubCategory = entryBuilder.startSubCategory(Text.literal(languageModel.tracking()), List.of(huntingBoxToggle, attributeMenuToggle))
                .setTooltip(Text.literal(languageModel.trackingTooltip()))
                .build();

        hunting.addEntry(trackingSubCategory);

        LOGGER.debug("Successfully created hunting category");
    }
}
