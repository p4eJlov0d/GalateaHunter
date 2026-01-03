package ru.p4ejlov0d.galateahunter.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.resource.Resource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import ru.p4ejlov0d.galateahunter.model.LanguageModel;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.*;

import static net.minecraft.text.Text.translatable;

public class GalateaHunterScreen extends Screen {

    private static final MutableText TITLE;
    public static final Map<String, Resource> LANG_FILES;
    private String currentLangCode = "en_us";
    private final Map<String, ClickableWidget> widgets = new HashMap<>();

    static {
        TITLE = Text.literal("Galatea Hunter");
        LANG_FILES = new LinkedHashMap<>();
    }

    public GalateaHunterScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        TextWidget textWidget = new TextWidget(10, 10, 120, 20, translatable("galateahunter.lang"), textRenderer);
        ButtonWidget buttonWidget = ButtonWidget.builder(translatable("galateahunter.btn"), (btn) -> {
            Iterator<Map.Entry<String, Resource>> iterator = LANG_FILES.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Resource> entry = iterator.next();

                if(entry.getKey().equals(currentLangCode)) {
                    try {
                        currentLangCode = iterator.next().getKey();
                    } catch (Exception e) {
                        currentLangCode = LANG_FILES.entrySet().stream().findFirst().get().getKey();
                    } finally {
                        loadLanguage();
                    }

                    break;
                }
            }
        }).dimensions(40, 40, 120, 20).build();

        widgets.put("lang", addDrawableChild(textWidget));
        widgets.put("btn", addDrawableChild(buttonWidget));

        loadLanguage();
    }

    private void loadLanguage() {
        LanguageModel lang = jsonToModel();

        for (Map.Entry<String, ClickableWidget> widgetEntry : widgets.entrySet()) {
            try {
                String key = widgetEntry.getKey();
                ClickableWidget value = widgetEntry.getValue();
                Field langField = lang.getClass().getDeclaredField(key);

                langField.setAccessible(true);

                value.setMessage(Text.literal((String) langField.get(lang)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private LanguageModel jsonToModel() {
        try {
            StringBuilder json = new StringBuilder();
            String s;
            BufferedReader reader = LANG_FILES.get(currentLangCode).getReader();
            ObjectMapper mapper = new ObjectMapper();

            while ((s = reader.readLine()) != null) {
                json.append(s);
            }

            return mapper.readValue(json.toString(), LanguageModel.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
