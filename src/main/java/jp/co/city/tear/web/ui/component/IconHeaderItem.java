/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.general.ArgUtil;

import java.util.Collections;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * @author jabaraster
 */
public class IconHeaderItem extends HeaderItem {

    private final ResourceReference iconReference;

    /**
     * @param pIconReference -
     */
    public IconHeaderItem(final ResourceReference pIconReference) {
        this.iconReference = ArgUtil.checkNull(pIconReference, "pIconReference"); //$NON-NLS-1$
    }

    /**
     * @see org.apache.wicket.markup.head.HeaderItem#getRenderTokens()
     */
    @Override
    public Iterable<?> getRenderTokens() {
        return Collections.emptyList();
    }

    /**
     * @see org.apache.wicket.markup.head.HeaderItem#render(org.apache.wicket.request.Response)
     */
    @Override
    public void render(final Response pResponse) {
        pResponse.write("<link rel=\"shortcut icon\" href=\"" + getUrl() + "\" />"); //$NON-NLS-1$ //$NON-NLS-2$
        pResponse.write("\n"); //$NON-NLS-1$
    }

    private String getUrl() {
        final IRequestHandler handler = new ResourceReferenceRequestHandler(this.iconReference);
        return RequestCycle.get().urlFor(handler).toString();
    }

    /**
     * @param pResourceReference -
     * @return -
     */
    public static HeaderItem forReference(final ResourceReference pResourceReference) {
        ArgUtil.checkNull(pResourceReference, "pResourceReference"); //$NON-NLS-1$
        return new IconHeaderItem(pResourceReference);
    }

}
