package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-01-08T08:26:11.256+0900")
@StaticMetamodel(EDataStore.class)
public class EDataStore_ extends EntityBase_ {
	public static volatile SingularAttribute<EDataStore, Long> dataId;
	public static volatile SingularAttribute<EDataStore, byte[]> data;
}
