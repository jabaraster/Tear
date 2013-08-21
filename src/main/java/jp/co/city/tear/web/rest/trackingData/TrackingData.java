package jp.co.city.tear.web.rest.trackingData;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jabaraster
 */
@XmlRootElement
public class TrackingData {
    @XmlElement(name = "Sensor")
    @XmlElementWrapper(name = "Sensors")
    List<Sensor>     sensors     = new ArrayList<>();
    {
        final Sensor sensor = new Sensor();
        this.sensors.add(sensor);
    }

    @XmlElement(name = "COS")
    @XmlElementWrapper(name = "Connections")
    List<Connection> connections = new ArrayList<>();
}