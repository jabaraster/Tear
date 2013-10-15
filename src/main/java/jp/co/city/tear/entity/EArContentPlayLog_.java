package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase_;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-15T12:03:50.520+0900")
@StaticMetamodel(EArContentPlayLog.class)
public class EArContentPlayLog_ extends EntityBase_ {
	public static volatile SingularAttribute<EArContentPlayLog, Date> playDatetime;
	public static volatile SingularAttribute<EArContentPlayLog, Long> arContentId;
	public static volatile SingularAttribute<EArContentPlayLog, Double> latitude;
	public static volatile SingularAttribute<EArContentPlayLog, Double> longitude;
	public static volatile SingularAttribute<EArContentPlayLog, String> trackingDescriptor;
}
