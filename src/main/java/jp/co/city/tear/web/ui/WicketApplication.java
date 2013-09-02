package jp.co.city.tear.web.ui;

import jabara.general.ArgUtil;
import jabara.wicket.LoginPageInstantiationAuthorizer;
import jabara.wicket.MarkupIdForceOutputer;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.city.tear.web.ui.page.AdministrationPageBase;
import jp.co.city.tear.web.ui.page.ArContentDeletePage;
import jp.co.city.tear.web.ui.page.ArContentEditPage;
import jp.co.city.tear.web.ui.page.ArContentInsertPage;
import jp.co.city.tear.web.ui.page.ArContentListPage;
import jp.co.city.tear.web.ui.page.ArContentUpdatePage;
import jp.co.city.tear.web.ui.page.LoginPage;
import jp.co.city.tear.web.ui.page.LogoutPage;
import jp.co.city.tear.web.ui.page.RestrictedPageBase;
import jp.co.city.tear.web.ui.page.TopPage;
import jp.co.city.tear.web.ui.page.UserDeletePage;
import jp.co.city.tear.web.ui.page.UserEditPage;
import jp.co.city.tear.web.ui.page.UserInsertPage;
import jp.co.city.tear.web.ui.page.UserListPage;
import jp.co.city.tear.web.ui.page.UserUpdatePage;
import jp.co.city.tear.web.ui.page.WebPageBase;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.IProvider;

import com.google.inject.Injector;

/**
 *
 */
public class WicketApplication extends WebApplication {

    @SuppressWarnings("nls")
    private static final List<MenuInfo>                                                _menuInfoList  = Arrays.asList(
                                                                                                              //
                                                                                                              new MenuInfo(Models.readOnly("ユーザ一覧"),
                                                                                                                      UserListPage.class) //
                                                                                                              // , new
                                                                                                              // MenuInfo(Models.readOnly("ユーザ新規登録"),
                                                                                                              // UserInsertPage.class) //
                                                                                                              ,
                                                                                                              new MenuInfo(Models
                                                                                                                      .readOnly("ARコンテンツ一覧"),
                                                                                                                      ArContentListPage.class) //
                                                                                                      // , new
                                                                                                      // MenuInfo(Models.readOnly("ARコンテンツ新規登録"),
                                                                                                      // ArContentInsertPage.class) //
                                                                                                      // , new MenuInfo(Models.readOnly("ログアウト"),
                                                                                                      // LogoutPage.class) //
                                                                                                      );

    private static final String                                                        ENC            = "UTF-8";              //$NON-NLS-1$

    private final IProvider<Injector>                                                  injectorProvider;

    private final Map<Class<? extends WebPageBase>, Set<Class<? extends WebPageBase>>> menuCategories = buildMenuCategories();

    /**
     * @param pInjectorProvider Guiceの{@link Injector}を供給するオブジェクト. DI設定に使用します.
     */
    public WicketApplication(final IProvider<Injector> pInjectorProvider) {
        ArgUtil.checkNull(pInjectorProvider, "pInjectorProvider"); //$NON-NLS-1$
        this.injectorProvider = pInjectorProvider;
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return TopPage.class;
    }

    /**
     * @return -
     */
    public Injector getInjector() {
        return this.injectorProvider.get();
    }

