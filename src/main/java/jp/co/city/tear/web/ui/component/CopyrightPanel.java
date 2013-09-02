/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jp.co.city.tear.Environment;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
public class CopyrightPanel extends Panel {
    private static final long serialVersionUID = 950592631577020085L;

    /**
     * @param pId -
     */
    public CopyrightPanel(final String pId) {
        super(pId);
        this.add(new Label("copyright", Environment.getCopyright())); //$NON-NLS-1$
    }
}
