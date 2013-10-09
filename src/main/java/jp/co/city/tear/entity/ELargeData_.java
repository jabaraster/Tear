package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-09T08:07:20.777+0900")
@StaticMetamodel(ELargeData.class)
public class ELargeData_ extends EntityBase_ {
	public static volatile SingularAttribute<ELargeData, Boolean> hasData;
	public static volatile SingularAttribute<ELargeData, String> dataName;
	public static volatile SingularAttribute<ELargeData, String> contentType;
	public static volatile SingularAttribute<ELargeData, Long> length;
}
