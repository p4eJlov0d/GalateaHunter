package ru.p4ejlov0d.galateahunter.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IconButtonWidget extends PressableWidget {
    private final Runnable onPress;
    private final ButtonTextures textures;
    private final int x;
    private final int y;

    public IconButtonWidget(int x, int y, int width, int height, Identifier icon, Identifier activeIcon, Runnable onPress) {
        super(x, y, width, height, Text.empty());
        this.x = x;
        this.y = y;
        this.textures = new ButtonTextures(icon, activeIcon);
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        this.onPress.run();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawTexture(RenderLayer::getGuiTextured, textures.get(isNarratable(), isHovered()), x, y, 0f, 0f, width, height, width, height);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }
}
