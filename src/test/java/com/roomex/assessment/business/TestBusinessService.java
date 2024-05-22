package com.roomex.assessment.business;

import com.roomex.assessment.integration.PartnerIntegrator;
import com.roomex.assessment.transformation.TransformationConfigurationException;
import com.roomex.assessment.transformation.TransformationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestBusinessService {

    @Test
    public void testRequestAvailability() throws FileNotFoundException, TransformationException {

        PartnerIntegrator integrator = Mockito.mock(PartnerIntegrator.class);
        Mockito.doNothing().when(integrator).requestAvailability("input.xml", "output.xml");

        BusinessService service = new BusinessService(integrator);
        service.requestAvailability("input.xml", "output.xml");

        Mockito.verify(integrator).requestAvailability("input.xml", "output.xml");
    }

    @Test
    public void testRequestAvailabilityWithUnknownResource() {

        BusinessService service = new BusinessService(null);

        FileNotFoundException fnfe = assertThrows(FileNotFoundException.class,
                () -> service.requestAvailability("UNKNOWN", "output.xml"));

        assertEquals("requested input resource 'UNKNOWN' does not exist", fnfe.getMessage());
    }

    @Test
    public void testRequestAvailabilityWithMissingTransformationConfiguration() throws TransformationException {

        PartnerIntegrator integrator = Mockito.mock(PartnerIntegrator.class);
        Mockito.doThrow(new TransformationConfigurationException("fail")).when(integrator).requestAvailability("input.xml", "output.xml");
        BusinessService service = new BusinessService(integrator);

        TransformationConfigurationException tce = assertThrows(TransformationConfigurationException.class,
                () -> service.requestAvailability("input.xml", "output.xml"));

        assertEquals("fail", tce.getMessage());
    }
}
