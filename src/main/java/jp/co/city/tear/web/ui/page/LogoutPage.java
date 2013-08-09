package jp.co.city.tear.web.ui.page;

import jabara.wicket.Models;

import org.apache.wicket.Session;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * 
 */
public class LogoutPage extends WebPageBase {
    private static final long serialVersionUID         = -3810270407936165942L;

    private static final int  REFRESH_INTERVAL_MINUTES = 5;

    /**
     * 
     */
    public LogoutPage() {
        this.add(new BookmarkablePageLink<>("goLogin", LoginPage.class)); //$NON-NLS-1$
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse, this.getClass());
        pResponse.render(OnDomReadyHeaderItem.forScript("countDown(" + REFRESH_INTERVAL_MINUTES + ")")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pId) {
        return createHeaderPanel(pId);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.of(getString("pageTitle")); //$NON-NLS-1$
    }

    /**
     * @see org.apache.wicket.markup.html.WebPage#onAfterRender()
     */
    @Override
    protected void onAfterRender() {
        super.onAfterRender();
        Session.get().invalidate();
    }
}
