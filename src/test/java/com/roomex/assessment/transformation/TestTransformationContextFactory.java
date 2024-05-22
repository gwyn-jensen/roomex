package com.roomex.assessment.transformation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTransformationContextFactory {

    @Test
    public void testGetTransformationContext() throws TransformationConfigurationException {

        TransformationContext ctx = TransformationContextFactory.getTransformationContext("input.xml");
        assertEquals("partnera.xslt", ctx.xslt());
        assertInstanceOf(PartnerAStyleSheetCompanion.class, ctx.decorator());
    }

    @Test
    public void testGetTransformationContextFails() {

        TransformationConfigurationException tce = assertThrows(TransformationConfigurationException.class,
                () -> TransformationContextFactory.getTransformationContext("missing"));

        assertEquals("input file exists but transformation configuration is missing", tce.getMessage());
    }
}
