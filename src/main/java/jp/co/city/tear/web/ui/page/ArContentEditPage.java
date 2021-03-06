/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.ArgUtil;
import jabara.general.IoUtil;
import jabara.general.NotFound;
import jabara.general.io.DataOperation;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.ContentTypeValidator;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.FileUploadPanel;
import jabara.wicket.Models;
import jabara.wicket.ValidatorUtil;

import java.io.InputStream;
import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.component.BodyCssHeaderItem;
import jp.co.city.tear.web.ui.component.RangeField;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
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
@SuppressWarnings({ "serial", "synthetic-access" })
public class ArContentEditPage extends RestrictedPageBase {
    private static final long   serialVersionUID           = 980535287221358707L;

    /**
     * 
     */
    public static final Float   SIMILARITY_THRESHOLD_MIN   = Float.valueOf(-1f);

    /**
     * 
     */
    public static final Float   SIMILARITY_THRESHOLD_MAX   = Float.valueOf(1f);

    /**
     * 
     */
    public static final Float   SIMILARITY_THRESHOLD_STEP  = Float.valueOf(0.05f);

    private static final String CLASS_VALUE_HAS_CONTENT    = "label-warning";     //$NON-NLS-1$
    private static final String CLASS_VALUE_HAS_NO_CONTENT = "label-default";     //$NON-NLS-1$

    private final EArContent    arContent;

    private final Handler       handler                    = new Handler();

    @Inject
    IArContentService           arContentService;

    private FeedbackPanel       feedback;

    private AjaxButton          submitter;
    private Link<?>             goIndex;

    private Form<?>             form;
    private FeedbackPanel       titleFeedback;
    private TextField<String>   title;
    private RangeField<Float>   similarityThreshold;

    private Form<?>             markerForm;
    private FileUploadPanel     markerUpload;
    private NonCachingImage     markerImage;
    private FeedbackPanel       markerUploadFeedback;

