package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import ru.p4ejlov0d.galateahunter.model.Shard;

import java.util.ArrayList;
import java.util.List;

public class TextFieldWidgetWithSuggestions extends TextFieldWidget {
    private final List<Shard> need2BeVisible = new ArrayList<>();
    private final int x;
    private final int y;
    private final TextRenderer textRenderer;
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
                if (suggestion.getName().toLowerCase().contains(string.toLowerCase())) {
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
        renderSuggestion(context, y + height, 0);
    }

    private void renderSuggestion(DrawContext context, int y, int idx) {
        if (idx >= need2BeVisible.size()) return;

        Shard shard = need2BeVisible.get(idx);

        context.fillGradient(x, y, x + width, y + height, Colors.BLACK, 0xFF171717);
        context.drawBorder(x, y, width, height, Colors.WHITE);
        context.drawTexture(RenderLayer::getGuiTextured, shard.getTexture(), x + 2, y + 2, 0f, 0f, height - 4, height - 4, height - 4, height - 4);
        context.drawText(textRenderer, shard.getName(), x + height + 2, y + height / 4, Colors.WHITE, false);

        renderSuggestion(context, y + height, idx + 1);
    }

    public List<Shard> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Shard> suggestions) {
        this.suggestions = suggestions;
    }
}
