package ru.p4ejlov0d.galateahunter.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;

import java.util.List;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.*;

public class GalateaHunterScreen {
    private static final MutableText TITLE;

    static {
        TITLE = Text.literal(NAME + " v" + VERSION);
    }

    private GalateaHunterScreen() {
    }

    public static Screen createGui() {
        return createGui(null);
    }

    public static Screen createGui(Screen parent) {
        LanguageResourceHandler languageResourceHandler = LanguageResourceHandler.getInstance();
        LanguageModel languageModel = languageResourceHandler.getLanguageModel();

        LOGGER.debug("Creating main mod GUI");

        ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(TITLE);
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        entryBuilder.setResetButtonKey(Text.literal(languageModel.reset()));

        // categories
        createGeneralCategory(configBuilder, languageModel, entryBuilder, languageResourceHandler);
        createHuntingCategory(configBuilder, languageModel, entryBuilder);

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
                .setSaveConsumer(languageResourceHandler::changeLangCodeByLangName)
                .setTooltip(Text.literal(languageModel.languageTooltip()))
                .build();

        BooleanListEntry beautifulBazaarCategory = entryBuilder.startBooleanToggle(Text.literal(languageModel.beautifulBazaar()), true)
                .setDefaultValue(true)
                .setYesNoTextSupplier(bool -> bool ? Text.literal(languageModel.enabled()).withColor(Colors.GREEN) : Text.literal(languageModel.disabled()).withColor(Colors.LIGHT_RED))
                .setTooltip(Text.literal(languageModel.beautifulBazaarTooltip()))
                .build();

        general.addEntry(changeLanguage);
        general.addEntry(beautifulBazaarCategory);

        LOGGER.debug("Successfully created general category");
    }

    private static void createHuntingCategory(ConfigBuilder configBuilder, LanguageModel languageModel, ConfigEntryBuilder entryBuilder) {
        LOGGER.debug("Creating hunting category");

        ConfigCategory hunting = configBuilder.getOrCreateCategory(Text.literal(languageModel.huntingCategory()));
        hunting.setDescription(LanguageModel.parseTexts(languageModel.huntingDescriptions()));

        BooleanListEntry huntingBoxToggle = entryBuilder.startBooleanToggle(Text.literal(languageModel.huntingBox()), true)
                .setDefaultValue(true)
                .setYesNoTextSupplier(bool -> bool ? Text.literal(languageModel.enabled()).withColor(Colors.GREEN) : Text.literal(languageModel.disabled()).withColor(Colors.LIGHT_RED))
                .setTooltip(Text.literal(languageModel.huntingBoxTooltip()))
                .build();

        BooleanListEntry attributeMenuToggle = entryBuilder.startBooleanToggle(Text.literal(languageModel.attributeMenu()), true)
                .setDefaultValue(true)
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