    private Form<?>             contentForm;
    private TextField<String>   contentDescription;
    private FileUploadPanel     contentUpload;
    private Label               contentLabel;
    private FeedbackPanel       contentUploadFeedback;

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
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);

        pResponse.render(BodyCssHeaderItem.get());
        pResponse.render(ComponentCssHeaderItem.forType(ArContentEditPage.class));

        pResponse.render(ComponentJavaScriptHeaderItem.forType(ArContentEditPage.class));
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
            this.form.add(getTitleFeedback());
            this.form.add(getTitle());
            this.form.add(getSimilarityThreshold());
        }
        return this.form;
    }

    AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter", getForm()) { //$NON-NLS-1$
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    ArContentEditPage.this.handler.onSubmitError(pTarget);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    ArContentEditPage.this.handler.onSubmit(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private TextField<String> getContentDescription() {
        if (this.contentDescription == null) {
            this.contentDescription = new TextField<>( //
                    EArContent_.contentDescription.getName() //
                    , new PropertyModel<String>(this.arContent, EArContent_.contentDescription.getName()) //
                    , String.class //
            );
            this.contentDescription.add(new OnChangeAjaxBehavior() {
                @Override
                protected void onUpdate(@SuppressWarnings("unused") final AjaxRequestTarget pTarget) {
                    // 処理なし(Modelに値を突っ込んでほしいだけなので).
                }
            });
            ValidatorUtil.setSimpleStringValidator(this.contentDescription, EArContent.class, EArContent_.contentDescription);
        }
        return this.contentDescription;
    }

    private Form<?> getContentForm() {
        if (this.contentForm == null) {
            this.contentForm = new Form<>("contentForm"); //$NON-NLS-1$
            this.contentForm.add(getContentDescription());
            this.contentForm.add(getContentUpload());
            this.contentForm.add(getContentLabel());
            this.contentForm.add(getContentUploadFeedback());
        }
        return this.contentForm;
    }

    @SuppressWarnings("nls")
    private Label getContentLabel() {
        if (this.contentLabel == null) {
            this.contentLabel = new Label("contentLabel", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return hasContentData() ? "動画あり" : "動画なし";
                }
            });
            final String c = hasContentData() ? CLASS_VALUE_HAS_CONTENT : CLASS_VALUE_HAS_NO_CONTENT;
            this.contentLabel.add(AttributeModifier.append("class", c));
        }
        return this.contentLabel;
    }

    private FileUploadPanel getContentUpload() {
        if (this.contentUpload == null) {
            this.contentUpload = new FileUploadPanel("contentUpload") { //$NON-NLS-1$
                @Override
                protected void onUploadError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    ArContentEditPage.this.handler.onContentUploadError(pTarget);
                }

                @Override
                protected void onUploadSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    ArContentEditPage.this.handler.onContentUploadSubmit(pTarget);
                }
            };
            this.contentUpload.getFile().add(ContentTypeValidator.type("video", "動画")); //$NON-NLS-1$//$NON-NLS-2$
        }
        return this.contentUpload;
    }

    private FeedbackPanel getContentUploadFeedback() {
        if (this.contentUploadFeedback == null) {
            this.contentUploadFeedback = new ComponentFeedbackPanel("contentUploadFeedback", getContentUpload().getFile()); //$NON-NLS-1$
        }
        return this.contentUploadFeedback;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new ComponentFeedbackPanel("feedback", this); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private Link<?> getGoIndex() {
        if (this.goIndex == null) {
            this.goIndex = new BookmarkablePageLink<>("goIndex", ArContentListPage.class); //$NON-NLS-1$
        }
        return this.goIndex;
    }

    private Form<?> getMarkerForm() {
        if (this.markerForm == null) {
            this.markerForm = new Form<>("markerForm"); //$NON-NLS-1$
            this.markerForm.add(getMarkerUpload());
            this.markerForm.add(getMarkerImage());
            this.markerForm.add(getMarkerUploadFeedback());
        }
        return this.markerForm;
    }

    private Image getMarkerImage() {
        if (this.markerImage == null) {
            this.markerImage = new NonCachingImage("markerImage", new ResourceStreamResource(new MarkerImageResourceStream())); //$NON-NLS-1$

            final String c = hasMarkerData() ? "hasImage" : "nonImage"; //$NON-NLS-1$ //$NON-NLS-2$
            this.markerImage.add(AttributeModifier.append("class", c)); //$NON-NLS-1$
        }
        return this.markerImage;
    }

    private FileUploadPanel getMarkerUpload() {
        if (this.markerUpload == null) {
            this.markerUpload = new FileUploadPanel("markerUpload") { //$NON-NLS-1$
                @Override
                protected void onUploadError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    ArContentEditPage.this.handler.onMarkerUploadError(pTarget);
                }

                @Override
                protected void onUploadSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    ArContentEditPage.this.handler.onMarkerUploadSubmit(pTarget);
                }
            };
            this.markerUpload.getFile().add(ContentTypeValidator.type("image", "画像")); //$NON-NLS-1$//$NON-NLS-2$
        }
        return this.markerUpload;
    }

    private FeedbackPanel getMarkerUploadFeedback() {
        if (this.markerUploadFeedback == null) {
            this.markerUploadFeedback = new ComponentFeedbackPanel("markerUploadFeedback", getMarkerUpload().getFile()); //$NON-NLS-1$
        }
        return this.markerUploadFeedback;
    }

    private WebMarkupContainer getSimilarityThreshold() {
        if (this.similarityThreshold == null) {
            this.similarityThreshold = new RangeField<>( //
                    "similarityThreshold" // //$NON-NLS-1$
                    , Float.class //
                    , new PropertyModel<Float>(this.arContent, EArContent_.similarityThreshold.getName()) //
                    , Models.of(SIMILARITY_THRESHOLD_MIN) //
                    , Models.of(SIMILARITY_THRESHOLD_MAX) //
                    , Models.of(SIMILARITY_THRESHOLD_STEP) //
            );
            this.similarityThreshold.setRangeValidator();
        }
        return this.similarityThreshold;
    }

    private TextField<String> getTitle() {
        if (this.title == null) {
            this.title = new TextField<>("title" // //$NON-NLS-1$
                    , new PropertyModel<String>(this.arContent, EArContent_.title.getName()) //
                    , String.class);
            ValidatorUtil.setSimpleStringValidator(this.title, EArContent.class, EArContent_.title);
        }
        return this.title;
    }

    private FeedbackPanel getTitleFeedback() {
        if (this.titleFeedback == null) {
            this.titleFeedback = new ComponentFeedbackPanel("titleFeedback", getTitle()); //$NON-NLS-1$
        }
        return this.titleFeedback;
    }

    private boolean hasContentData() {
        return this.arContent.getContent().hasData() || getContentUpload().getDataOperation().hasData();
    }

    private boolean hasMarkerData() {
        return this.arContent.getMarker().hasData() || getMarkerUpload().getDataOperation().hasData();
    }

    private void initialize() {
        this.add(getFeedback());
        this.add(getForm());
        this.add(getSubmitter());
        this.add(getGoIndex());
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

    @SuppressWarnings("nls")
    private static String buildClassValueReplaceScript(final Component pComponent, final String pRemoveValue, final String pAddValue) {
        if (!pComponent.getOutputMarkupId()) {
            throw new IllegalStateException();
        }
        final String markupId = pComponent.getMarkupId();
        return "$('#" + markupId + "').removeClass('" + pRemoveValue + "').addClass('" + pAddValue + "')";
    }

    private class Handler implements Serializable {

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender(Models.readOnly("error")); //$NON-NLS-1$

        void onContentUploadError(final AjaxRequestTarget pTarget) {
            pTarget.add(getContentUploadFeedback());
        }

        void onContentUploadSubmit(final AjaxRequestTarget pTarget) {
            // コンテンツがアップロードされたり削除されたら、class属性を書き換える.
            // 本来この処理はAttributeModifierを使ってWicketの領域で実装したいのだが
            // そうするとFileUploadPanelが正常に動作しない(アップロードされたデータがなぜか消えてしまう)
            pTarget.appendJavaScript(buildClassValueReplaceScript( //
                    getContentLabel() //
                    , ArContentEditPage.CLASS_VALUE_HAS_NO_CONTENT //
                    , ArContentEditPage.CLASS_VALUE_HAS_CONTENT));
            pTarget.add(getContentLabel());
            pTarget.add(getContentUploadFeedback());
        }

        void onMarkerUploadError(final AjaxRequestTarget pTarget) {
            pTarget.add(getMarkerUploadFeedback());
        }

        void onMarkerUploadSubmit(final AjaxRequestTarget pTarget) {
            pTarget.appendJavaScript(buildClassValueReplaceScript(getMarkerImage(), "nonImage", "hasImage")); //$NON-NLS-1$ //$NON-NLS-2$
            pTarget.add(getMarkerImage());
            pTarget.add(getMarkerUploadFeedback());
        }

        void onSubmit(final AjaxRequestTarget pTarget) {
            ArContentEditPage.this.arContentService.insertOrUpdate( //
                    getSession().getLoginUser() //
                    , ArContentEditPage.this.arContent //
                    , getMarkerUpload().getDataOperation() //
                    , getContentUpload().getDataOperation());
            info("保存しました！"); //$NON-NLS-1$

            this.errorClassAppender.addErrorClass(getForm());
            pTarget.appendJavaScript("setTimeout(function() { $('#" + getFeedback().getMarkupId() + "').hide('slow'); }, 1000);"); //$NON-NLS-1$ //$NON-NLS-2$

            pTarget.add(getTitle());
            pTarget.add(getTitleFeedback());
            pTarget.add(getFeedback());
        }

        void onSubmitError(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getTitle());
            pTarget.add(getTitleFeedback());
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
            if (dataOperation.hasData()) {
                this.stream = dataOperation.getData().getInputStream();
                return this.stream;
            }
            try {
                this.stream = ArContentEditPage.this.arContentService.getDataInputStream(ArContentEditPage.this.arContent.getMarker());
                return this.stream;
            } catch (final NotFound e) {
                throw new ResourceStreamNotFoundException();
            }
        }

    }
}
