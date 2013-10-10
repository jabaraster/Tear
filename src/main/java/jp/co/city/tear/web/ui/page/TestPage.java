/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.FileUploadPanel;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.Models;

import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.service.IArContentService;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author jabaraster
 */
@SuppressWarnings({ "nls", "serial", "synthetic-access" })
public class TestPage extends RestrictedPageBase {

    private final EArContent  arContent = new EArContent();

    private final Handler     handler   = new Handler();

    @Inject
    IArContentService         arContentService;

    private Form<?>           form;
    private TextField<String> title;
    private AjaxButton        submitter;

    private Form<?>           markerForm;
    private FileUploadPanel   markerUpload;

    private Form<?>           contentForm;
    private FileUploadPanel   contentUpload;
    private Label             contentLabel;

    /**
     * 
     */
    public TestPage() {
        this.add(getForm());
        this.add(getMarkerForm());
        this.add(getContentForm());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        pResponse.render(ComponentCssHeaderItem.forType(TestPage.class));
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ARコンテンツの編集");
    }

    private Form<?> getContentForm() {
        if (this.contentForm == null) {
            this.contentForm = new Form<>("contentForm");
            this.contentForm.add(getContentUpload());
            this.contentForm.add(getContentLabel());
        }
        return this.contentForm;
    }

    private Label getContentLabel() {
        if (this.contentLabel == null) {
            this.contentLabel = new Label("contentLabel", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return getContentUpload().getDataOperation().hasData() ? "コンテンツあり" : "コンテンツなし";
                }
            });
        }
        return this.contentLabel;
    }

    private FileUploadPanel getContentUpload() {
        if (this.contentUpload == null) {
            this.contentUpload = new FileUploadPanel("contentUpload");

            final IAjaxCallback callback = new IAjaxCallback() {
                @Override
                public void call(final AjaxRequestTarget pTarget) {
                    pTarget.add(getContentLabel());
                }
            };
            this.contentUpload.setOnUpload(callback);
            this.contentUpload.setOnDelete(callback);
        }
        return this.contentUpload;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form");
            this.form.add(getTitle());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private Form<?> getMarkerForm() {
        if (this.markerForm == null) {
            this.markerForm = new Form<>("markerForm");
            this.markerForm.add(getMarkerUpload());
        }
        return this.markerForm;
    }

    private FileUploadPanel getMarkerUpload() {
        if (this.markerUpload == null) {
            this.markerUpload = new FileUploadPanel("markerUpload");
        }
        return this.markerUpload;
    }

    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") {
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    TestPage.this.handler.onSubmit(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private TextField<String> getTitle() {
        if (this.title == null) {
            this.title = new TextField<String>("title" //
                    , new PropertyModel<String>(this.arContent, EArContent_.title.getName()) //
                    , String.class);
        }
        return this.title;
    }

    private class Handler implements Serializable {

        void onSubmit(final AjaxRequestTarget pTarget) {
            TestPage.this.arContentService.insertOrUpdate( //
                    getSession().getLoginUser() //
                    , TestPage.this.arContent //
                    , getMarkerUpload().getDataOperation() //
                    , getContentUpload().getDataOperation());

            jabara.Debug.write(TestPage.this.arContent.getTitle());
            jabara.Debug.write(getMarkerUpload().getDataOperation());
            jabara.Debug.write(getContentUpload().getDataOperation());
            jabara.Debug.write(TestPage.this.arContent.getMarker());
            jabara.Debug.write(TestPage.this.arContent.getContent());
        }
    }
}
