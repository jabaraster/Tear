/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.wicket.JavaScriptUtil;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author jabaraster
 */
public class BootPage extends WebPage {
    private static final long serialVersionUID = -2693611456169252851L;

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(WebPageBase.class, "bootstrap/css/bootstrap.min.css"))); //$NON-NLS-1$
        JavaScriptUtil.addJQuery1_9_1Reference(pResponse);
        pResponse.render(JavaScriptHeaderItem.forReference(new CssResourceReference(WebPageBase.class, "bootstrap/js/bootstrap.min.js"))); //$NON-NLS-1$
    }
}
