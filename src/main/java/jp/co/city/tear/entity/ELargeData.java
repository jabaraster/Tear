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
    private static final long serialVersionUID            = -7306088955859680620L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_DATA_NAME    = 1000;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_CONTENT_TYPE = 200;

    /**
     * 
     */
    @Column(nullable = false)
    protected boolean         hasData                     = false;

    /**
     * 
     */
    @Column(nullable = true, length = MAX_CHAR_COUNT_DATA_NAME * 3)
    protected String          dataName;

    /**
     * 
     */
    @Column(nullable = true, length = MAX_CHAR_COUNT_CONTENT_TYPE)
    protected String          contentType;

    /**
     * 
     */
    @Column(nullable = true)
    protected Long            length;

    /**
     * 
     */
    public void clearData() {
        this.length = null;
        this.contentType = null;
        this.hasData = false;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return this.contentType;
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
    public Long getLength() {
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
     * @param pContentType the contentType to set
     */
    public void setContentType(final String pContentType) {
        this.contentType = pContentType;
    }

    /**
     * @param pLength -
     */
    public void setDataLength(final long pLength) {
        this.hasData = true;
        this.length = Long.valueOf(pLength);
    }

    /**
     * @param pDataName dataNameを設定.
     */
    public void setDataName(final String pDataName) {
        this.dataName = pDataName;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "ELargeData [hasData=" + this.hasData + ", dataName=" + this.dataName + ", contentType=" + this.contentType + ", length="
                + this.length + ", id=" + this.id + "]";
    }
}
