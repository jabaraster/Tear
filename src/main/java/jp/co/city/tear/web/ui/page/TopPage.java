package jp.co.city.tear.web.ui.page;

import jabara.wicket.Models;

import org.apache.wicket.model.IModel;

/**
 *
 */
public class TopPage extends RestrictedPageBase {
    private static final long serialVersionUID = -4965903336608758671L;

    /**
     * 
     */
    public TopPage() {
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("Top"); //$NON-NLS-1$
    }
}
