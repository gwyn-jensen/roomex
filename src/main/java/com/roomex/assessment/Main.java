package com.roomex.assessment;

import com.roomex.assessment.business.Service;
import com.roomex.assessment.integration.PayloadTransformer;
import com.roomex.assessment.business.BusinessService;
import com.roomex.assessment.integration.PartnerIntegrator;
import com.roomex.assessment.transformation.TransformationException;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

        if(args.length != 2) {
            throw new IllegalArgumentException("please provide the name of the input xml file followed by the name of the output xml file as parameters");
        }

        for(String arg: args) {
            if(arg.isBlank()) {
                throw new IllegalArgumentException("neither parameter can be blank");
            }
        }

        try {
            PartnerIntegrator integrator = new PayloadTransformer();
            Service svc = new BusinessService(integrator);
            svc.requestAvailability(args[0], args[1]);
        } catch (FileNotFoundException | TransformationException e) {
            // Wrap in a RuntimeException, so we don't need to add to method signature
            // Production grade code would have a mechanism for catching and logging all exceptions that
            // escape the application boundary
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("transformation completed successfully");
        System.out.println("please view your result at roomex/" + args[1]);
    }
}
