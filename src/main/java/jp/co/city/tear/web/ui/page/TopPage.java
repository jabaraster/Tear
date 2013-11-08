package jp.co.city.tear.web.ui.page;

import jabara.wicket.Models;
import jp.co.city.tear.web.ui.component.DescriptionPanel;

import org.apache.wicket.model.IModel;

/**
 *
 */
public class TopPage extends RestrictedPageBase {
    private static final long serialVersionUID = -4965903336608758671L;
    private DescriptionPanel  description;

    /**
     * 
     */
    public TopPage() {
        this.add(getDescription());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("Top"); //$NON-NLS-1$
    }

    private DescriptionPanel getDescription() {
        if (this.description == null) {
            this.description = new DescriptionPanel("description"); //$NON-NLS-1$
        }
        return this.description;
    }
}
