package jp.co.city.tear.web.rest.trackingData;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author jabaraster
 */
public class Fuser {
    @XmlAttribute(name = "Type")
    Type            type       = Type.SmoothingFuser;
    @XmlElement(name = "Parameters")
    FuserParameters parameters = new FuserParameters();

    /**
     * @author jabaraster
     */
    public enum Type {
        /**
         * 
         */
        SmoothingFuser,
        /**
         * 
         */
        BestQualityFuser, ;
    }
}