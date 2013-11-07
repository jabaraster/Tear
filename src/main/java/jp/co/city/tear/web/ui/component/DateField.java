/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import java.util.Date;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

/**
 * TODO value属性の処理が不完全.
 * 
 * @author jabaraster
 */
public class DateField extends FormComponent<Date> {
    private static final long serialVersionUID = 4106555986867442354L;

    /**
     * @param pId -
     */
    public DateField(final String pId) {
        super(pId);
    }

    /**
     * @param pId -
     * @param pModel -
     */
    public DateField(final String pId, final IModel<Date> pModel) {
        super(pId, pModel);
    }

    /**
     * @see org.apache.wicket.markup.html.form.FormComponent#onComponentTag(org.apache.wicket.markup.ComponentTag)
     */
    @SuppressWarnings("nls")
    @Override
    protected void onComponentTag(final ComponentTag pTag) {
        // Must be attached to an input tag
        checkComponentTag(pTag, "input");

        // check for text type
        if (pTag.getAttributes().containsKey("type")) {
            checkComponentTagAttribute(pTag, "type", "date");
        }

        pTag.put("value", getValue());

        // Default handling for component tag
        super.onComponentTag(pTag);
    }
}
