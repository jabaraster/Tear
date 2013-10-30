/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase;

import java.util.Date;

import jp.co.city.tear.util.DateUtil;

/**
 * @param <E>
 * @author jabaraster
 */
public abstract class TearEntityBase<E extends EntityBase<E>> extends EntityBase<E> {
    private static final long serialVersionUID = 2245025228350578757L;

    /**
     * @see jabara.jpa.entity.EntityBase#getCreated()
     */
    @Override
    public Date getCreated() {
        return DateUtil.toApplicationTimeZone(this.created);
    }

    /**
     * @see jabara.jpa.entity.EntityBase#getUpdated()
     */
    @Override
    public Date getUpdated() {
        return DateUtil.toApplicationTimeZone(this.updated);
    }
}
