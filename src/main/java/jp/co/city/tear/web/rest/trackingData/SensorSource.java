package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class SensorSource {
    @XmlElement(name = "SensorID")
    String  sensorID           = "FeatureTracking1"; //$NON-NLS-1$
    @XmlElement(name = "SensorCosID")
    String  sensorCosID        = "Patch";           //$NON-NLS-1$
    @XmlElement(name = "HandEyeCalibration")
    Offsets handEyeCalibration = new Offsets();
    @XmlElement(name = "COSOffset")
    Offsets cosOffset          = new Offsets();
}