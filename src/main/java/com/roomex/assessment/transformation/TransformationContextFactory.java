package com.roomex.assessment.transformation;

import java.util.Map;

public class TransformationContextFactory {

    private static final Map<String, TransformationContext> XFORM_CTXS;
    static {
        // static initializer block to mimic injection from DI context
        XFORM_CTXS = Map.of("input.xml", new TransformationContext("partnera.xslt", new PartnerAStyleSheetCompanion()),
                "test.xml", new TransformationContext("missing.xslt", new PartnerAStyleSheetCompanion()));
    }

    public static TransformationContext getTransformationContext(String input) throws TransformationConfigurationException {
        if(!XFORM_CTXS.containsKey(input)) {
            throw new TransformationConfigurationException("input file exists but transformation configuration is missing");
        }
        return XFORM_CTXS.get(input);
    }
}
