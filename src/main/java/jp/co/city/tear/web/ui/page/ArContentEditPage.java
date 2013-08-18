/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.wicket.CssUtil;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;
import jabara.wicket.ValidatorUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.ui.AppSession;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * @author jabaraster
 */
@SuppressWarnings({ "synthetic-access", "serial" })
public class ArContentEditPage extends RestrictedPageBase {
    private static final long serialVersionUID = -4884364205385771240L;

    private final EArContent  arContent;

    private final Handler     handler          = new Handler();

    @Inject
    IArContentService         arContentService;

    private FeedbackPanel     feedback;
    private Form<?>           form;
    private TextField<String> title;
    private FileUploadField   marker;
    private Image             markerImage;
    private FileUploadField   contents;
    private Label             contentLabel;
    private Button            submitter;

    /**
     * 
     */
    public ArContentEditPage() {
        this(new EArContent());
    }

    /**
     * @param pArContent -
     */
    public ArContentEditPage(final EArContent pArContent) {
        ArgUtil.checkNull(pArContent, "pArContents"); //$NON-NLS-1$
        this.arContent = pArContent;
        initialize();
    }

    /**
     * @param pParameters -
     */
    public ArContentEditPage(final PageParameters pParameters) {
        super(pParameters);
        try {
            this.arContent = this.arContentService.findById(AppSession.get().getLoginUser(), pParameters.get(0).toLong());
            initialize();
        } catch (StringValueConversionException | NotFound e) {
            throw new RestartResponseException(WebApplication.get().getHomePage());
        }
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, ArContentEditPage.class);
        JavaScriptUtil.addJQuery1_9_1Reference(pResponse);
        JavaScriptUtil.addComponentJavaScriptReference(pResponse, ArContentEditPage.class);
        JavaScriptUtil.addFocusScript(pResponse, getTitle());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ARコンテンツの編集"); //$NON-NLS-1$
    }

    Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getFeedback());
            this.form.add(getTitle());
            this.form.add(getMarker());
            this.form.add(getMarkerImage());
            this.form.add(getContents());
            this.form.add(getContentLabel());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new Button("submitter") { //$NON-NLS-1$

                @Override
                public void onError() {
                    super.onError();
                }

                @Override
                public void onSubmit() {
                    ArContentEditPage.this.handler.onSubmit();
                }
            };
        }
        return this.submitter;
    }

    @SuppressWarnings("nls")
    private Label getContentLabel() {
        if (this.contentLabel == null) {
            this.contentLabel = new Label("contentLabel", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return ArContentEditPage.this.arContent.getContent().hasData() //
                    ? String.valueOf(ArContentEditPage.this.arContent.getContent().getLength()) //
                            : "コンテンツが登録されていません";
                }
            });

        }
        return this.contentLabel;
    }

    private FileUploadField getContents() {
        if (this.contents == null) {
            this.contents = new FileUploadField(EArContent_.content.getName());
        }
        return this.contents;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private FileUploadField getMarker() {
        if (this.marker == null) {
            this.marker = new FileUploadField(EArContent_.marker.getName());
        }
        return this.marker;
    }

    @SuppressWarnings("resource")
    private Image getMarkerImage() {
        if (this.markerImage == null) {
            this.markerImage = new Image("markerImage", new ResourceStreamResource(new R(this.arContent.getMarker()))); //$NON-NLS-1$
        }
        return this.markerImage;
    }

    private TextField<String> getTitle() {
        if (this.title == null) {
            this.title = new TextField<>(EArContent_.title.getName(), new PropertyModel<String>(this.arContent, EArContent_.title.getName()));
            ValidatorUtil.setSimpleStringValidator(this.title, EArContent.class, EArContent_.title);
        }
        return this.title;
    }

    private void initialize() {
        this.add(getForm());
    }

    /**
     * @param pArContent -
     * @return -
     */
    public static PageParameters createParameters(final EArContent pArContent) {
        ArgUtil.checkNull(pArContent, "pArContent"); //$NON-NLS-1$
        if (!pArContent.isPersisted()) {
            throw new IllegalArgumentException("永続化されていないエンティティは処理出来ません."); //$NON-NLS-1$
        }

        final PageParameters ret = new PageParameters();
        ret.set(0, pArContent.getId());
        return ret;
    }

    private static InputStream getDataFromFileUpload(final FileUploadField pField) throws IOException {
        final FileUpload upload = pField.getFileUpload();
        if (upload == null) {
            return null;
        }
        return upload.getInputStream();
    }

    private class Handler implements Serializable {

        void onSubmit() {
            try (final InputStream markerData = getDataFromFileUpload(getMarker()); //
                    final InputStream contentData = getDataFromFileUpload(getContents()) //
            ) {
                ArContentEditPage.this.arContentService.insertOrUpdate( //
                        AppSession.get().getLoginUser() //
                        , ArContentEditPage.this.arContent //
                        , markerData //
                        , contentData //
                        );

                setResponsePage(ArContentListPage.class);

            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
    }

    private class R extends AbstractResourceStream {

        private final ELargeData data;
        private InputStream      in;

        R(final ELargeData pData) {
            this.data = pData;
        }

        @Override
        public void close() {
            IOUtils.closeQuietly(this.in);
        }

        @Override
        public InputStream getInputStream() throws ResourceStreamNotFoundException {
            if (this.data == null) {
                throw new ResourceStreamNotFoundException();
            }
            try {
                return ArContentEditPage.this.arContentService.getDataInputStream(this.data);
            } catch (final NotFound e) {
                throw new ResourceStreamNotFoundException();
            }
        }

    }
}
