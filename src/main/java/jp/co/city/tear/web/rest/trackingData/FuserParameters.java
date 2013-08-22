package jp.co.city.tear.web.rest.trackingData;

import jabara.general.Empty;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class FuserParameters {
    /**
     * 
     */
    @XmlElement(name = "KeepPoseForNumberOfFrames")
    public int     keepPoseForNumberOfFrames                 = 2;
    /**
     * 
     */
    @XmlElement(name = "GravityAssistance")
    public String  gravityAssistance                         = Empty.STRING;
    /**
     * 
     */
    @XmlElement(name = "AlphaTranslation")
    public float   alphaTranslation                          = 0.8f;
    /**
     * 
     */
    @XmlElement(name = "GammaTranslation")
    public float   gammaTranslation                          = 0.8f;
    /**
     * 
     */
    @XmlElement(name = "AlphaRotation")
    public float   alphaRotation                             = 0.5f;
    /**
     * 
     */
    @XmlElement(name = "GammaRotation")
    public float   gammaRotation                             = 0.5f;
    /**
     * 
     */
    @XmlElement(name = "ContinueLostTrackingWithOrientationSensor")
    public boolean continueLostTrackingWithOrientationSensor = false;
}