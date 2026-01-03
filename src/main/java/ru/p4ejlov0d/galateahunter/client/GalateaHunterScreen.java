package ru.p4ejlov0d.galateahunter.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class GalateaHunterScreen extends Screen {

    private static final MutableText TITLE;

    static {
        TITLE = Text.literal("Galatea Hunter");
    }

    public GalateaHunterScreen() {
        super(TITLE);
    }
}
