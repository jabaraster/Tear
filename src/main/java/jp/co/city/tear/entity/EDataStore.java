/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @see jp.co.city.tear.service.impl.LobDataStore
 * @author jabaraster
 */
@Entity
public class EDataStore extends EntityBase<EDataStore> {
    private static final long serialVersionUID = -4756831992138712638L;

    /**
     * 
     */
    protected long            dataId;

    /**
     * 
     */
    @Lob
    protected byte[]          data             = {};

    /**
     * @return dataを返す.
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * @return dataIdを返す.
     */
    public long getDataId() {
        return this.dataId;
    }

    /**
     * @param pData dataを設定.
     */
    public void setData(final byte[] pData) {
        this.data = pData;
    }

    /**
     * @param pDataId dataIdを設定.
     */
    public void setDataId(final long pDataId) {
        this.dataId = pDataId;
    }

}
