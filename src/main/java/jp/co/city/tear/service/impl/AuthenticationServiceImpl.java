/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.city.tear.entity.EUserPassword;
import jp.co.city.tear.entity.EUserPassword_;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.model.FailAuthentication;
import jp.co.city.tear.service.IAuthenticationService;

/**
 * @author jabaraster
 */
public class AuthenticationServiceImpl extends JpaDaoBase implements IAuthenticationService {
    private static final long serialVersionUID = 6002856896981032655L;

    /**
     * @param pEntityManagerFactory -
     */
    @Inject
    public AuthenticationServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see jp.co.city.tear.service.IAuthenticationService#login(java.lang.String, java.lang.String)
     */
    @Override
    public AuthenticatedAs login(final String pUserId, final String pPassword) throws FailAuthentication {
        ArgUtil.checkNullOrEmpty(pUserId, "pUserId"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUserPassword> query = builder.createQuery(EUserPassword.class);
        final Root<EUserPassword> root = query.from(EUserPassword.class);

        query.distinct(true);
        root.fetch(EUserPassword_.user);

        query.where( //
        builder.equal(root.get(EUserPassword_.user).get(EUser_.userId), pUserId) //
        );

        try {
            final EUserPassword member = getSingleResult(em.createQuery(query));
            if (!member.equal(pPassword)) {
                throw FailAuthentication.INSTANCE;
            }

            return member.getUser().isAdministrator() ? AuthenticatedAs.ADMINISTRATOR : AuthenticatedAs.NORMAL_USER;

        } catch (final NotFound e) {
            throw FailAuthentication.INSTANCE;
        }
    }
}
