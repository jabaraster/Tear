/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;

/**
 * @author jabaraster
 */
public class BodyCssHeaderItem extends CssContentHeaderItem {

    /**
     * 
     */
    public BodyCssHeaderItem() {
        super(buildCssContent(), null, null);
    }

    /**
     * @return　-
     */
    public static BodyCssHeaderItem get() {
        return new BodyCssHeaderItem();
    }

    private static CharSequence buildCssContent() {
        final PackageTextTemplate text = new PackageTextTemplate(BodyCssHeaderItem.class, BodyCssHeaderItem.class.getSimpleName() + ".css"); //$NON-NLS-1$
        final Request request = RequestCycle.get().getRequest();
        final Map<String, Object> params = new HashMap<>();
        params.put("bodyBackground", request.getContextPath() + request.getFilterPath() + "/back"); //$NON-NLS-1$ //$NON-NLS-2$
        return text.asString(params);
    }
}