    /**
     * @param pLinkTargetPage -
     * @param pViewPage -
     * @return -
     */
    public boolean isSelected(final Class<? extends WebPageBase> pLinkTargetPage, final Page pViewPage) {
        final Set<Class<? extends WebPageBase>> pages = this.menuCategories.get(pLinkTargetPage);
        if (pages == null) {
            return false;
        }
        for (final Class<? extends WebPageBase> page : pages) {
            if (page.isInstance(pViewPage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(final Request pRequest, @SuppressWarnings("unused") final Response pResponse) {
        return new AppSession(pRequest);
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        mountPages();
        initializeEncoding();
        initializeInjection();
        initializeSecurity();
        initializeOther();
    }

    private void initializeEncoding() {
        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());
    }

    private void initializeInjection() {
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, this.injectorProvider.get()));
    }

    private void initializeOther() {
        getComponentInstantiationListeners().add(new MarkupIdForceOutputer());
    }

    private void initializeSecurity() {
        getSecuritySettings().setAuthorizationStrategy(new LoginPageInstantiationAuthorizer() {

            @Override
            protected Class<? extends Page> getFirstPageType() {
                return TopPage.class;
            }

            @Override
            protected Class<? extends Page> getLoginPageType() {
                return LoginPage.class;
            }

            @Override
            protected Class<? extends Page> getRestictedPageType() {
                return RestrictedPageBase.class;
            }

            @Override
            protected boolean isAuthenticated() {
                final AppSession session = AppSession.get();
                return session.isAuthenticated();
            }

            @Override
            protected boolean isPermittedPage(final Class<? extends WebPage> pPageType) {
                if (!AppSession.get().currentUserIsAdministrator()) {
                    return !AdministrationPageBase.class.isAssignableFrom(pPageType);
                }
                return true;
            }
        });
    }

    @SuppressWarnings("nls")
    private void mountPages() {
        this.mountPage("login", LoginPage.class);
        this.mountPage("logout", LogoutPage.class);
        this.mountPage("top", TopPage.class);

        this.mountPage("mainte/user/", UserListPage.class);
        this.mountPage("mainte/user/index", UserListPage.class);
        this.mountPage("mainte/user/new", UserInsertPage.class);
        this.mountPage("mainte/user/edit", UserUpdatePage.class);
        this.mountPage("mainte/user/delete", UserDeletePage.class);

        this.mountPage("mainte/content/", ArContentListPage.class);
        this.mountPage("mainte/content/index", ArContentListPage.class);
        this.mountPage("mainte/content/new", ArContentInsertPage.class);
        this.mountPage("mainte/content/edit", ArContentUpdatePage.class);
        this.mountPage("mainte/content/delete", ArContentDeletePage.class);
    }

    /**
     * @return -
     */
    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }

    /**
     * @return -
     */
    public static List<MenuInfo> getMenuInfo() {
        if (AppSession.get().currentUserIsAdministrator()) {
            return new ArrayList<>(_menuInfoList);
        }

        final List<MenuInfo> ret = new ArrayList<>();
        for (final MenuInfo mi : _menuInfoList) {
            if (!AdministrationPageBase.class.isAssignableFrom(mi.getPage())) {
                ret.add(mi);
            }
        }
        return ret;
    }

    private static Map<Class<? extends WebPageBase>, Set<Class<? extends WebPageBase>>> buildMenuCategories() {
        final Map<Class<? extends WebPageBase>, Set<Class<? extends WebPageBase>>> ret = new HashMap<Class<? extends WebPageBase>, Set<Class<? extends WebPageBase>>>();
        ret.put(TopPage.class, new HashSet<Class<? extends WebPageBase>>(Arrays.asList(TopPage.class)));
        ret.put(UserListPage.class, new HashSet<Class<? extends WebPageBase>>(Arrays.asList(UserListPage.class, UserEditPage.class)));
        ret.put(ArContentListPage.class, new HashSet<Class<? extends WebPageBase>>(Arrays.asList(ArContentListPage.class, ArContentEditPage.class)));
        return ret;
    }

    /**
     * @author jabaraster
     */
    public static class MenuInfo implements Serializable {
        private static final long                  serialVersionUID = -7238828499283607277L;

        private final IModel<String>               linkLabel;
        private final Class<? extends WebPageBase> page;

        /**
         * @param pLinkLabel -
         * @param pPage -
         */
        public MenuInfo(final IModel<String> pLinkLabel, final Class<? extends WebPageBase> pPage) {
            ArgUtil.checkNull(pLinkLabel, "pLinkLabel"); //$NON-NLS-1$
            ArgUtil.checkNull(pPage, "pPage"); //$NON-NLS-1$
            this.linkLabel = pLinkLabel;
            this.page = pPage;
        }

        /**
         * @return -
         */
        public IModel<String> getLinkLabel() {
            return this.linkLabel;
        }

        /**
         * @return -
         */
        public Class<? extends WebPageBase> getPage() {
            return this.page;
        }
    }
}
