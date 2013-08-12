package jp.co.city.tear.web.ui.page;

import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.component.MenuPanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public abstract class RestrictedPageBase extends WebPageBase {
    private static final long serialVersionUID = -7167986041931382061L;

    private Label             loginUserId;

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
        this.add(getLoginUserId());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pId) {
        return new MenuPanel(pId);
    }

    private Label getLoginUserId() {
        if (this.loginUserId == null) {
            this.loginUserId = new Label("loginUserId", AppSession.get().getLoginUser().getUserId()); //$NON-NLS-1$
        }
        return this.loginUserId;
    }
}
