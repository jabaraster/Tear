/**
 * 
 */
package jp.co.city.tear.service;

import java.util.List;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.service.impl.ArContentPlayLogServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(ArContentPlayLogServiceImpl.class)
public interface IArContentPlayLogService {

    /**
     * @return -
     */
    String createDescriptor();

    /**
     * @param pFirst -
     * @param pCount -
     * @return -
     */
    List<EArContentPlayLog> get(int pFirst, int pCount);

    /**
     * @param pLog -
     */
    void insert(ArContentPlayLog pLog);
}
