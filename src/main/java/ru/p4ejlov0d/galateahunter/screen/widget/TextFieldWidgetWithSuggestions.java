package ru.p4ejlov0d.galateahunter.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.Shard;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.LOGGER;
import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class TextFieldWidgetWithSuggestions extends TextFieldWidget {
    private final List<Shard> need2BeVisible = new ArrayList<>();
    private final int x;
    private final int y;
    private final TextRenderer textRenderer;
    private final ButtonTextures TEXTURES = new ButtonTextures(Identifier.of(MOD_ID, "textures/gui/search-field.png"), Identifier.of(MOD_ID, "textures/gui/search-field-highlighted.png"));
    private final Map<Shard, SuggestionWidget> childrens = new HashMap<>();
    private List<Shard> suggestions;
    private boolean isSelectedSuggestion = false;
    private Shard selectedSuggestion;

    public TextFieldWidgetWithSuggestions(TextRenderer textRenderer, int x, int y, int width, int height) {
        this(textRenderer, x, y, width, height, new ArrayList<>());
    }

    public TextFieldWidgetWithSuggestions(TextRenderer textRenderer, int x, int y, int width, int height, List<Shard> suggestions) {
        super(textRenderer, x, y, width, height, Text.empty());
        this.suggestions = suggestions;
        this.x = x;
        this.y = y;
        this.textRenderer = textRenderer;

        super.setChangedListener(string -> {
            need2BeVisible.clear();
            childrens.clear();

            if (string == null || string.isBlank()) {
                super.setSuggestion(null);
                return;
            }

            for (Shard suggestion : this.suggestions) {
                if (suggestion.getName().toLowerCase().startsWith(string.toLowerCase())) {
                    need2BeVisible.addFirst(suggestion);
                } else if (suggestion.getName().toLowerCase().contains(string.toLowerCase())) {
                    need2BeVisible.add(suggestion);
                }
            }

            if (!need2BeVisible.isEmpty()) {
                String suggestion = need2BeVisible.getFirst().getName();

                super.setSuggestion(suggestion.substring(suggestion.toLowerCase().indexOf(string.toLowerCase()) + string.length()));
            } else super.setSuggestion(null);
        });
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURES.get(isNarratable(), isFocused()), x - 2, y - 2, 0f, 0f, width + 4, height + 4, width + 4, height + 4);

        if (isSelectedSuggestion) {
            context.drawTexture(RenderLayer::getGuiTextured, selectedSuggestion.getTexture(), x - 2 - height, y + height / 2 - (height - 6) / 2, 0f, 0f, height - 6, height - 6, height - 6, height - 6);
        }

        if (isActive()) renderSuggestion(context, y + height + 2, 0, mouseX, mouseY);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        try {
            Method onChanged = TextFieldWidget.class.getDeclaredMethod("onChanged", String.class);
            onChanged.setAccessible(true);
            onChanged.invoke(this, getText());

            // do not invoke onChanged
            Field text = TextFieldWidget.class.getDeclaredField("text");
            text.setAccessible(true);
            text.set(this, "");
            setSelectionStart(0);
            setSelectionEnd(0);
        } catch (Exception e) {
            LOGGER.error("An error occurred in onClick method", e);
            MinecraftClient.getInstance().setScreen(null);
        }
    }

    private void renderSuggestion(DrawContext context, int y, int idx, int mouseX, int mouseY) {
        if (idx >= need2BeVisible.size()) return;

        int x = this.x - 2;
        int width = this.width + 4;
        Shard shard = need2BeVisible.get(idx);

        SuggestionWidget suggestion = new SuggestionWidget(x, y, width, height, Identifier.of(MOD_ID, "textures/gui/" + shard.getRarity() + ".png"), Identifier.of(MOD_ID, "textures/gui/" + shard.getRarity() + "-selected.png"), btn -> {
            selectedSuggestion = shard;
            setText(shard.getName());
            setSuggestion(null);
            need2BeVisible.clear();
            isSelectedSuggestion = true;
            setFocused(false);
        });
        childrens.put(shard, suggestion);
        suggestion.renderWidget(context, shard, textRenderer, mouseX, mouseY);

        renderSuggestion(context, y + height, idx + 1, mouseX, mouseY);
    }

    public List<Shard> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Shard> suggestions) {
        this.suggestions = suggestions;
    }

    public boolean interviewChildren(double mouseX, double mouseY, int button) {
        for (SuggestionWidget suggestion : childrens.values()) {
            if (suggestion.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return false;
    }

    public boolean isSelectedSuggestion() {
        return isSelectedSuggestion;
    }

    public Shard getSelectedSuggestion() {
        return selectedSuggestion;
    }
}
