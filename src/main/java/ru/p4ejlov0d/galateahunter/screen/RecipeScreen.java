package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;
import ru.p4ejlov0d.galateahunter.repo.impl.ShardRepoImpl;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;
import ru.p4ejlov0d.galateahunter.utils.registries.ResourceReloadRegistrar;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class RecipeScreen extends Screen {
    private static final Text TITLE;

    static {
        TITLE = Text.literal("Recipe");
    }

    private final ShardRepo shardRepo;
    private final LanguageModel languageModel;
    private String searchText;

    {
        languageModel = LanguageResourceHandler.getInstance().getLanguageModel();
        shardRepo = ShardRepoImpl.getInstance();
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
        search.setSuggestions(shardRepo.getShards().values().stream().toList());
        ButtonWidget reload = ButtonWidget.builder(Text.literal(languageModel.upload()), btn -> {
            ResourceReloadRegistrar.lazyShardsReloaderRegister();
            assert client != null;
            client.reloadResources();
        }).dimensions(width - 115, height - 25, 110, 20).build();

        if (searchText != null) {
            search.write(searchText);
        }

        addDrawableChild(reload);
        addDrawableChild(search);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/background-screen.png"), 0, 0, 0f, 0f, width, height, width, height);
        context.draw();
        applyBlur();

        for (Element element : children()) {
            ((Drawable) element).render(context, mouseX, mouseY, deltaTicks);
        }
    }
}
