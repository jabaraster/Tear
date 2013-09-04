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
import jp.co.city.tear.model.LargeDataOperation;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.component.FileUploadPanel;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
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
    private NonCachingImage         markerImage;

    private Form<?>                 contentForm;
    private FileUploadPanel         contentUpload;
    private Label                   contentLabel;

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
        addBodyCssReference(pResponse);
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
                public void onSubmit() {
                    ArContentEditPage.this.handler.save();
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

    private Form<?> getContentForm() {
        if (this.contentForm == null) {
            this.contentForm = new Form<>("contentForm"); //$NON-NLS-1$
            this.contentForm.add(getContentUpload());
            this.contentForm.add(getContentLabel());
        }
        return this.contentForm;
    }

    @SuppressWarnings("nls")
    private Label getContentLabel() {
        if (this.contentLabel == null) {
            this.contentLabel = new Label("contentLabel", new ContentLabelModel());
        }
        return this.contentLabel;
    }

    private FileUploadPanel getContentUpload() {
        if (this.contentUpload == null) {
            this.contentUpload = new FileUploadPanel("contentUpload"); //$NON-NLS-1$
            this.contentUpload.getRestorer().setVisible(this.arContent.getContent().hasData());
        }
        return this.contentUpload;
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

    private NonCachingImage getMarkerImage() {
        if (this.markerImage == null) {
            try (final LargeDataResourceStream stream = new LargeDataResourceStream()) {
                final ResourceStreamResource resource = new ResourceStreamResource(stream);
                this.markerImage = new NonCachingImage("markerImage", resource); //$NON-NLS-1$
            }
        }
        return this.markerImage;
    }

    private FileUploadPanel getMarkerUpload() {
        if (this.markerUpload == null) {
            this.markerUpload = new FileUploadPanel("markerUpload"); //$NON-NLS-1$
            this.markerUpload.getRestorer().setVisible(this.arContent.getMarker().hasData());
        }
        return this.markerUpload;
    }

    private TextField<String> getTitle() {
        if (this.title == null) {
            this.title = new TextField<>(EArContent_.title.getName(), new PropertyModel<String>(this.arContent, EArContent_.title.getName()));
            ValidatorUtil.setSimpleStringValidator(this.title, EArContent.class, EArContent_.title);

            // タイトルを入力した後に「保存」を押さずにファイルをアップロードすると入力内容が消えてしまう.
            // この現象に対処するため、タイトルテキストの変更内容を随時Ajaxで送ってもらうようにする.
            this.title.add(new OnChangeAjaxBehavior() {
                @Override
                protected void onUpdate(@SuppressWarnings("unused") final AjaxRequestTarget pTarget) {
                    // 画面更新の必要はないので、ここで行う処理はない.
                }
            });
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

    private class ContentLabelModel extends AbstractReadOnlyModel<String> {
        @SuppressWarnings("nls")
        @Override
        public String getObject() {
            try (LargeDataOperation op = getContentUpload().getOperation()) {
                switch (op.getMode()) {
                case DELETE:
                    return "コンテンツなし";
                case NOOP:
                    return ArContentEditPage.this.arContent.getContent().hasData() ? "コンテンツあり" : "コンテンツなし";
                case UPDATE:
                    return op.getData() == null ? "コンテンツなし" : "コンテンツあり";
                default:
                    throw new IllegalStateException();
                }
            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }

    }

    private class Handler implements Serializable {

        private void save() {
            try (LargeDataOperation markerDataOperation = getMarkerUpload().getOperation(); //
                    LargeDataOperation contentDataOperation = getContentUpload().getOperation()) {
                ArContentEditPage.this.arContentService.insertOrUpdate( //
                        getSession().getLoginUser() //
                        , ArContentEditPage.this.arContent //
                        , markerDataOperation //
                        , contentDataOperation);
                setResponsePage(ArContentEditPage.class, ArContentEditPage.createParameters(ArContentEditPage.this.arContent));

            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
    }

    private class LargeDataResourceStream extends AbstractResourceStream {

        private transient InputStream in;

        @Override
        public void close() {
            IOUtils.closeQuietly(this.in);
            this.in = null;
        }

        @Override
        public InputStream getInputStream() throws ResourceStreamNotFoundException {
            this.in = getInputStreamCore();
            return this.in;
        }

        @SuppressWarnings("resource")
        private InputStream getInputStreamCore() throws ResourceStreamNotFoundException {
            final LargeDataOperation operation = getMarkerUpload().getOperation();
            switch (operation.getMode()) {
            case DELETE:
                throw new ResourceStreamNotFoundException();
            case UPDATE:
                return operation.getData();
            case NOOP:
                try {
                    return ArContentEditPage.this.arContentService.getDataInputStream(ArContentEditPage.this.arContent.getMarker());
                } catch (final NotFound e1) {
                    throw new ResourceStreamNotFoundException();
                }
            default:
                throw new IllegalStateException();
            }
        }
    }
}
