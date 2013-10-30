/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.general.Empty;
import jabara.general.ExceptionUtil;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author jabaraster
 */
@Entity
public class EUserPassword extends TearEntityBase<EUserPassword> {
    private static final long          serialVersionUID = 7885103086152737376L;

    private static final Charset       ENCODING         = Charset.forName("utf-8"); //$NON-NLS-1$
    private static final MessageDigest DIGESTER         = getMessageDigest();

    /**
     * 
     */
    @Column(nullable = false)
    protected byte[]                   password         = {};
    /**
     * 
     */
    @OneToOne
    @JoinColumn(nullable = false)
    protected EUser                    user;

    /**
     * 
     */
    public EUserPassword() {
        // 処理なし
    }

    /**
     * @param pUser -
     * @param pPassword -
     */
    public EUserPassword(final EUser pUser, final String pPassword) {
        setUser(pUser);
        setPassword(pPassword);
    }

    /**
     * @param pPassword -
     * @return -
     */
    public boolean equal(final String pPassword) {
        return Arrays.equals(this.password, digest(pPassword));
    }

    /**
     * @return userを返す.
     */
    public EUser getUser() {
        return this.user;
    }

    /**
     * @param pPassword -
     */
    public void setPassword(final String pPassword) {
        this.password = digest(pPassword);
    }

    /**
     * @param pUser userを設定.
     */
    public void setUser(final EUser pUser) {
        this.user = pUser;
    }

    private static byte[] digest(final String pValue) {
        final byte[] value = pValue == null ? Empty.BYTE_ARRAY : pValue.getBytes(ENCODING);
        return DIGESTER.digest(value);
    }

    private static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5"); //$NON-NLS-1$
        } catch (final NoSuchAlgorithmException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }
}
