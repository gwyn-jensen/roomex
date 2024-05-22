package com.roomex.assessment.transformation;

// This class represents the immutable partnership between the stylesheet and the java class that executes it
public record TransformationContext(String xslt, TransformerDecorator decorator) {}