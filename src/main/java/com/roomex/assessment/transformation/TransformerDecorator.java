package com.roomex.assessment.transformation;

import javax.xml.transform.Transformer;

public interface TransformerDecorator {
    void decorate(Transformer transformer);
}
