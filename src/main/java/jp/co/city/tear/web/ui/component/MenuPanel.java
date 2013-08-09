/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jp.co.city.tear.web.ui.WicketApplication;
import jp.co.city.tear.web.ui.WicketApplication.MenuInfo;
import jp.co.city.tear.web.ui.page.RestrictedPageBase;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
public class MenuPanel extends Panel {
    private static final long  serialVersionUID = 1736298938027733253L;

    private ListView<MenuInfo> menus;

    /**
     * @param pId -
     */
    public MenuPanel(final String pId) {
        super(pId);
        this.add(getMenus());
    }

    /**
     * @see org.apache.wicket.Component#getStatelessHint()
     */
    @Override
    protected boolean getStatelessHint() {
        return true;
    }

    @SuppressWarnings("serial")
    private ListView<MenuInfo> getMenus() {
        if (this.menus == null) {
            this.menus = new ListView<MenuInfo>("menus", WicketApplication.getMenuInfo()) { //$NON-NLS-1$ TODO Modelはコンストラクタからもらった方がいいかも・・・
                @Override
                protected void populateItem(final ListItem<MenuInfo> pItem) {
                    final Class<? extends RestrictedPageBase> info = pItem.getModelObject().getPage();
                    final BookmarkablePageLink<Object> link = new BookmarkablePageLink<>("goPage", info); //$NON-NLS-1$
                    link.add(new Label("label", pItem.getModelObject().getLinkLabel())); //$NON-NLS-1$
                    pItem.add(link);
                }
            };
        }
        return this.menus;
    }

}
