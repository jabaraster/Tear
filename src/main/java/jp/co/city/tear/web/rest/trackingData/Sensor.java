package jp.co.city.tear.web.rest.trackingData;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class Sensor {
    /**
     * 
     */
    @XmlElement
    public String                 SensorID  = "FeatureTracking1";        //$NON-NLS-1$
    /**
     * 
     */
    @XmlAttribute(name = "ButtonType")
    public String                 type      = "FeatureBasedSensorSource"; //$NON-NLS-1$
    /**
     * 
     */
    @XmlAttribute(name = "Subtype")
    public String                 subtype   = "Fast";                    //$NON-NLS-1$
    /**
     * 
     */
    @XmlElement(name = "Parameters")
    public final SensorParameters parmeters = new SensorParameters();
    /**
     * 
     */
    @XmlElement(name = "SensorCOS")
    public final List<SensorCOS>  sensorCOS = new ArrayList<>();
}