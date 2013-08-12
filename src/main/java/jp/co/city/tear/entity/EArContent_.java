package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-12T12:03:25.438+0900")
@StaticMetamodel(EArContent.class)
public class EArContent_ extends EntityBase_ {
	public static volatile SingularAttribute<EArContent, EUser> owner;
	public static volatile SingularAttribute<EArContent, String> title;
	public static volatile SingularAttribute<EArContent, ELargeData> marker;
	public static volatile SingularAttribute<EArContent, ELargeData> content;
}
