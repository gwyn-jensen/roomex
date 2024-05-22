package com.roomex.assessment.integration;

import com.roomex.assessment.transformation.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class PayloadTransformer implements PartnerIntegrator {

    public void requestAvailability(String input, String output) throws TransformationException {

        TransformationContext ctx = TransformationContextFactory.getTransformationContext(input);

        try(InputStream is = this.getClass().getResourceAsStream("/" + input);
            InputStream xsltStream = this.getClass().getResourceAsStream("/" + ctx.xslt())) {

            // The 'is' input stream cannot be null because we confirmed its existence in the BusinessService
            if(xsltStream == null) {
                throw new TransformationConfigurationException("stylesheet " + ctx.xslt() + " is missing");
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Source xsltSource = new StreamSource(xsltStream);
            Templates templates = transformerFactory.newTemplates(xsltSource);
            Transformer transformer = templates.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // decorate the transformer with parameters specific to chosen transformation
            ctx.decorator().decorate(transformer);

            Source xmlSource = new StreamSource(is);

            StreamResult result = new StreamResult(new File(output));
            transformer.transform(xmlSource, result);

        } catch (IOException | TransformerException e) {
            throw new TransformationProcessingException(e);
        }
    }
}
