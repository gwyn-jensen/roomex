package com.roomex.assessment.transformation;

// Make this a checked exception as missing configuration would require an emergency fix
public class TransformationConfigurationException extends TransformationException {

    public TransformationConfigurationException(String msg) {
        super(msg);
    }
}
