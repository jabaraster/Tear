package jp.co.city.tear.web.rest.trackingData;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author jabaraster
 */
public class Sensor {
    @XmlElement
    String           SensorID  = "FeatureTracking1";        //$NON-NLS-1$
    @XmlAttribute(name = "Type")
    String           type      = "FeatureBasedSensorSource"; //$NON-NLS-1$
    @XmlAttribute(name = "Subtype")
    String           subtype   = "Fast";                    //$NON-NLS-1$
    @XmlElement(name = "Parameters")
    SensorParameters parmeters = new SensorParameters();
    @XmlElement(name = "SensorCOS")
    List<SensorCOS>  sensorCOS = new ArrayList<>();
}