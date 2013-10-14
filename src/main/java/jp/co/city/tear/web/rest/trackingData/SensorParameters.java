package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class SensorParameters {
    /**
     * 
     */
    @XmlElement(name = "FeatureDescriptorAlignment")
    public String featureDescriptorAlignment  = "regular"; //$NON-NLS-1$
    /**
     * 
     */
    @XmlElement(name = "MaxObjectsToDetectPerFrame")
    public int    maxObjectsToDetectPerFrame  = 5;
    /**
     * 
     */
    @XmlElement(name = "MaxObjectsToTrackInParallel")
    public int    maxObjectsToTrackInParallel = 1;
    /**
     * 
     */
    @XmlElement(name = "SimilarityThreshold")
    public float  similarityThreshold         = 0.7f;
}