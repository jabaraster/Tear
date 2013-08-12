/**
 * 
 */
package jp.co.city.tear.service;

import jabara.general.NotFound;

import java.io.InputStream;

import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.impl.LargeDataServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(LargeDataServiceImpl.class)
public interface ILargeDataService {

    /**
     * @param pData nullの場合は何も処理を行いません.
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
     * @param pStream -
     */
    void insertOrUpdate(ELargeData pData, InputStream pStream);
}
