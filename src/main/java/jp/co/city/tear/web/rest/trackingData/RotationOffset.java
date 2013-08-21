package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class RotationOffset {
    @XmlElement(name = "X")
    int x = 0;
    @XmlElement(name = "Y")
    int y = 0;
    @XmlElement(name = "Z")
    int z = 0;
    @XmlElement(name = "W")
    int w = 1;
}