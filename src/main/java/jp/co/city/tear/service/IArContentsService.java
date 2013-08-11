/**
 * 
 */
package jp.co.city.tear.service;

import jabara.general.NotFound;

import java.util.List;

import jp.co.city.tear.entity.EArContents;
import jp.co.city.tear.model.LoginUser;
import jp.co.city.tear.service.impl.ArContentsServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(ArContentsServiceImpl.class)
public interface IArContentsService {

    /**
     * @param pLoginUser -
     * @return -
     */
    long count(LoginUser pLoginUser);

    /**
     * @param pLoginUser -
     * @param pFirst -
     * @param pCount -
     * @return -
     */
    List<EArContents> find(LoginUser pLoginUser, long pFirst, long pCount);

    /**
     * @param pUser -
     * @param pId -
     * @return -
     * @throws NotFound -
     */
    EArContents findById(LoginUser pUser, long pId) throws NotFound;

    /**
     * @param pLoginUser -
     * @param pArContents -
     */
    void insertOrUpdate(LoginUser pLoginUser, EArContents pArContents);
}
