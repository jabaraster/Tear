/**
 * 
 */
package jp.co.city.tear.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import jabara.general.Sort;
import jabara.jpa.entity.EntityBase_;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;

import jp.co.city.tear.WebStarter;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * @author jabaraster
 */
@RunWith(Enclosed.class)
public class UserServiceImplTest {

    /**
     * @throws NamingException -
     */
    @BeforeClass
    public static void beforeClass() throws NamingException {
        WebStarter.initializeDataSource();
    }

    /**
     * @author jabaraster
     */
    public static class TableIsEmpty {
        /**
         * 
         */
        @Rule
        public final JpaDaoRule<UserServiceImpl> tool = new JpaDaoRule<UserServiceImpl>() {
                                                          @Override
                                                          protected UserServiceImpl createService(final EntityManagerFactory pEntityManagerFactory) {
                                                              return new UserServiceImpl(pEntityManagerFactory, null);
                                                          }
                                                      };

        /**
         * 
         */
        @SuppressWarnings("boxing")
        @Test
        public void getAllの結果が0件() {
            final int actual = this.tool.getSut().getAll(Sort.asc(EntityBase_.id.getName())).size();
            assertThat(actual, is(0));
        }
    }
}
