package jp.co.city.tear.web.ui.page;

import jabara.wicket.Models;

import java.io.Serializable;
import java.util.Calendar;

import jp.co.city.tear.Environment;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 *
 */
@SuppressWarnings("synthetic-access")
public class TopPage extends RestrictedPageBase {
    private static final long serialVersionUID = -4965903336608758671L;

    private final Handler     handler          = new Handler();

    private Label             applicationName;
    private Label             now;
    private AjaxLink<?>       reloader;
    private Link<?>           goLogout;

    /**
     * 
     */
    public TopPage() {
        this.add(getApplicationName());
        this.add(getNow());
        this.add(getReloader());
        this.add(getGoLogout());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.of("Top"); //$NON-NLS-1$
    }

    private Label getApplicationName() {
        if (this.applicationName == null) {
            this.applicationName = new Label("applicationName", Models.of(Environment.getApplicationName())); //$NON-NLS-1$
        }
        return this.applicationName;
    }

    private Link<?> getGoLogout() {
        if (this.goLogout == null) {
            this.goLogout = new BookmarkablePageLink<Object>("goLogout", LogoutPage.class); //$NON-NLS-1$
        }
        return this.goLogout;
    }

    @SuppressWarnings({ "nls" })
    private Label getNow() {
        if (this.now == null) {
            this.now = new Label("now", Models.readOnlyDate("yyyy/MM/dd HH:mm:ss", Calendar.getInstance().getTime())); //$NON-NLS-1$
        }
        return this.now;
    }

    @SuppressWarnings("serial")
    private AjaxLink<?> getReloader() {
        if (this.reloader == null) {
            this.reloader = new IndicatingAjaxLink<Object>("reloader") { //$NON-NLS-1$
                @Override
                public void onClick(final AjaxRequestTarget pTarget) {
                    TopPage.this.handler.onReloaderClick(pTarget);
                }
            };
        }
        return this.reloader;
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = 8826180320287426527L;

        private void onReloaderClick(final AjaxRequestTarget pTarget) {
            pTarget.add(getNow());
        }

    }
}
