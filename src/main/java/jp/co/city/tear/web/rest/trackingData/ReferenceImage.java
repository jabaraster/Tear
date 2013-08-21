package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author jabaraster
 */
public class ReferenceImage {
    @XmlAttribute(name = "WidthMM")
    int    widthMM  = 80;
    @XmlAttribute(name = "HeightMM")
    int    heightMM = 80;
    @XmlValue
    String name     = "image"; //$NON-NLS-1$
}