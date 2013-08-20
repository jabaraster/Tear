/**
 * 
 */
package jp.co.city.tear.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;

import java.io.IOException;
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

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

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

    @SuppressWarnings("resource")
    private static LargeDataOperation getContentData() {
        final InputStream in = getContentDataStream();
        return new LargeDataOperation().update(in);
    }

    private static int getContentDataLength() throws IOException {
        try (final InputStream in = getContentDataStream()) {
            return IOUtils.toByteArray(in).length;
        }
    }

    private static InputStream getContentDataStream() {
        return getResourceAsStream("AR01_content.jpg"); //$NON-NLS-1$
    }

    @SuppressWarnings("resource")
    private static LargeDataOperation getMarkerData() {
        final InputStream in = getMarkerDataStream();
        return new LargeDataOperation().update(in);
    }

    private static int getMarkerDataLength() throws IOException {
        try (final InputStream in = getMarkerDataStream()) {
            return IOUtils.toByteArray(in).length;
        }
    }

    private static InputStream getMarkerDataStream() {
        return getResourceAsStream("AR01_marker.jpg"); //$NON-NLS-1$
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
         * @throws IOException -
         */
        @Test
        public void _insert_データあり() throws NotFound, IOException {
            final EntityManager em = this.rule.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final EArContent ac = new EArContent();
            final String title = "title"; //$NON-NLS-1$
            ac.setTitle(title);

            try (LargeDataOperation md = getMarkerData(); LargeDataOperation cd = getContentData()) {
                this.rule.getSut().insertOrUpdate(loginUser, ac, md, cd);
            }

            em.flush();
            em.clear();

            assertContent(loginUser, ac, title, true);
        }

        /**
         * @throws NotFound -
         * @throws IOException -
         */
        @Test
        public void _insert_データなし_cancel() throws NotFound, IOException {
            final EntityManager em = this.rule.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final EArContent ac = new EArContent();
            final String title = "title"; //$NON-NLS-1$
            ac.setTitle(title);

            try (LargeDataOperation md = getMarkerData(); LargeDataOperation cd = getContentData()) {
                this.rule.getSut().insertOrUpdate(loginUser, ac, md.cancel(), cd.cancel());
            }

            em.flush();
            em.clear();

            assertContent(loginUser, ac, title, false);
        }

        /**
         * @throws NotFound -
         * @throws IOException -
         */
        @Test
        public void _insert_データなし_delete() throws NotFound, IOException {
            final EntityManager em = this.rule.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final EArContent ac = new EArContent();
            final String title = "title"; //$NON-NLS-1$
            ac.setTitle(title);

            try (LargeDataOperation md = getMarkerData(); LargeDataOperation cd = getContentData()) {
                this.rule.getSut().insertOrUpdate(loginUser, ac, md.delete(), cd.delete());
            }

            em.flush();
            em.clear();

            assertContent(loginUser, ac, title, false);
        }

        /**
         * @throws NotFound -
         * @throws IOException -
         */
        @SuppressWarnings("boxing")
        @Test
        public void _update_データに変更なし() throws NotFound, IOException {
            final EntityManager em = this.rule.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final String newTitle = "new_title"; //$NON-NLS-1$
            final EArContent update = insertAndUpdate(loginUser, newTitle);

            try (LargeDataOperation md = getMarkerData(); //
                    LargeDataOperation cd = getContentData(); //
                    InputStream updateMarkerData = getContentDataStream(); //
                    InputStream updateContentData = getMarkerDataStream(); //
            ) {
                md.update(updateMarkerData).cancel();
                cd.update(updateContentData).cancel();
                this.rule.getSut().insertOrUpdate(loginUser, update, md, cd);

                em.flush();
                em.clear();

                assertContent(loginUser, update, newTitle, true);
                final EArContent inDb = this.rule.getSut().findById(loginUser, update.getId().longValue());
                assertThat(inDb.getMarker().getLength(), is(getMarkerDataLength()));
                assertThat(inDb.getContent().getLength(), is(getContentDataLength()));
            }
        }

        /**
         * @throws NotFound -
         * @throws IOException -
         */
        @SuppressWarnings("boxing")
        @Test
        public void _update_データ更新() throws NotFound, IOException {
            final EntityManager em = this.rule.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final String newTitle = "new_title"; //$NON-NLS-1$
            final EArContent update = insertAndUpdate(loginUser, newTitle);

            try (LargeDataOperation md = getMarkerData(); //
                    LargeDataOperation cd = getContentData(); //
                    InputStream updateMarkerData = getContentDataStream(); //
                    InputStream updateContentData = getMarkerDataStream(); //
            ) {
                this.rule.getSut().insertOrUpdate(loginUser, update, md.update(updateMarkerData), cd.update(updateContentData));

                em.flush();
                em.clear();

                assertContent(loginUser, update, newTitle, true);
                final EArContent inDb = this.rule.getSut().findById(loginUser, update.getId().longValue());
                assertThat(inDb.getMarker().getLength(), is(getContentDataLength()));
                assertThat(inDb.getContent().getLength(), is(getMarkerDataLength()));
            }
        }

        /**
         * @throws NotFound -
         * @throws IOException -
         */
        @Test
        public void _update_データ削除() throws NotFound, IOException {
            final EntityManager em = this.rule.getEntityManager();
            final LoginUser loginUser = getAdministratorUser(em);

            final String newTitle = "new_title"; //$NON-NLS-1$
            final EArContent update = insertAndUpdate(loginUser, newTitle);

            try (LargeDataOperation md = getMarkerData(); LargeDataOperation cd = getContentData()) {
                this.rule.getSut().insertOrUpdate(loginUser, update, md.delete(), cd.delete());
            }

            em.flush();
            em.clear();

            assertContent(loginUser, update, newTitle, false);
        }

        @SuppressWarnings("boxing")
        private void assertContent( //
                final LoginUser pLoginUser //
                , final EArContent pArContent //
                , final String pTitle //
                , final boolean pHasData) throws NotFound {
            final EArContent inDb = this.rule.getSut().findById(pLoginUser, pArContent.getId().longValue());
            assertThat(inDb.getTitle(), is(pTitle));
            assertThat(inDb.getMarker().hasData(), is(pHasData));
            assertThat(inDb.getContent().hasData(), is(pHasData));
        }

        private EArContent insertAndUpdate(final LoginUser loginUser, final String pNewTitle) throws IOException, NotFound {
            final EntityManager em = this.rule.getEntityManager();
            final EArContent ac = new EArContent();
            ac.setTitle("title"); //$NON-NLS-1$

            try (LargeDataOperation md = getMarkerData(); LargeDataOperation cd = getContentData()) {
                this.rule.getSut().insertOrUpdate(loginUser, ac, md, cd);
            }

            em.flush();
            em.clear();

            final EArContent update = this.rule.getSut().findById(loginUser, ac.getId().longValue());
            update.setTitle(pNewTitle);
            return update;
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
