package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class RotationOffset {
    /**
     * 
     */
    @XmlElement(name = "X")
    public int x = 0;
    /**
     * 
     */
    @XmlElement(name = "Y")
    public int y = 0;
    /**
     * 
     */
    @XmlElement(name = "Z")
    public int z = 0;
    /**
     * 
     */
    @XmlElement(name = "W")
    public int w = 1;
}