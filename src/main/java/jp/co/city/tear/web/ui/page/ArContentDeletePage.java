/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.NotFound;
import jabara.wicket.Models;

import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.WicketApplication;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class ArContentDeletePage extends RestrictedPageBase {
    private static final long serialVersionUID = 4077550347681798839L;

    private final Handler     handler          = new Handler();

    @Inject
    IArContentService         arContentService;

    private EArContent        arContent;

    private Label             title;
    private Form<?>           form;
    private Button            submitter;

    /**
     * @param pParameters -
     */
    public ArContentDeletePage(final PageParameters pParameters) {
        super(pParameters);
        try {
            this.arContent = this.arContentService.findById(AppSession.get().getLoginUser(), pParameters.get(0).toLong());

            this.add(getUserId());
            this.add(getForm());

        } catch (StringValueConversionException | NotFound e) {
            throw new RestartResponseAtInterceptPageException(WicketApplication.get().getHomePage());
        }
    }

    /**
     * @see jp.co.city.tear.web.ui.page.RestrictedPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addBodyCssReference(pResponse);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ユーザ情報削除"); //$NON-NLS-1$
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    @SuppressWarnings("serial")
    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new Button("submitter") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    ArContentDeletePage.this.handler.onSubmit();
                }
            };
        }
        return this.submitter;
    }

    private Label getUserId() {
        if (this.title == null) {
            this.title = new Label(EArContent_.title.getName(), this.arContent.getTitle());
        }
        return this.title;
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = 1602048681286165479L;

        public void onSubmit() {
            ArContentDeletePage.this.arContentService.delete(ArContentDeletePage.this.arContent);
            setResponsePage(ArContentListPage.class);
        }
    }

}
