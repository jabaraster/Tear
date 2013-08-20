/**
 * 
 */
package jp.co.city.tear.service;

import java.io.File;

import jp.co.city.tear.service.impl.TempFileServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(TempFileServiceImpl.class)
public interface ITempFileService {

    /**
     * 
     */
    void cleanUp();

    /**
     * @param pPrefix -
     * @param pSuffix -
     * @return -
     */
    File create(Class<?> pPrefix, String pSuffix);

    /**
     * @param pPrefix -
     * @param pSuffix -
     * @return -
     */
    File create(String pPrefix, String pSuffix);
}
