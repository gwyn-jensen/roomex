package com.roomex.assessment.integration;

import com.roomex.assessment.transformation.TransformationException;

public interface PartnerIntegrator {
    void requestAvailability(String input, String output) throws TransformationException;
}
