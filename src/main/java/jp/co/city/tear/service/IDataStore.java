/**
 * 
 */
package jp.co.city.tear.service;

import java.io.Serializable;

import jp.co.city.tear.entity.ELargeData;

/**
 * @author jabaraster
 */
public interface IDataStore extends Serializable {
    /**
     * @param pData -
     */
    void delete(ELargeData pData);

    /**
     * @param pData -
     * @return データ長.
     */
    int save(ELargeData pData);
}
