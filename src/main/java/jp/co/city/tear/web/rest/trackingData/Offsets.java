package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class Offsets {
    /**
     * 
     */
    @XmlElement(name = "TranslationOffset")
    public final TranslationOffset translationOffset = new TranslationOffset();
    /**
     * 
     */
    @XmlElement(name = "RotationOffset")
    public final RotationOffset    rotationOffset    = new RotationOffset();
}