/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.WebStarter.Mode;
import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.web.WebInitializer;
import jp.co.city.tear.web.ui.WicketApplication;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.IProvider;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.google.inject.Injector;

/**
 * @author jabaraster
 */
public class WicketRule extends WicketTester implements TestRule {

    WicketRule() {
        super(createApplication());
    }

    /**
     * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement, org.junit.runner.Description)
     */
    @Override
    public Statement apply(final Statement pBase, @SuppressWarnings("unused") final Description pDescription) {
        return pBase;
    }

    /**
     * @return -
     */
    @Override
    public WicketApplication getApplication() {
        return (WicketApplication) super.getApplication();
    }

    /**
     * @param pStartPageType -
     * @return ログインまで済ませ、pStartPageTypeを表示した状態のテスタ.
     */
    @SuppressWarnings("nls")
    public static WicketRule loggedin(final Class<? extends WebPage> pStartPageType) {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
        System.setProperty("hibernate.hbm2ddl.auto", "create");
        System.setProperty("HIBERNATE_HBM2DDL_AUTO", "create");

        final WicketRule ret = new WicketRule();
        ret.startPage(LoginPage.class);
        ret.assertRenderedPage(LoginPage.class);

        final LoginPage ids = (LoginPage) ret.getLastRenderedPage();

        final FormTester formTester = ret.newFormTester(ids.getForm().getId());
        formTester.setValue(ids.getUserId(), EUser.DEFAULT_ADMINISTRATOR_USER_ID);
        formTester.setValue(ids.getPassword(), EUser.DEFAULT_ADMINISTRATOR_PASSWORD);
        // formTester.submit(ids.getSubmitter()); ←AjaxButtonのサブミットはこちらではダメ.
        ret.executeAjaxEvent(ids.getSubmitter(), "click");

        ret.startPage(pStartPageType);
        return ret;
    }

    /**
     * @param pStartPageType -
     * @return -
     */
    public static WicketRule newInstance(final Class<? extends WebPage> pStartPageType) {
        final WicketRule ret = new WicketRule();
        ret.startPage(pStartPageType);
        return ret;
    }

    private static WicketApplication createApplication() {
        final Injector injector = new WebInitializer() {
            @Override
            public Injector getInjector() {
                return super.getInjector();
            }
        }.getInjector();
        final WicketApplication application = new WicketApplication(new IProvider<Injector>() {
            @Override
            public Injector get() {
                return injector;
            }
        });
        return application;
    }

}