package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class SensorCOSParameters {
    @XmlElement(name = "ReferenceImage")
    ReferenceImage referenceImage      = new ReferenceImage();
    @XmlElement(name = "SimilarityThreshold")
    float          similarityThreshold = 0.7f;
}