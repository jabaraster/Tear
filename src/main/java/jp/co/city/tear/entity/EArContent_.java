package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-10-11T14:47:15.006+0900")
@StaticMetamodel(EArContent.class)
public class EArContent_ extends EntityBase_ {
	public static volatile SingularAttribute<EArContent, EUser> owner;
	public static volatile SingularAttribute<EArContent, String> title;
	public static volatile SingularAttribute<EArContent, ELargeData> marker;
	public static volatile SingularAttribute<EArContent, ELargeData> content;
	public static volatile SingularAttribute<EArContent, Float> similarityThreshold;
}
