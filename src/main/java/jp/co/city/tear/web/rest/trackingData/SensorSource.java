package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class SensorSource {
    /**
     * 
     */
    @XmlElement(name = "SensorID")
    public String        sensorID           = "FeatureTracking1"; //$NON-NLS-1$
    /**
     * 
     */
    @XmlElement(name = "SensorCosID")
    public String        sensorCosID        = "Patch";           //$NON-NLS-1$
    /**
     * 
     */
    @XmlElement(name = "HandEyeCalibration")
    public final Offsets handEyeCalibration = new Offsets();
    /**
     * 
     */
    @XmlElement(name = "COSOffset")
    public final Offsets cosOffset          = new Offsets();
}