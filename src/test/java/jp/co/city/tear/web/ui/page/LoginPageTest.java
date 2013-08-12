/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.WebStarter.Mode;
import jp.co.city.tear.entity.EUser;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
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
public class LoginPageTest {

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

    private static void login(final WicketTester pTester, final String pUserId, final String pPassword) {
        final LoginPage page = (LoginPage) pTester.getLastRenderedPage();
        final FormTester formTester = pTester.newFormTester(page.getForm().getId());
        formTester.setValue(page.getUserId(), pUserId);
        formTester.setValue(page.getPassword(), pPassword);
        formTester.submit(page.getSubmitter());
    }

    private static void loginAsAdministrator(final WicketTester pTester) {
        login(pTester, EUser.DEFAULT_ADMINISTRATOR_USER_ID, EUser.DEFAULT_ADMINISTRATOR_PASSWORD);
    }

    /**
     * @author jabaraster
     */
    public static class AccessTo_LoginPage {
        /**
         * 
         */
        @Rule
        public WicketRule tester = WicketRule.newInstance(LoginPage.class);

        /**
         * 
         */
        @Test
        public void _test() {
            this.tester.assertRenderedPage(LoginPage.class);

            loginAsAdministrator(this.tester);

            this.tester.assertRenderedPage(this.tester.getApplication().getHomePage());
        }
    }

    /**
     * @author jabaraster
     */
    public static class AccessTo_NotLoginPage {
        /**
         * 
         */
        @Rule
        public WicketRule tester = WicketRule.newInstance(ArContentListPage.class);

        /**
         * 
         */
        @Test
        public void _test() {
            this.tester.assertRenderedPage(LoginPage.class);

            loginAsAdministrator(this.tester);

            this.tester.assertRenderedPage(ArContentListPage.class);
        }
    }

    /**
     * @author jabaraster
     */
    public static class InputError {
        /**
         * 
         */
        @Rule
        public WicketRule tester = WicketRule.newInstance(LoginPage.class);

        /**
         * 
         */
        @SuppressWarnings("nls")
        @Test
        public void _test() {
            this.tester.assertRenderedPage(LoginPage.class);

            login(this.tester, "", "");
            this.tester.assertRenderedPage(LoginPage.class);
            this.tester.assertErrorMessages("ユーザIDは必須入力です.", "パスワードは必須入力です.");

            login(this.tester, null, "");
            this.tester.assertRenderedPage(LoginPage.class);
            this.tester.assertErrorMessages("ユーザIDは必須入力です.", "パスワードは必須入力です.");

            login(this.tester, "", null);
            this.tester.assertRenderedPage(LoginPage.class);
            this.tester.assertErrorMessages("ユーザIDは必須入力です.", "パスワードは必須入力です.");

            login(this.tester, EUser.DEFAULT_ADMINISTRATOR_USER_ID, "invalid password.");
            this.tester.assertRenderedPage(LoginPage.class);
            this.tester.assertErrorMessages("ログインに失敗しました.");
        }
    }
}
