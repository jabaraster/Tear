/**
 * 
 */
package jp.co.city.tear.model;

import jabara.general.ArgUtil;

import java.io.Serializable;

import jp.co.city.tear.entity.EUser;

/**
 * @author jabaraster
 */
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 2677431235987673999L;

    private final long        id;
    private final String      userId;
    private final boolean     administrator;

    /**
     * @param pUser -
     */
    public LoginUser(final EUser pUser) {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
        this.id = pUser.getId().longValue();
        this.userId = pUser.getUserId();
        this.administrator = pUser.isAdministrator();
    }

    /**
     * @param pUser
     * @return pUserがこのオブジェクトと同じユーザ情報を示している場合はtrue.
     */
    public boolean equal(final EUser pUser) {
        if (!pUser.isPersisted()) {
            return false;
        }
        return pUser.getId().longValue() == this.id;
    }

    /**
     * @return idを返す.
     */
    public long getId() {
        return this.id;
    }

    /**
     * @return userIdを返す.
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * @return administratorを返す.
     */
    public boolean isAdministrator() {
        return this.administrator;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "LoginUser [id=" + this.id + ", userId=" + this.userId + ", administrator=" + this.administrator + "]";
    }

}
