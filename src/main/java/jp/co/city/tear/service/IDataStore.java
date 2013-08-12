/**
 * 
 */
package jp.co.city.tear.service;

import jabara.general.NotFound;

import java.io.InputStream;
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
     * @return -
     * @throws NotFound -
     */
    InputStream getDataInputStream(ELargeData pData) throws NotFound;

    /**
     * @param pData -
     * @return データ長.
     */
    int save(ELargeData pData);
}
