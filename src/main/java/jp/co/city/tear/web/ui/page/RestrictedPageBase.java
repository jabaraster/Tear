package jp.co.city.tear.web.ui.page;

import jabara.wicket.CssUtil;
import jabara.wicket.Models;
import jp.co.city.tear.Environment;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.WicketApplication;
import jp.co.city.tear.web.ui.WicketApplication.MenuInfo;
import jp.co.city.tear.web.ui.component.CopyrightPanel;
import jp.co.city.tear.web.ui.component.MenuLinkList;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
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
    private static final long       serialVersionUID = -7167986041931382061L;

    private Label                   loginUserId;
    private ListView<MenuInfo>      menus;
    private MenuLinkList            goTop;
    private Link<?>                 goUserEdit;
    private BookmarkablePageLink<?> goLogout;
    private CopyrightPanel          copyright;

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
        this.add(getGoTop());
        this.add(getMenus());
        this.add(getGoUserEdit());
        this.add(getGoLogout());
        this.add(getCopyright());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, RestrictedPageBase.class);
    }

    private CopyrightPanel getCopyright() {
        if (this.copyright == null) {
            this.copyright = new CopyrightPanel("copyright"); //$NON-NLS-1$
        }
        return this.copyright;
    }

    private Link<?> getGoLogout() {
        if (this.goLogout == null) {
            this.goLogout = new BookmarkablePageLink<>("goLogout", LogoutPage.class); //$NON-NLS-1$
        }
        return this.goLogout;
    }

    private MenuLinkList getGoTop() {
        if (this.goTop == null) {
            this.goTop = new MenuLinkList("goTop" // //$NON-NLS-1$
                    , new MenuInfo(Models.readOnly(Environment.getApplicationName()), TopPage.class) //
            );
        }
        return this.goTop;
    }

    private Link<?> getGoUserEdit() {
        if (this.goUserEdit == null) {
            this.goUserEdit = new BookmarkablePageLink<>("goUserEdit", UserUpdatePage.class // //$NON-NLS-1$
                    , UserEditPage.createParameters(getSession().getLoginUser().getId()));
            this.goUserEdit.add(getLoginUserId());
        }
        return this.goUserEdit;
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
            this.menus = new ListView<MenuInfo>("menus", WicketApplication.get().getMenuInfo()) { //$NON-NLS-1$
                @Override
                protected void populateItem(final ListItem<MenuInfo> pItem) {
                    final MenuInfo menu = pItem.getModelObject();
                    if (WicketApplication.get().isSelected(menu.getPage(), findPage())) {
                        pItem.add(AttributeModifier.append("class", "active")); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    final BookmarkablePageLink<?> goPage = new BookmarkablePageLink<>("goPage", menu.getPage()); //$NON-NLS-1$
                    goPage.add(new Label("label", menu.getLinkLabel())); //$NON-NLS-1$

                    pItem.add(goPage);
                }
            };
        }
        return this.menus;
    }
}
