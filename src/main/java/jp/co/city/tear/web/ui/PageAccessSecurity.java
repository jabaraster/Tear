package jp.co.city.tear.web.ui;

import jabara.wicket.LoginPageInstantiationAuthorizer;
import jp.co.city.tear.web.ui.page.AdministrationPageBase;
import jp.co.city.tear.web.ui.page.LoginPage;
import jp.co.city.tear.web.ui.page.RestrictedPageBase;
import jp.co.city.tear.web.ui.page.TopPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;

/**
 * @author jabaraster
 */
public class PageAccessSecurity extends LoginPageInstantiationAuthorizer {

    /**
     * @see jabara.wicket.LoginPageInstantiationAuthorizer#getFirstPageType()
     */
    @Override
    protected Class<? extends Page> getFirstPageType() {
        return TopPage.class;
    }

    /**
     * @see jabara.wicket.LoginPageInstantiationAuthorizer#getLoginPageType()
     */
    @Override
    protected Class<? extends Page> getLoginPageType() {
        return LoginPage.class;
    }

    /**
     * @see jabara.wicket.LoginPageInstantiationAuthorizer#getRestictedPageType()
     */
    @Override
    protected Class<? extends Page> getRestictedPageType() {
        return RestrictedPageBase.class;
    }

    /**
     * @see jabara.wicket.LoginPageInstantiationAuthorizer#isAuthenticated()
     */
    @Override
    protected boolean isAuthenticated() {
        return AppSession.get().isAuthenticated();
    }

    /**
     * @see jabara.wicket.LoginPageInstantiationAuthorizer#isPermittedPage(java.lang.Class)
     */
    @Override
    protected boolean isPermittedPage(final Class<? extends WebPage> pPageType) {
        if (!AppSession.get().currentUserIsAdministrator()) {
            return !AdministrationPageBase.class.isAssignableFrom(pPageType);
        }
        return true;
    }
}
