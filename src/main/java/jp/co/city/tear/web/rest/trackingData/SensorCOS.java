package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;


/**
 * @author jabaraster
 */
public class SensorCOS {
    @XmlElement
    String              sensorCosID = "Patch";                  //$NON-NLS-1$
    @XmlElement(name = "Parameters")
    SensorCOSParameters parameters  = new SensorCOSParameters();
}