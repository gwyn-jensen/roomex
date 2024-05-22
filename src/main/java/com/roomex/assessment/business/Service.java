package com.roomex.assessment.business;

import com.roomex.assessment.transformation.TransformationException;

import java.io.FileNotFoundException;

public interface Service {

    /**
     * Ensure the requested input resource exists and delegate the request to the integration layer
     *
     * @param input
     * @param output
     * @throws FileNotFoundException
     * @throws TransformationException
     */
    void requestAvailability(String input, String output) throws FileNotFoundException, TransformationException;
}
