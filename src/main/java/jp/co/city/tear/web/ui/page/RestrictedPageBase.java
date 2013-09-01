package jp.co.city.tear.web.ui.page;

import jabara.wicket.CssUtil;
import jp.co.city.tear.Environment;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.WicketApplication;
import jp.co.city.tear.web.ui.WicketApplication.MenuInfo;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public abstract class RestrictedPageBase extends WebPageBase {
    private static final long  serialVersionUID = -7167986041931382061L;

    private Label              applicationNameInNavbar;
    private Label              loginUserId;
    private ListView<MenuInfo> menus;
    private Link<?>            goLogout;

    /**
     * 
     */
    protected RestrictedPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    protected RestrictedPageBase(final PageParameters pParameters) {
        super(pParameters);
        this.add(getApplicationNameInNavbar());
        this.add(getMenus());
        this.add(getLoginUserId());
        this.add(getGoLogout());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, RestrictedPageBase.class);
    }

    private Label getApplicationNameInNavbar() {
        if (this.applicationNameInNavbar == null) {
            this.applicationNameInNavbar = new Label("applicationNameInNavbar", Environment.getApplicationName()); //$NON-NLS-1$
        }
        return this.applicationNameInNavbar;
    }

    private Link<?> getGoLogout() {
        if (this.goLogout == null) {
            this.goLogout = new BookmarkablePageLink<>("goLogout", LogoutPage.class); //$NON-NLS-1$
        }
        return this.goLogout;
    }

    private Label getLoginUserId() {
        if (this.loginUserId == null) {
            this.loginUserId = new Label("loginUserId", AppSession.get().getLoginUser().getUserId()); //$NON-NLS-1$
        }
        return this.loginUserId;
    }

    @SuppressWarnings("serial")
    private ListView<MenuInfo> getMenus() {
        if (this.menus == null) {
            this.menus = new ListView<MenuInfo>("menus", WicketApplication.getMenuInfo()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<MenuInfo> pItem) {
                    final MenuInfo menu = pItem.getModelObject();

                    final WebMarkupContainer list = new WebMarkupContainer("list"); //$NON-NLS-1$
                    if (RestrictedPageBase.this.getClass().isAssignableFrom(menu.getPage())) {
                        list.add(AttributeModifier.append("class", "active")); //$NON-NLS-1$ //$NON-NLS-2$
                    }

                    final BookmarkablePageLink<?> link = new BookmarkablePageLink<>("goPage", menu.getPage()); //$NON-NLS-1$
                    link.add(new Label("label", menu.getLinkLabel())); //$NON-NLS-1$

                    list.add(link);

                    pItem.add(list);
                }
            };
        }
        return this.menus;
    }
}
