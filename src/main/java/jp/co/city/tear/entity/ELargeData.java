/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author jabaraster
 */
@Entity
public class ELargeData extends EntityBase<ELargeData> {
    private static final long serialVersionUID = -7306088955859680620L;

    /**
     * 
     */
    @Column(nullable = false)
    protected boolean         hasData          = false;

    /**
     * 
     */
    @Column(nullable = true)
    protected Integer         length;

    /**
     * 
     */
    public void clearData() {
        this.length = null;
        this.hasData = false;
    }

    /**
     * @return -
     */
    public Integer getLength() {
        return this.length;
    }

    /**
     * @return -
     */
    public boolean hasData() {
        return this.hasData;
    }

    /**
     * @param pLength -
     */
    public void setDataLength(final int pLength) {
        this.hasData = true;
        this.length = Integer.valueOf(pLength);
    }
}
