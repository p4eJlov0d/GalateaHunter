package ru.p4ejlov0d.galateahunter.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
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

        LOGGER.debug("Creating general GUI");

        ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(TITLE);
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();
        ConfigCategory general = configBuilder.getOrCreateCategory(Text.literal(languageModel.generalCategory()));

        SelectionListEntry<String> changeLanguage = entryBuilder.startSelector(Text.literal(languageModel.lang()), languageResourceHandler.loadLangNames(), languageModel.langName())
                .setSaveConsumer(languageResourceHandler::changeLangCodeByLangName)
                .setTooltip(Text.literal(languageModel.languageTooltip()))
                .build();
        ((ClickableWidget) changeLanguage.children().getLast()).setMessage(Text.literal(languageModel.reset()));

        general.addEntry(changeLanguage);

        Screen generalScreen = configBuilder.build();

        LOGGER.debug("Successfully created general GUI");

        return generalScreen;
    }
}
