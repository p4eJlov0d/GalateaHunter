package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;
import ru.p4ejlov0d.galateahunter.model.Shard;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;

import java.util.List;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

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
        TextFieldWidgetWithSuggestions search = new TextFieldWidgetWithSuggestions(textRenderer, 25, 20, this.width - 50, 20);
        search.setPlaceholder(Text.literal(languageModel.search()));
        search.setSuggestions(List.of(new Shard(Identifier.of(MOD_ID, "textures/gui/wyvern.png"), "Wyvern Shard")));

        if (searchText != null) {
            search.write(searchText);
        }

        addDrawableChild(search);
    }
}
