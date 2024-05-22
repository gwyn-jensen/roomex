package com.roomex.assessment.business;

import com.roomex.assessment.integration.PartnerIntegrator;
import com.roomex.assessment.transformation.TransformationException;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BusinessService implements Service {

    private final PartnerIntegrator integrator;

    public BusinessService(PartnerIntegrator integrator) {
        this.integrator = integrator;
    }

    /**
     * {@inheritDoc}
     *
     */
    public void requestAvailability(String input, String output) throws FileNotFoundException, TransformationException {
        // Check whether requested input xml exists and throw exception if not
        Path path = Paths.get("./src/main/resources", input);
        if(Files.notExists(path)) {
            throw new FileNotFoundException("requested input resource '" + input + "' does not exist");
        }

        integrator.requestAvailability(input, output);
    }
}
