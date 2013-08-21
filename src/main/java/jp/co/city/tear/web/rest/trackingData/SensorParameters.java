package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class SensorParameters {
    @XmlElement
    String FeatureDescriptorAlignment  = "regular"; //$NON-NLS-1$
    @XmlElement
    int    MaxObjectsToDetectPerFrame  = 5;
    @XmlElement
    int    MaxObjectsToTrackInParallel = 1;
    @XmlElement
    float  SimilarityThreshold         = 0.7f;
}