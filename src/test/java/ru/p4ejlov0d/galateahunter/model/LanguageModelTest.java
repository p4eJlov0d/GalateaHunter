package ru.p4ejlov0d.galateahunter.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageModelTest {

    @Test
    void parseTexts() {
        String[] expected = new String[]{"a", "c", "b", "e", "d", "sfwefewf", "235325"};
        var actual = LanguageModel.parseTexts(expected);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i].getLiteralString());
        }

        assertEquals(expected.length, actual.length);
    }
}