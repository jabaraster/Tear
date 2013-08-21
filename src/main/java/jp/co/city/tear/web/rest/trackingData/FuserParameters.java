package jp.co.city.tear.web.rest.trackingData;

import jabara.general.Empty;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class FuserParameters {
    @XmlElement(name = "KeepPoseForNumberOfFrames")
    int     keepPoseForNumberOfFrames                 = 2;
    @XmlElement(name = "GravityAssistance")
    String  gravityAssistance                         = Empty.STRING;
    @XmlElement(name = "AlphaTranslation")
    float   alphaTranslation                          = 0.8f;
    @XmlElement(name = "GammaTranslation")
    float   gammaTranslation                          = 0.8f;
    @XmlElement(name = "AlphaRotation")
    float   alphaRotation                             = 0.5f;
    @XmlElement(name = "GammaRotation")
    float   gammaRotation                             = 0.5f;
    @XmlElement(name = "ContinueLostTrackingWithOrientationSensor")
    boolean continueLostTrackingWithOrientationSensor = false;
}