/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * @author jabaraster
 */
@Entity
public class ELargeData extends EntityBase<ELargeData> {
    private static final long serialVersionUID = -7306088955859680620L;

    /**
     * 
     */
    @Column(nullable = true)
    protected Integer         length;

    /**
     * 
     */
    @Transient
    protected InputStream     data;

    /**
     * 
     */
    public ELargeData() {
        // 処理なし
    }

    /**
     * @param pData -
     */
    public ELargeData(final InputStream pData) {
        setData(pData);
    }

    /**
     * @return the data
     */
    public InputStream getData() {
        return this.data;
    }

    /**
     * @return -
     */
    public int getLength() {
        return this.length.intValue();
    }

    /**
     * @param pData the data to set
     */
    public void setData(final InputStream pData) {
        this.data = pData;
    }

    /**
     * @param pLength -
     */
    public void setLength(final int pLength) {
        this.length = Integer.valueOf(pLength);
    }
}
