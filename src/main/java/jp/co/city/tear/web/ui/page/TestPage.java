/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.IoUtil;
import jabara.general.io.DataOperation;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.FileUploadPanel;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.Models;
import jabara.wicket.ValidatorUtil;

import java.io.InputStream;
import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.ui.component.BodyCssHeaderItem;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

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
    private FeedbackPanel     feedback;
    private TextField<String> title;
    private AjaxButton        submitter;

    private Form<?>           markerForm;
    private FileUploadPanel   markerUpload;
    private NonCachingImage   markerImage;

    private Form<?>           contentForm;
    private FileUploadPanel   contentUpload;
    private Label             contentLabel;

    /**
     * 
     */
    public TestPage() {
        this.add(getForm());
        this.add(getSubmitter());
        this.add(getMarkerForm());
        this.add(getContentForm());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        pResponse.render(BodyCssHeaderItem.get());
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
                    return getContentUpload().getDataOperation().hasData() ? "動画あり" : "動画なし";
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

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback");
        }
        return this.feedback;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form");
            this.form.add(getFeedback());
            this.form.add(getTitle());
        }
        return this.form;
    }

    private Form<?> getMarkerForm() {
        if (this.markerForm == null) {
            this.markerForm = new Form<>("markerForm");
            this.markerForm.add(getMarkerUpload());
            this.markerForm.add(getMarkerImage());
        }
        return this.markerForm;
    }

    @SuppressWarnings("resource")
    private Image getMarkerImage() {
        if (this.markerImage == null) {
            this.markerImage = new NonCachingImage("markerImage", new ResourceStreamResource(new MarkerImageResourceStream()));
        }
        return this.markerImage;
    }

    private FileUploadPanel getMarkerUpload() {
        if (this.markerUpload == null) {
            this.markerUpload = new FileUploadPanel("markerUpload");

            final IAjaxCallback callback = new IAjaxCallback() {
                @Override
                public void call(final AjaxRequestTarget pTarget) {
                    pTarget.add(getMarkerImage());
                }
            };
            this.markerUpload.setOnUpload(callback);
            this.markerUpload.setOnDelete(callback);
        }
        return this.markerUpload;
    }

    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter", getForm()) {
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    TestPage.this.handler.onSubmitError(pTarget);
                }

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
            this.title = new TextField<>("title" //
                    , new PropertyModel<String>(this.arContent, EArContent_.title.getName()) //
                    , String.class);
            ValidatorUtil.setSimpleStringValidator(this.title, EArContent.class, EArContent_.title);
        }
        return this.title;
    }

    private class Handler implements Serializable {

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender(Models.readOnly("error"));

        void onSubmit(final AjaxRequestTarget pTarget) {
            TestPage.this.arContentService.insertOrUpdate( //
                    getSession().getLoginUser() //
                    , TestPage.this.arContent //
                    , getMarkerUpload().getDataOperation() //
                    , getContentUpload().getDataOperation());

            info("保存しました！");

            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getTitle());
            pTarget.add(getFeedback());
        }

        void onSubmitError(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getTitle());
            pTarget.add(getFeedback());
        }
    }

    private class MarkerImageResourceStream extends AbstractResourceStream {
        private InputStream stream;

        @Override
        public void close() {
            IoUtil.close(this.stream);
            this.stream = null;
        }

        @Override
        public InputStream getInputStream() throws ResourceStreamNotFoundException {
            if (this.stream != null) {
                return this.stream;
            }
            final DataOperation dataOperation = getMarkerUpload().getDataOperation();
            if (!dataOperation.hasData()) {
                throw new ResourceStreamNotFoundException();
            }
            this.stream = dataOperation.getData().getInputStream();
            return this.stream;
        }

    }
}
