package ru.p4ejlov0d.galateahunter.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;

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

        general.addEntry(changeLanguage);

        LOGGER.debug("Successfully created general category");
    }

    private static void createHuntingCategory(ConfigBuilder configBuilder, LanguageModel languageModel, ConfigEntryBuilder entryBuilder) {
        LOGGER.debug("Creating hunting category");

        ConfigCategory hunting = configBuilder.getOrCreateCategory(Text.literal(languageModel.huntingCategory()));
        hunting.setDescription(LanguageModel.parseTexts(languageModel.huntingDescriptions()));

        LOGGER.debug("Successfully created hunting category");
    }
}
