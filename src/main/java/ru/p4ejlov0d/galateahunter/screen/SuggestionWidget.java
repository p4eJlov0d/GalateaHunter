package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.p4ejlov0d.galateahunter.model.Shard;

public class SuggestionWidget extends PressableWidget {
    private final PressAction onPress;
    private final ButtonTextures textures;
    private final int x;
    private final int y;

    public SuggestionWidget(int x, int y, int width, int height, Identifier icon, Identifier hoveredIcon, PressAction onPress) {
        super(x, y, width, height, Text.empty());
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        textures = new ButtonTextures(icon, hoveredIcon);
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    public void renderWidget(DrawContext context, Shard shard, TextRenderer textRenderer, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, textures.get(isNarratable(), isMouseOver(mouseX, mouseY)), x, y, 0f, 0f, width, height, width, height);
        context.drawTexture(RenderLayer::getGuiTextured, shard.getTexture(), x + 4, y + 2, 0f, 0f, height - 6, height - 6, height - 6, height - 6);
        context.drawText(textRenderer, Text.literal(shard.getName()).withColor(getColorByRarity(shard)).append(Text.literal(" (" + shard.getId().toUpperCase() + ")").withColor(0xFF808080)), x + height + 2, y + height - height * 85 / 100, 0xFFFFFFFF, false);
        context.drawText(textRenderer, shard.getFamily(), x + height + 2, y + height * 62 / 100, 0xFFFFFFFF, false);
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

    public interface PressAction {
        void onPress(SuggestionWidget button);
    }
}
