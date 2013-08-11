package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-10T02:44:33.829+0900")
@StaticMetamodel(EArContents.class)
public class EArContents_ extends EntityBase_ {
	public static volatile SingularAttribute<EArContents, ELargeData> marker;
	public static volatile SingularAttribute<EArContents, ELargeData> contents;
	public static volatile SingularAttribute<EArContents, String> title;
	public static volatile SingularAttribute<EArContents, EUser> owner;
}
