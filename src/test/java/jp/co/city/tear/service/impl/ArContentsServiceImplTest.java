/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.entity.EArContents;
import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.model.LoginUser;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class ArContentsServiceImplTest {

    /**
     * 
     */
    @Rule
    public final JpaDaoRule<ArContentsServiceImpl> tool = new JpaDaoRule<ArContentsServiceImpl>() {
                                                            @SuppressWarnings("synthetic-access")
                                                            @Override
                                                            protected ArContentsServiceImpl createService(
                                                                    final EntityManagerFactory pEntityManagerFactory) {
                                                                return createServiceCore(pEntityManagerFactory);
                                                            }
                                                        };

    /**
     * 
     */
    @Test
    public void _update() {
        final EUser loginUser = getAdministratorUser();
        final EArContents ac = new EArContents();
        ac.setTitle("title"); //$NON-NLS-1$

        this.tool.getSut().insert(new LoginUser(loginUser), ac);

        this.tool.getEntityManager().clear();

        this.tool.getSut().insert(new LoginUser(loginUser), ac);
    }

    private EUser getAdministratorUser() {
        final EntityManager em = this.tool.getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUser> query = builder.createQuery(EUser.class);
        final Root<EUser> root = query.from(EUser.class);

        query.where(builder.equal(root.get(EUser_.administrator), Boolean.valueOf(true)));

        try {
            final EUser ret = JpaDaoBase.getSingleResult(em.createQuery(query));
            em.clear();
            return ret;
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @throws NamingException -
     */
    @BeforeClass
    public static void beforeClass() throws NamingException {
        WebStarter.initializeDataSource();
    }

    private static ArContentsServiceImpl createServiceCore(final EntityManagerFactory pEntityManagerFactory) {
        final UserServiceImpl userService = new UserServiceImpl(pEntityManagerFactory);
        userService.insertAdministratorIfNotExists();
        return new ArContentsServiceImpl( //
                pEntityManagerFactory //
                , userService //
                , new LargeDataServiceImpl(pEntityManagerFactory, new FileDataStore()) //
        );
    }
}
