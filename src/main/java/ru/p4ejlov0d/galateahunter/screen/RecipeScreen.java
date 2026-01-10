package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;

public class RecipeScreen extends Screen {
    private static final Text TITLE;

    static {
        TITLE = Text.literal("Recipe");
    }

    private final LanguageModel languageModel;
    private String searchText;

    {
        languageModel = LanguageResourceHandler.getInstance().getLanguageModel();
    }

    public RecipeScreen() {
        super(TITLE);
    }

    public RecipeScreen(String searchText) {
        this();
        this.searchText = searchText;
    }

    @Override
    protected void init() {
        TextFieldWidget search = new TextFieldWidget(textRenderer, 25, 20, this.width - 50, 20, Text.empty());
        search.setPlaceholder(Text.literal(languageModel.search()));

        if (searchText != null) {
            search.write(searchText);
        }

        addDrawableChild(search);
    }
}
