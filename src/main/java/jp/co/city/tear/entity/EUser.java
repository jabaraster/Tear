/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.bean.BeanProperties;
import jabara.bean.annotation.Hidden;
import jabara.bean.annotation.Localized;
import jabara.bean.annotation.Order;
import jabara.jpa.entity.EntityBase;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jabaraster
 */
@Entity
public class EUser extends EntityBase<EUser> {
    private static final long           serialVersionUID               = 5322511553248558567L;

    private static final BeanProperties _properties                    = BeanProperties.getInstance(EUser.class);

    /**
     * 
     */
    public static final String          DEFAULT_ADMINISTRATOR_USER_ID  = "admin";                                //$NON-NLS-1$

    /**
     * 
     */
    public static final String          DEFAULT_ADMINISTRATOR_PASSWORD = "admin";                                //$NON-NLS-1$
    /**
     * 
     */
    public static final int             MAX_CHAR_COUNT_USER_ID         = 50;

    /**
     * 
     */
    @Column(nullable = false, unique = true, length = MAX_CHAR_COUNT_USER_ID)
    @NotNull
    @Size(min = 1, max = MAX_CHAR_COUNT_USER_ID)
    protected String                    userId;

    /**
     * 
     */
    @Column(nullable = false)
    protected boolean                   administrator;

    /**
     * @see jabara.jpa.entity.EntityBase#getCreated()
     */
    @Override
    @Localized
    @Hidden
    public Date getCreated() {
        return super.getCreated();
    }

    /**
     * @see jabara.jpa.entity.EntityBase#getId()
     */
    @Override
    @Hidden
    public Long getId() {
        return super.getId();
    }

    /**
     * @see jabara.jpa.entity.EntityBase#getUpdated()
     */
    @Override
    @Localized
    @Hidden
    public Date getUpdated() {
        return super.getUpdated();
    }

    /**
     * @return userIdを返す.
     */
    @Order(100)
    @Localized
    public String getUserId() {
        return this.userId;
    }

    /**
     * @return administratorを返す.
     */
    @Order(150)
    @Localized
    public boolean isAdministrator() {
        return this.administrator;
    }

    /**
     * @see jabara.jpa.entity.EntityBase#isPersisted()
     */
    @Override
    @Hidden
    public boolean isPersisted() {
        return super.isPersisted();
    }

    /**
     * @param pAdministrator administratorを設定.
     */
    public void setAdministrator(final boolean pAdministrator) {
        this.administrator = pAdministrator;
    }

    /**
     * @param pUserId userIdを設定.
     */
    public void setUserId(final String pUserId) {
        this.userId = pUserId;
    }

    /**
     * @return -
     */
    public static BeanProperties getMeta() {
        return _properties;
    }
}
