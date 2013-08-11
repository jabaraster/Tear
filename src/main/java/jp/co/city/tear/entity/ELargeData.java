/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.general.ArgUtil;
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
    private static final long       serialVersionUID = -7306088955859680620L;

    /**
     * 
     */
    @Transient
    protected transient InputStream data;

    /**
     * 
     */
    @Column(nullable = true)
    protected Integer               length;

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
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$
        setData(pData);
    }

    /**
     * @return -
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
     * @param pData dataを設定.
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
