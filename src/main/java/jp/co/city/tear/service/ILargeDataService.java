/**
 * 
 */
package jp.co.city.tear.service;

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
     * @param pData nullの場合は何も処理を行いません. <br>
     *            また、{@link ELargeData#getData()}がnullの場合も何も処理を行いません.
     */
    void insert(ELargeData pData);

}
