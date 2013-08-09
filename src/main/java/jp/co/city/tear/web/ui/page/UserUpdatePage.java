/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public class UserUpdatePage extends UserEditPage {
    private static final long serialVersionUID = -1979174828823637569L;

    /**
     * @param pParameters -
     */
    public UserUpdatePage(final PageParameters pParameters) {
        super(pParameters);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Model.of("ユーザ情報更新"); //$NON-NLS-1$
    }
}
