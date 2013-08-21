package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;


/**
 * @author jabaraster
 */
public class Connection {
    /**
     * 
     */
    @XmlElement(name = "Name")
    public String       name         = "MarkerlessCOS";   //$NON-NLS-1$
    /**
     * 
     */
    @XmlElement(name = "Fuser")
    public Fuser        fuser        = new Fuser();
    /**
     * 
     */
    @XmlElement(name = "SensorSource")
    public SensorSource sensorSource = new SensorSource();
}