/**
 * 
 */
package jp.co.city.tear.service;

import jp.co.city.tear.model.FailAuthentication;
import jp.co.city.tear.service.impl.AuthenticationServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(AuthenticationServiceImpl.class)
public interface IAuthenticationService {

    /**
     * @param pUserId
     * @param pPassword
     * @return -
     * @throws FailAuthentication
     */
    AuthenticatedAs login(String pUserId, String pPassword) throws FailAuthentication;

    /**
     * @author jabaraster
     */
    public enum AuthenticatedAs {

        /**
         * 通常ユーザとして認証済み.
         */
        NORMAL_USER,

        /**
         * 管理者ユーザとして認証済み.
         */
        ADMINISTRATOR, ;
    }

}
