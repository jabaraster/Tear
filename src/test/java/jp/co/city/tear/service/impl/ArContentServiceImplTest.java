/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;

import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.WebStarter.Mode;
import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.model.Duplicate;
import jp.co.city.tear.model.LargeDataOperation;
import jp.co.city.tear.model.LoginUser;
import jp.co.city.tear.service.IUserService;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import static org.hamcrest.core.Is.is;

/**
 * @author jabaraster
 */
@RunWith(Enclosed.class)
@SuppressWarnings("synthetic-access")
public class ArContentServiceImplTest {

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

    private static ArContentServiceImpl createServiceCore(final EntityManagerFactory pEntityManagerFactory) {
        final UserServiceImpl userService = new UserServiceImpl(pEntityManagerFactory);
        userService.insertAdministratorIfNotExists();
        return new ArContentServiceImpl( //
                pEntityManagerFactory //
                , userService //
                , new LargeDataServiceImpl(pEntityManagerFactory, new FileDataStore()) //
        );
    }

    private static IUserService createUserService(final EntityManagerFactory pEntityManagerFactory) {
        return new UserServiceImpl(pEntityManagerFactory);
    }

    private static LoginUser getAdministratorUser(final EntityManager em) {
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUser> query = builder.createQuery(EUser.class);
        final Root<EUser> root = query.from(EUser.class);

        query.where(builder.equal(root.get(EUser_.administrator), Boolean.valueOf(true)));

        try {
            final EUser ret = JpaDaoBase.getSingleResult(em.createQuery(query));
            em.clear();
            return new LoginUser(ret);

        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static LargeDataOperation getContentData() {
        final InputStream in = getResourceAsStream("AR01_content.jpg"); //$NON-NLS-1$
        return new LargeDataOperation().update(in);
    }

    private static LargeDataOperation getMarkerData() {
        final InputStream in = getResourceAsStream("AR01_marker.jpg"); //$NON-NLS-1$
        return new LargeDataOperation().update(in);
    }

    private static InputStream getResourceAsStream(final String pString) {
        final InputStream ret = ArContentServiceImplTest.class.getResourceAsStream(pString);
        if (ret == null) {
            throw new IllegalStateException();
        }
        return ret;
    }

    /**
     * @author jabaraster
     */
    public static class Other {
        /**
         * 
         */
        @Rule
        public JpaDaoRule<ArContentServiceImpl> rule = new JpaDaoRule<ArContentServiceImpl>() {
                                                         @Override
                                                         protected ArContentServiceImpl createService(final EntityManagerFactory pEntityManagerFactory) {
                                                             return createServiceCore(pEntityManagerFactory);
                                                         }
                                                     };

        /**
         * @throws NotFound -
         */
        @SuppressWarnings("boxing")
        @Test
        public void _insert() throws NotFound {
            final EntityManager em = this.rule.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final EArContent ac = new EArContent();
            ac.setTitle("title"); //$NON-NLS-1$
            this.rule.getSut().insertOrUpdate(loginUser, ac, getMarkerData(), getContentData());

            em.clear();

            final EArContent inDb = this.rule.getSut().findById(loginUser, ac.getId().longValue());

            assertThat(inDb.getMarker().hasData(), is(true));
            assertThat(inDb.getContent().hasData(), is(true));
        }
    }

    /**
     * @author jabaraster
     */
    public static class TableRowCount_is_0 {
        /**
         * 
         */
        @Rule
        public final JpaDaoRule<ArContentServiceImpl> tool = new JpaDaoRule<ArContentServiceImpl>() {
                                                               @Override
                                                               protected ArContentServiceImpl createService(
                                                                       final EntityManagerFactory pEntityManagerFactory) {
                                                                   return createServiceCore(pEntityManagerFactory);
                                                               }
                                                           };

        /**
         * 
         */
        @Test
        public void _update() {
            fail();

            final EntityManager em = this.tool.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final EArContent ac = new EArContent();
            ac.setTitle("title"); //$NON-NLS-1$

            em.flush();
        }

    }

    /**
     * @author jabaraster
     */
    public static class TableRowCount_is_10 {
        /**
         * 
         */
        @Rule
        public JpaDaoRule<ArContentServiceImpl> rule = new JpaDaoRule<ArContentServiceImpl>() {
                                                         @Override
                                                         protected ArContentServiceImpl createService(final EntityManagerFactory pEntityManagerFactory) {
                                                             return createServiceCore(pEntityManagerFactory);
                                                         }
                                                     };

        /**
         * 
         */
        @SuppressWarnings("boxing")
        @Test
        public void _count_管理者ユーザ() {
            final LoginUser adminUser = getAdministratorUser(this.rule.getEntityManager());
            final LoginUser normalUser = insertNormalUser("jabaraster"); //$NON-NLS-1$

            final int adminUserContentCount = 20;
            final int normalUserContentCount = 6;

            insert(adminUser, adminUserContentCount);
            insert(normalUser, normalUserContentCount);
            this.rule.getEntityManager().flush();

            final long actual = this.rule.getSut().count(adminUser);

            assertThat(actual, is((long) adminUserContentCount + normalUserContentCount));
        }

        /**
         * 
         */
        @SuppressWarnings("boxing")
        @Test
        public void _count_通常ユーザ() {
            final LoginUser adminUser = getAdministratorUser(this.rule.getEntityManager());
            final LoginUser normalUser = insertNormalUser("jabaraster"); //$NON-NLS-1$

            final int adminUserContentCount = 20;
            final int normalUserContentCount = 6;

            insert(adminUser, adminUserContentCount);
            insert(normalUser, normalUserContentCount);
            this.rule.getEntityManager().flush();

            final long actual = this.rule.getSut().count(normalUser);

            assertThat(actual, is((long) normalUserContentCount));
        }

        /**
         * 
         */
        @SuppressWarnings("boxing")
        @Test
        public void _find_管理者ユーザ() {
            final LoginUser adminUser = getAdministratorUser(this.rule.getEntityManager());
            insert(adminUser, 30);
            this.rule.getEntityManager().flush();

            final long first = 6;
            final long count = 5;

            final List<EArContent> contents = this.rule.getSut().find(adminUser, first, count, Sort.asc(EArContent_.title.getName()));

            assertThat((long) contents.size(), is(count));
        }

        /**
         * 
         */
        @SuppressWarnings("boxing")
        @Test
        public void _find_通常ユーザ() {
            final LoginUser adminUser = getAdministratorUser(this.rule.getEntityManager());
            final LoginUser normalUser = insertNormalUser("jabaraster"); //$NON-NLS-1$

            final int adminUserContentCount = 20;
            final int normalUserContentCount = 6;

            insert(adminUser, adminUserContentCount);
            insert(normalUser, normalUserContentCount);
            this.rule.getEntityManager().flush();

            final long first = 0;
            final long count = 10;

            final List<EArContent> contents = this.rule.getSut().find(normalUser, first, count, Sort.asc(EArContent_.title.getName()));
            assertThat(contents.size(), is(normalUserContentCount));
        }

        private EUser findUserByUserId(final String pUserId) throws NotFound {
            final EntityManager em = this.rule.getEntityManager();
            final CriteriaBuilder builder = em.getCriteriaBuilder();
            final CriteriaQuery<EUser> query = builder.createQuery(EUser.class);
            final Root<EUser> root = query.from(EUser.class);
            query.where(builder.equal(root.get(EUser_.userId), pUserId));
            return JpaDaoBase.getSingleResult(em.createQuery(query));
        }

        private void insert(final LoginUser pOwner, final int pInsertRowCount) {
            try {
                final EUser owner = createUserService(this.rule.getEntityManagerFactory()).findById(pOwner.getId());
                final LoginUser o = new LoginUser(owner);
                for (int i = 0; i < pInsertRowCount; i++) {
                    final EArContent ac = new EArContent();
                    ac.setTitle(i + "'th AR Contents."); //$NON-NLS-1$
                    this.rule.getSut().insertOrUpdate(o, ac, getMarkerData(), getContentData());
                }
                this.rule.getEntityManager().flush();

            } catch (final NotFound e) {
                throw ExceptionUtil.rethrow(e);
            }
        }

        private LoginUser insertNormalUser(final String pUserId) {
            try {
                final EUser already = findUserByUserId(pUserId);
                return new LoginUser(already);
            } catch (final NotFound e) {
                //
            }
            try {
                final EUser user = new EUser();
                user.setAdministrator(false);
                user.setUserId(pUserId);
                createUserService(this.rule.getEntityManagerFactory()).insertOrUpdate(user, "aaa"); //$NON-NLS-1$
                return new LoginUser(user);

            } catch (final Duplicate e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
    }
}
