package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-08-12T19:03:41.487+0900")
@StaticMetamodel(EUserPassword.class)
public class EUserPassword_ extends EntityBase_ {
	public static volatile SingularAttribute<EUserPassword, byte[]> password;
	public static volatile SingularAttribute<EUserPassword, EUser> user;
}
