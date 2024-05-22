package com.roomex.assessment.transformation;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.xml.transform.Transformer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

public class TestPartnerAStyleSheetCompanion {

    @Test
    public void testDecorate() {

        PartnerAStyleSheetCompanion companion = new PartnerAStyleSheetCompanion();

        Transformer transformer = Mockito.mock(Transformer.class);
        companion.decorate(transformer);

        Mockito.verify(transformer).setParameter(eq("message_id"), anyString());
        Mockito.verify(transformer).setParameter(eq("nonce"), anyString());
        Mockito.verify(transformer).setParameter(eq("created"), anyString());
    }

    @Test
    public void testPassword() {


    }

    // ... continue to add unit tests as appropriate
}
