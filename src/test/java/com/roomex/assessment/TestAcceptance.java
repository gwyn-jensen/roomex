package com.roomex.assessment;

import com.roomex.assessment.business.BusinessService;
import com.roomex.assessment.business.Service;
import com.roomex.assessment.integration.PartnerIntegrator;
import com.roomex.assessment.integration.PayloadTransformer;
import com.roomex.assessment.transformation.TransformationConfigurationException;
import com.roomex.assessment.transformation.TransformationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestAcceptance {

    // The message id is a random uuid, so it and fields derived from it will change with each transformation
    // and need to be excluded from comparisons
    // The created date is today's date and so also needs to be excluded
    private List<String> ignores = Arrays.asList("add:MessageID", "oas:Nonce", "oas:Password", "oas1:Created");

    @AfterEach
    public void teardown() throws IOException {
        Path path = Paths.get("./acceptance-test.xml");
        Files.deleteIfExists(path);
    }

    @Test
    public void testSuccess() throws IOException, TransformationException {

        PartnerIntegrator integrator = new PayloadTransformer();
        Service svc = new BusinessService(integrator);
        svc.requestAvailability("input.xml", "acceptance-test.xml");

        InputStream exp = TestAcceptance.class.getResourceAsStream("/output.xml");
        InputStream act = new FileInputStream("./acceptance-test.xml");

        //There are certain unavoidable differences
        Diff diff = DiffBuilder.compare(exp).withTest(act)
                .checkForIdentical()
                .withNodeFilter(node -> !ignores.contains(node.getNodeName()))
                .build();

        assertFalse(diff.hasDifferences());
    }

    @Test
    public void testInputFileNotFound() {

        PartnerIntegrator integrator = new PayloadTransformer();
        Service svc = new BusinessService(integrator);
        FileNotFoundException fnfe = assertThrows(FileNotFoundException.class,
                () -> svc.requestAvailability("missing.xml", "acceptance-test.xml"));

        assertEquals("requested input resource 'missing.xml' does not exist", fnfe.getMessage());
    }

    @Test
    public void testStyleSheetNotFound() {

        PartnerIntegrator integrator = new PayloadTransformer();
        Service svc = new BusinessService(integrator);
        TransformationConfigurationException tce = assertThrows(TransformationConfigurationException.class,
                () -> svc.requestAvailability("test.xml", "acceptance-test.xml"));

        assertEquals("stylesheet missing.xslt is missing", tce.getMessage());
    }
}
