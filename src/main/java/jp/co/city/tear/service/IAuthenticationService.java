/**
 * 
 */
package jp.co.city.tear.service;

import jp.co.city.tear.model.FailAuthentication;
import jp.co.city.tear.model.LoginUser;
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
    LoginUser login(String pUserId, String pPassword) throws FailAuthentication;
}
