package jp.co.city.tear.service;

import jabara.general.NotFound;
import jabara.general.Sort;

import java.util.List;

import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.model.Duplicate;
import jp.co.city.tear.service.impl.UserServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * 
 */
@ImplementedBy(UserServiceImpl.class)
public interface IUserService {

    /**
     * @return -
     */
    long countAll();

    /**
     * @param pUser -
     */
    void delete(EUser pUser);

    /**
     * @param pId -
     * @return -
     * @throws NotFound -
     */
    EUser findById(long pId) throws NotFound;

    /**
     * @param pFirst -
     * @param pCount -
     * @param pSort -
     * @return -
     */
    List<EUser> get(long pFirst, long pCount, Sort pSort);

    /**
     * @param pSort ソート条件.
     * @return 全件.
     */
    List<EUser> getAll(Sort pSort);

    /**
     * 
     */
    void insertAdministratorIfNotExists();

    /**
     * @param pUser -
     * @param pPassword -
     * @throws Duplicate -
     */
    void insertOrUpdate(EUser pUser, String pPassword) throws Duplicate;
}
