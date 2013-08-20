/**
 * 
 */
package jp.co.city.tear.web;

import jabara.general.ArgUtil;

import javax.servlet.http.HttpSession;

import jp.co.city.tear.model.LoginUser;

/**
 * @author jabaraster
 */
public class LoginUserHolder {

    private static final String KEY = LoginUserHolder.class.getName();

    /**
     * @param pSession -
     * @return -
     */
    public static LoginUser get(final HttpSession pSession) {
        ArgUtil.checkNull(pSession, "pSession"); //$NON-NLS-1$

        final LoginUser ret = (LoginUser) pSession.getAttribute(KEY);
        if (ret == null) {
            throw new IllegalStateException("未ログインのようです."); //$NON-NLS-1$
        }
        return ret;
    }

    /**
     * @param pSession -
     * @return -
     */
    public static boolean isLoggedin(final HttpSession pSession) {
        ArgUtil.checkNull(pSession, "pSession"); //$NON-NLS-1$
        return pSession.getAttribute(KEY) != null;
    }

    /**
     * @param pSession -
     * @param pLoginUser -
     */
    public static void set(final HttpSession pSession, final LoginUser pLoginUser) {
        ArgUtil.checkNull(pSession, "pSession"); //$NON-NLS-1$
        ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$
        pSession.setAttribute(KEY, pLoginUser);
    }
}
