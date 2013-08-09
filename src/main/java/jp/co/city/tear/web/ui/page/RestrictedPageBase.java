package jp.co.city.tear.web.ui.page;

import jp.co.city.tear.web.ui.component.MenuPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public abstract class RestrictedPageBase extends WebPageBase {
    private static final long serialVersionUID = -7167986041931382061L;

    /**
     * 
     */
    protected RestrictedPageBase() {
        super();
    }

    /**
     * @param pParameters -
     */
    protected RestrictedPageBase(final PageParameters pParameters) {
        super(pParameters);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pId) {
        return new MenuPanel(pId);
    }
}
