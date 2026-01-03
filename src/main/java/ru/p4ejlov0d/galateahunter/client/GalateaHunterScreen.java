package ru.p4ejlov0d.galateahunter.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static net.minecraft.text.Text.translatable;

public class GalateaHunterScreen extends Screen {

    private static final MutableText TITLE;

    static {
        TITLE = Text.literal("Galatea Hunter");
    }

    public GalateaHunterScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        TextWidget textWidget = new TextWidget(10, 10, 120, 20, translatable("galateahunter.lang"), textRenderer);
        ButtonWidget buttonWidget = ButtonWidget.builder(translatable("galateahunter.btn"), (btn) -> {

        }).dimensions(40, 40, 120, 20).build();

        addDrawableChild(textWidget);
        addDrawableChild(buttonWidget);

    }
}
