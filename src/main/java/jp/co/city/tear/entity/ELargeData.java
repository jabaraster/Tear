/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

/**
 * @author jabaraster
 */
@Entity
public class ELargeData extends EntityBase<ELargeData> {
    private static final long serialVersionUID         = -7306088955859680620L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_DATA_NAME = 1000;

    /**
     * 
     */
    @Column(nullable = false)
    protected boolean         hasData                  = false;

    /**
     * 
     */
    @Column(nullable = true, length = MAX_CHAR_COUNT_DATA_NAME * 3)
    protected String          dataName;

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
     * @return dataNameを返す.
     */
    public String getDataName() {
        return this.dataName;
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
    public String getType() {
        return StringUtils.substringAfterLast(this.dataName, "."); //$NON-NLS-1$
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

    /**
     * @param pDataName dataNameを設定.
     */
    public void setDataName(final String pDataName) {
        this.dataName = pDataName;
    }
}
