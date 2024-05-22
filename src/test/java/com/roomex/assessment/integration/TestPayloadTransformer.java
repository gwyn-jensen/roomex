package com.roomex.assessment.integration;

import com.roomex.assessment.transformation.TransformationConfigurationException;
import com.roomex.assessment.transformation.TransformationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class TestPayloadTransformer {

    @AfterEach
    public void teardown() throws IOException {
        Path path = Paths.get("output.xml");
        Files.deleteIfExists(path);
    }

    @Test
    public void testRequestAvailability() throws TransformationException {

        PayloadTransformer integrator = new PayloadTransformer();
        integrator.requestAvailability("input.xml", "output.xml");

        //At this point we will just test the existence of the output file
        //Acceptance tests can assert the content
        Path path = Paths.get("output.xml");
        assertTrue(Files.exists(path));
    }

    @Test
    public void testRequestAvailabilityFailsToFindTransformationContext() {

        PayloadTransformer integrator = new PayloadTransformer();

        TransformationConfigurationException tce = assertThrows(TransformationConfigurationException.class,
                () -> integrator.requestAvailability("missing.xml", "output.xml"));

        assertEquals("input file exists but transformation configuration is missing", tce.getMessage());
    }

    @Test
    public void testRequestAvailabilityFailsToOpenInputResource() {

        PayloadTransformer integrator = new PayloadTransformer();

        TransformationConfigurationException tce = assertThrows(TransformationConfigurationException.class,
                () -> integrator.requestAvailability("test.xml", "output.xml"));

        assertEquals("stylesheet missing.xslt is missing", tce.getMessage());
    }
}
