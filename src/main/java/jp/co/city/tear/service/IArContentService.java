/**
 * 
 */
package jp.co.city.tear.service;

import jabara.general.NotFound;
import jabara.general.Sort;

import java.io.InputStream;
import java.util.List;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.model.LoginUser;
import jp.co.city.tear.service.impl.ArContentServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(ArContentServiceImpl.class)
public interface IArContentService {

    /**
     * @param pLoginUser -
     * @return -
     */
    long count(LoginUser pLoginUser);

    /**
     * @param pArContent -
     */
    void delete(EArContent pArContent);

    /**
     * @param pLoginUser -
     * @param pFirst -
     * @param pCount -
     * @param pSort -
     * @return -
     */
    List<EArContent> find(LoginUser pLoginUser, long pFirst, long pCount, Sort pSort);

    /**
     * @param pUser -
     * @param pId -
     * @return -
     * @throws NotFound -
     */
    EArContent findById(LoginUser pUser, long pId) throws NotFound;

    /**
     * @param pData -
     * @return -
     * @throws NotFound -
     */
    InputStream getDataInputStream(ELargeData pData) throws NotFound;

    /**
     * @param pLoginUser -
     * @param pArContents -
     */
    void insertOrUpdate(LoginUser pLoginUser, EArContent pArContents);
}