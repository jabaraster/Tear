/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.wicket.CssUtil;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;
import jabara.wicket.ValidatorUtil;

import java.io.InputStream;
import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.component.FileUploadPanel;
import jp.co.city.tear.web.ui.component.FileUploadPanel.IOperation;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
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
    private static final long       serialVersionUID = -4884364205385771240L;

    private final EArContent        arContent;

    private final Handler           handler          = new Handler();

    @Inject
    IArContentService               arContentService;

    private FeedbackPanel           feedback;

    private Form<?>                 form;
    private TextField<String>       title;
    private Button                  submitter;
    private BookmarkablePageLink<?> cancelar;

    private Form<?>                 markerForm;
    private FileUploadPanel         markerUpload;
    private Image                   markerImage;

    private Form<?>                 contentForm;
    private FileUploadField         content;
    private Label                   contentLabel;
    private Button                  contentUploader;

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
            this.form.add(getTitle());
            this.form.add(getSubmitter());
            this.form.add(getCancelar());
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
                    // TODO
                }
            };
        }
        return this.submitter;
    }

    private Link<?> getCancelar() {
        if (this.cancelar == null) {
            this.cancelar = new BookmarkablePageLink<>("cancelar", ArContentListPage.class); //$NON-NLS-1$
        }
        return this.cancelar;
    }

    private FileUploadField getContent() {
        if (this.content == null) {
            this.content = new FileUploadField(EArContent_.content.getName());
        }
        return this.content;
    }

    private Form<?> getContentForm() {
        if (this.contentForm == null) {
            this.contentForm = new Form<>("contentForm"); //$NON-NLS-1$
            this.contentForm.add(getContent());
            this.contentForm.add(getContentLabel());
            this.contentForm.add(getContentUploader());
        }
        return this.contentForm;
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

    private Button getContentUploader() {
        if (this.contentUploader == null) {
            this.contentUploader = new Button("contentUploader") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    // TODO
                }
            };
        }
        return this.contentUploader;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private Form<?> getMarkerForm() {
        if (this.markerForm == null) {
            this.markerForm = new Form<>("markerForm"); //$NON-NLS-1$
            this.markerForm.add(getMarkerImage());
            this.markerForm.add(getMarkerUpload());
        }
        return this.markerForm;
    }

    @SuppressWarnings("resource")
    private Image getMarkerImage() {
        if (this.markerImage == null) {
            this.markerImage = new Image("markerImage", new ResourceStreamResource(new R(this.arContent.getMarker()))); //$NON-NLS-1$
        }
        return this.markerImage;
    }

    private FileUploadPanel getMarkerUpload() {
        if (this.markerUpload == null) {
            this.markerUpload = new FileUploadPanel("markerUpload", new IOperation() { //$NON-NLS-1$
                        @Override
                        public void run() {
                            ArContentEditPage.this.handler.removeMarker();
                        }
                    });
        }
        return this.markerUpload;
    }

    private TextField<String> getTitle() {
        if (this.title == null) {
            this.title = new TextField<>(EArContent_.title.getName(), new PropertyModel<String>(this.arContent, EArContent_.title.getName()));
            ValidatorUtil.setSimpleStringValidator(this.title, EArContent.class, EArContent_.title);
        }
        return this.title;
    }

    private void initialize() {
        this.add(getFeedback());
        this.add(getForm());
        this.add(getMarkerForm());
        this.add(getContentForm());
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

    private class Handler implements Serializable {

        void removeContent() {
            ArContentEditPage.this.arContent.getContent().clearData();
        }

        void removeMarker() {
            ArContentEditPage.this.arContent.getMarker().clearData();
        }

    }

    private class R extends AbstractResourceStream {

        private final ELargeData      data;
        private transient InputStream in;

        R(final ELargeData pData) {
            this.data = pData;
        }

        @Override
        public void close() {
            IOUtils.closeQuietly(this.in);
        }

        @Override
        public InputStream getInputStream() throws ResourceStreamNotFoundException {
            this.in = getInputStreamCore();
            return this.in;
        }

        private InputStream getInputStreamCore() throws ResourceStreamNotFoundException {
            if (this.data == null) {
                throw new ResourceStreamNotFoundException();
            }

            try {
                return getMarkerUpload().getInputStream();
            } catch (final NotFound e) {
                // 次の処理へ.
            }

            try {
                return ArContentEditPage.this.arContentService.getDataInputStream(this.data);
            } catch (final NotFound e) {
                throw new ResourceStreamNotFoundException();
            }
        }

    }
}
