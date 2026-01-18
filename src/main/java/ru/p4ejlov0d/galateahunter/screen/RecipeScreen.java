package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;
import ru.p4ejlov0d.galateahunter.repo.ShardRepo;
import ru.p4ejlov0d.galateahunter.repo.impl.ShardRepoImpl;
import ru.p4ejlov0d.galateahunter.screen.widget.IconButtonWidget;
import ru.p4ejlov0d.galateahunter.screen.widget.TextFieldWidgetWithSuggestions;
import ru.p4ejlov0d.galateahunter.utils.LanguageResourceHandler;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class RecipeScreen extends Screen {
    private static final Text TITLE;

    static {
        TITLE = Text.literal("Recipe");
    }

    private final ShardRepo shardRepo;
    private final LanguageModel languageModel;
    private TextFieldWidgetWithSuggestions search;
    private IconButtonWidget close;
    private IconButtonWidget list;
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
        search = new TextFieldWidgetWithSuggestions(textRenderer, (int) (this.width / 3.3333333d), 5, (int) (this.width / 2.5d), 30);
        search.setPlaceholder(Text.literal(languageModel.search()));
        search.setSuggestions(shardRepo.getShards().values().stream().toList());

        if (searchText != null) {
            search.write(searchText);
            search.setFocused(true);
        }

        IconButtonWidget settings = new IconButtonWidget(width - 30, 10, 20, 20,
                Identifier.of(MOD_ID, "textures/gui/settings.png"),
                Identifier.of(MOD_ID, "textures/gui/settings-highlighted.png"),
                btn -> client.setScreen(GalateaHunterScreen.createGui(this, languageModel.huntingCategory()))
        );

        close = new IconButtonWidget(5, 20, 15, 15,
                Identifier.of(MOD_ID, "textures/gui/close.png"),
                Identifier.of(MOD_ID, "textures/gui/close-highlighted.png"),
                null
        );

        close.visible = false;

        list = new IconButtonWidget(5, 20, 15, 15,
                Identifier.of(MOD_ID, "textures/gui/list.png"),
                Identifier.of(MOD_ID, "textures/gui/list-highlighted.png"),
                null
        );

        list.visible = false;

        close.setOnPress(btn -> {
            btn.visible = false;
            list.visible = true;
        });

        list.setOnPress(btn -> {
            btn.visible = false;
            close.visible = true;
        });

        addDrawableChild(search);
        addDrawableChild(settings);
        addDrawableChild(list);
        addDrawableChild(close);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (search.interviewChildren(mouseX, mouseY, button)) {
            close.visible = true;
            list.visible = false;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/background-screen.png"), 0, 0, 0f, 0f, width, height, width, height);
        context.draw();
        applyBlur();

        // header
        Identifier header = Identifier.of(MOD_ID, "textures/gui/header.png");
        context.drawTexture(RenderLayer::getGuiTextured, header, 0, 0, 0f, 0f, width, 40, width, 40);

        // overview
        if (search.isSelectedSuggestion() && close.visible) {
            Identifier rectangle = Identifier.of(MOD_ID, "textures/gui/rectangle.png");

            context.drawTexture(RenderLayer::getGuiTextured, rectangle, 5, 45, 0f, 0f, width / 4, 30, width / 4, 30);
            context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/target.png"), 10, 50, 0f, 0f, 20, 20, 20, 20);
            context.drawText(textRenderer, Text.literal(languageModel.overview()).styled(style -> style.withBold(true)), 35, 50, 0xFFFFFFFF, false);

            context.drawTexture(RenderLayer::getGuiTextured, rectangle, 5, 80, 0f, 0f, width / 4, 30, width / 4, 30);
            context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/dollar.png"), 10, 85, 0f, 0f, 20, 20, 20, 20);
            context.drawText(textRenderer, Text.literal(languageModel.totalCoins()).styled(style -> style.withBold(true)), 35, 85, 0xFFFFFFFF, false);

            context.drawTexture(RenderLayer::getGuiTextured, rectangle, 5, 115, 0f, 0f, width / 4, 30, width / 4, 30);
            context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/stonks.png"), 10, 120, 0f, 0f, 20, 20, 20, 20);
            context.drawText(textRenderer, Text.literal(languageModel.coinsSaved()).styled(style -> style.withBold(true)), 35, 120, 0xFFFFFFFF, false);

            context.drawTexture(RenderLayer::getGuiTextured, rectangle, 5, 150, 0f, 0f, width / 4, 30, width / 4, 30);
            context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/prismarine-shard.png"), 10, 155, 0f, 0f, 20, 20, 20, 20);
            context.drawText(textRenderer, Text.literal(languageModel.totalShards()).styled(style -> style.withBold(true)), 35, 155, 0xFFFFFFFF, false);

            context.drawTexture(RenderLayer::getGuiTextured, rectangle, 5, 185, 0f, 0f, width / 4, 30, width / 4, 30);
            context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/loop.png"), 10, 190, 0f, 0f, 20, 20, 20, 20);
            context.drawText(textRenderer, Text.literal(languageModel.totalFusions()).styled(style -> style.withBold(true)), 35, 190, 0xFFFFFFFF, false);

            context.drawTexture(RenderLayer::getGuiTextured, rectangle, 5, 220, 0f, 0f, width / 4, 30, width / 4, 30);
            context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/reptile.png"), 10, 225, 0f, 0f, 20, 20, 20, 20);
            context.drawText(textRenderer, Text.literal(languageModel.totalReptiles()).styled(style -> style.withBold(true)), 35, 225, 0xFFFFFFFF, false);
        }

        for (Element element : children()) {
            ((Drawable) element).render(context, mouseX, mouseY, deltaTicks);
        }
    }
}
