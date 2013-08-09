/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public abstract class AdministrationPageBase extends RestrictedPageBase {
    private static final long serialVersionUID = -746095859339768174L;

    /**
     * 
     */
    protected AdministrationPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    protected AdministrationPageBase(final PageParameters pParameters) {
        super(pParameters);
    }

}
