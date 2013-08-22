package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class SensorParameters {
    /**
     * 
     */
    @XmlElement
    public String FeatureDescriptorAlignment  = "regular"; //$NON-NLS-1$
    /**
     * 
     */
    @XmlElement
    public int    MaxObjectsToDetectPerFrame  = 5;
    /**
     * 
     */
    @XmlElement
    public int    MaxObjectsToTrackInParallel = 1;
    /**
     * 
     */
    @XmlElement
    public float  SimilarityThreshold         = 0.7f;
}