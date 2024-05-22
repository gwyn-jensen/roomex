package com.roomex.assessment.integration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestLanguage {

    @Test
    public void testGetLanguageId() {

        assertEquals("EN", Language.getLanguageId("EN").toString());
        assertEquals("EN", Language.getLanguageId("en").toString());
        assertEquals("ES", Language.getLanguageId("ES").toString());
        assertEquals("ES", Language.getLanguageId("es").toString());
        assertEquals("FR", Language.getLanguageId("FR").toString());
        assertEquals("FR", Language.getLanguageId("fr").toString());
    }

    @Test
    public void testGetUnrecognisedLanguageId() {

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> Language.getLanguageId("UNRECOGNISED"));

        assertEquals("No language exists with id: UNRECOGNISED", iae.getMessage());
    }
}
