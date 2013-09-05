package jp.co.city.tear.service;

import jabara.general.NotFound;
import jabara.general.Sort;

import java.util.List;

import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.model.Duplicate;
import jp.co.city.tear.model.LoginUser;
import jp.co.city.tear.model.UnmatchPassword;
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
     * @param pLoginUser
     * @param pDeleteTargetUser
     * @return ログイン中ユーザが、pDeleteTargetUserを削除可能であればtrue.
     */
    boolean enableDelete(LoginUser pLoginUser, EUser pDeleteTargetUser);

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
     * @param pUser -
     * @param pPassword -
     * @throws Duplicate -
     */
    void insert(EUser pUser, String pPassword) throws Duplicate;

    /**
     * 
     */
    void insertAdministratorIfNotExists();

    /**
     * @param pUser -
     * @throws Duplicate -
     */
    void update(EUser pUser) throws Duplicate;

    /**
     * @param pUser -
     * @param pCurrentPassword -
     * @param pNewPassword -
     * @throws UnmatchPassword -
     */
    void updatePassword(EUser pUser, String pCurrentPassword, String pNewPassword) throws UnmatchPassword;
}
