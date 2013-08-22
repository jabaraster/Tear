package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class SensorCOSParameters {
    /**
     * 
     */
    @XmlElement(name = "ReferenceImage")
    public final ReferenceImage referenceImage      = new ReferenceImage();
    /**
     * 
     */
    @XmlElement(name = "SimilarityThreshold")
    public float                similarityThreshold = 0.7f;
}