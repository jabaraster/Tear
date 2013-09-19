package jp.co.city.tear.web.ui;

import jabara.general.ArgUtil;
import jabara.wicket.MarkupIdForceOutputer;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.city.tear.web.ui.page.AdministrationPageBase;
import jp.co.city.tear.web.ui.page.ArContentDeletePage;
import jp.co.city.tear.web.ui.page.ArContentEditPage;
import jp.co.city.tear.web.ui.page.ArContentInsertPage;
import jp.co.city.tear.web.ui.page.ArContentListPage;
import jp.co.city.tear.web.ui.page.ArContentUpdatePage;
import jp.co.city.tear.web.ui.page.LoginPage;
import jp.co.city.tear.web.ui.page.LogoutPage;
import jp.co.city.tear.web.ui.page.TopPage;
import jp.co.city.tear.web.ui.page.UserDeletePage;
import jp.co.city.tear.web.ui.page.UserEditPage;
import jp.co.city.tear.web.ui.page.UserInsertPage;
import jp.co.city.tear.web.ui.page.UserListPage;
import jp.co.city.tear.web.ui.page.UserUpdatePage;
import jp.co.city.tear.web.ui.page.WebPageBase;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.IProvider;
import org.apache.wicket.util.time.Duration;

import com.google.inject.Injector;

/**
 *
 */
public class WicketApplication extends WebApplication {

    @SuppressWarnings("nls")
    private static final List<MenuInfo> _menuInfoList  = Arrays.asList(new MenuInfo(Models.readOnly("ユーザ一覧"), UserListPage.class) //
                                                               , new MenuInfo(Models.readOnly("ARコンテンツ一覧"), ArContentListPage.class) //
                                                       );

    private static final String         ENC            = "UTF-8";              //$NON-NLS-1$

    private final IProvider<Injector>   injectorProvider;

    private final MenuCategories        menuCategories = buildMenuCategories();

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
     * @return -
     */
    @SuppressWarnings("static-method")
    public List<MenuInfo> getMenuInfo() {
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

    /**
     * @param pResource -
     * @return -
     */
    @SuppressWarnings("static-method")
    public ResourceReference getSharedResourceReference(final Resource pResource) {
        ArgUtil.checkNull(pResource, "pResource"); //$NON-NLS-1$
        return new SharedResourceReference(pResource.getName());
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

        mountResources();
        mountPages();
        initializeEncoding();
        initializeInjection();
        initializePageAccessSecurity();
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
        // getJavaScriptLibrarySettings().setWicketAjaxReference(new )
    }

    private void initializePageAccessSecurity() {
        getSecuritySettings().setAuthorizationStrategy(new PageAccessSecurity());
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

    private void mountResource(final Resource pResource, final String pFilePath, final Duration pCacheDuration) {
        mountResource(pResource.getName(), new ResourceReference(pResource.getName()) {
            private static final long serialVersionUID = -8982729375012083247L;

            @Override
            public IResource getResource() {
                return new ResourceStreamResource(new UrlResourceStream(WicketApplication.class.getResource(pFilePath))) //
                        .setCacheDuration(pCacheDuration) //
                ;
            }
        });
    }

    @SuppressWarnings({ "nls" })
    private void mountResources() {
        mountResource(Resource.BACK, "brickwall.png", Duration.days(10));
        mountResource(Resource.FAVICON, "favicon.png", Duration.days(10));
    }

    /**
     * @return -
     */
    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }

    @SuppressWarnings({ "synthetic-access", "unchecked" })
    private static MenuCategories buildMenuCategories() {
        final MenuCategories ret = new MenuCategories();

        ret.append(TopPage.class, TopPage.class);
        ret.append(UserListPage.class, UserListPage.class, UserEditPage.class, UserDeletePage.class);
        ret.append(ArContentListPage.class, ArContentListPage.class, ArContentEditPage.class, ArContentDeletePage.class);

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

    /**
     * @author jabaraster
     */
    public enum Resource {
        /**
         * 
         */
        BACK("back"), //$NON-NLS-1$

        /**
         * 
         */
        FAVICON("favicon"), //$NON-NLS-1$

        ;

        private final String name;

        Resource(final String pName) {
            this.name = pName;
        }

        /**
         * @return -
         */
        public String getName() {
            return this.name;
        }
    }

    private static class MenuCategories extends HashMap<Class<? extends WebPageBase>, Set<Class<? extends WebPageBase>>> {
        private static final long serialVersionUID = 2226176315425317930L;

        void append(final Class<? extends WebPageBase> pLinkTarget, @SuppressWarnings("unchecked") final Class<? extends WebPageBase>... pViewPages) {
            ArgUtil.checkNull(pLinkTarget, "pLinkTarget"); //$NON-NLS-1$
            ArgUtil.checkNull(pViewPages, "pViewPages"); //$NON-NLS-1$
            for (final Class<? extends WebPageBase> page : pViewPages) {
                if (page == null) {
                    throw new IllegalArgumentException("pViewPages contain null element."); //$NON-NLS-1$
                }
            }

            Set<Class<? extends WebPageBase>> pages = this.get(pLinkTarget);
            if (pages == null) {
                pages = new HashSet<>();
                put(pLinkTarget, pages);
            }
            pages.addAll(Arrays.asList(pViewPages));
        }
    }
}
