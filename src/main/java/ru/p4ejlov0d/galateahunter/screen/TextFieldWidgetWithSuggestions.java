package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.Shard;

import java.util.ArrayList;
import java.util.List;

import static ru.p4ejlov0d.galateahunter.GalateaHunter.MOD_ID;

public class TextFieldWidgetWithSuggestions extends TextFieldWidget {
    private final List<Shard> need2BeVisible = new ArrayList<>();
    private final int x;
    private final int y;
    private final TextRenderer textRenderer;
    private final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.of(MOD_ID, "textures/gui/search-field.png"),
            Identifier.of(MOD_ID, "textures/gui/search-field-highlighted.png")
    );
    private List<Shard> suggestions;

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
        if (isActive()) renderSuggestion(context, y + height + 2, 0);
    }

    private void renderSuggestion(DrawContext context, int y, int idx) {
        if (idx >= need2BeVisible.size()) return;

        int x = this.x - 2;
        int width = this.width + 4;
        Shard shard = need2BeVisible.get(idx);

        context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(MOD_ID, "textures/gui/" + shard.getRarity() + ".png"), x, y, 0f, 0f, width, height, width, height);
        context.drawTexture(RenderLayer::getGuiTextured, shard.getTexture(), x + 4, y + 2, 0f, 0f, height - 6, height - 6, height - 6, height - 6);
        context.drawText(textRenderer, Text.literal(shard.getName()).withColor(getColorByRarity(shard)).append(Text.literal(" (" + shard.getId().toUpperCase() + ")").withColor(0xFF808080)), x + height + 2, y + height - height * 85 / 100, 0xFFFFFFFF, false);
        context.drawText(textRenderer, shard.getFamily(), x + height + 2, y + height * 62 / 100, 0xFFFFFFFF, false);

        renderSuggestion(context, y + height, idx + 1);
    }

    private int getColorByRarity(Shard shard) {
        return switch (shard.getRarity()) {
            case "uncommon" -> 0xFF05DF72;
            case "rare" -> 0xFF51A2FF;
            case "epic" -> 0xFFC27AFF;
            case "legendary" -> 0xFFFFD700;
            case null, default -> 0xFFFFFFFF;
        };
    }

    public List<Shard> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Shard> suggestions) {
        this.suggestions = suggestions;
    }
}
