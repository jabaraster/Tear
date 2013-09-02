/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.general.ArgUtil;
import jp.co.city.tear.web.ui.WicketApplication;
import jp.co.city.tear.web.ui.WicketApplication.MenuInfo;
import jp.co.city.tear.web.ui.page.WebPageBase;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
public class MenuLinkList extends Panel {
    private static final long       serialVersionUID = -6217679378588424721L;

    private final MenuInfo          menuInfo;

    private WebMarkupContainer      list;
    private BookmarkablePageLink<?> goPage;
    private Label                   label;

    /**
     * @param pId -
     * @param pMenuInfo -
     */
    public MenuLinkList(final String pId, final MenuInfo pMenuInfo) {
        super(pId);
        ArgUtil.checkNull(pMenuInfo, "pMenuInfo"); //$NON-NLS-1$
        this.menuInfo = pMenuInfo;
        this.add(getList());
    }

    private BookmarkablePageLink<?> getGoPage() {
        if (this.goPage == null) {
            this.goPage = new BookmarkablePageLink<>("goPage", this.menuInfo.getPage()); //$NON-NLS-1$
            this.goPage.add(getLabel());
        }
        return this.goPage;
    }

    private Label getLabel() {
        if (this.label == null) {
            this.label = new Label("label", this.menuInfo.getLinkLabel()); //$NON-NLS-1$
        }
        return this.label;
    }

    @SuppressWarnings("serial")
    private WebMarkupContainer getList() {
        if (this.list == null) {
            this.list = new WebMarkupContainer("list") { //$NON-NLS-1$
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onBeforeRender() {
                    super.onBeforeRender();

                    final Page page = findPage();
                    if (!WebPageBase.class.isAssignableFrom(page.getClass())) {
                        return;
                    }

                    if (WicketApplication.get().isSelected(MenuLinkList.this.menuInfo.getPage(), page)) {
                        this.add(AttributeModifier.append("class", "active")); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            };
            this.list.add(getGoPage());
        }
        return this.list;
    }

}
