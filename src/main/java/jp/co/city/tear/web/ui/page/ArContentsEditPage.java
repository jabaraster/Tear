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

import jp.co.city.tear.entity.EArContents;
import jp.co.city.tear.entity.EArContents_;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.IArContentsService;
import jp.co.city.tear.web.ui.AppSession;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * @author jabaraster
 */
@SuppressWarnings({ "synthetic-access", "serial" })
public class ArContentsEditPage extends RestrictedPageBase {
    private static final long serialVersionUID = -4884364205385771240L;

    private final EArContents arContents;

    private final Handler     handler          = new Handler();

    @Inject
    IArContentsService        arContentsService;

    private FeedbackPanel     feedback;
    private Form<?>           form;
    private TextField<String> title;
    private FileUploadField   marker;
    private FileUploadField   contents;
    private Button            submitter;

    /**
     * 
     */
    public ArContentsEditPage() {
        this(new EArContents());
    }

    /**
     * @param pArContents -
     */
    public ArContentsEditPage(final EArContents pArContents) {
        ArgUtil.checkNull(pArContents, "pArContents"); //$NON-NLS-1$
        this.arContents = pArContents;
        initialize();
    }

    /**
     * @param pParameters -
     */
    public ArContentsEditPage(final PageParameters pParameters) {
        super(pParameters);
        try {
            this.arContents = this.arContentsService.findById(AppSession.get().getLoginUser(), pParameters.get(0).toLong());
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
        CssUtil.addComponentCssReference(pResponse, ArContentsEditPage.class);
        JavaScriptUtil.addFocusScript(pResponse, getTitle());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ARコンテンツの編集"); //$NON-NLS-1$
    }

    private FileUploadField getContents() {
        if (this.contents == null) {
            this.contents = new FileUploadField(EArContents_.contents.getName());
        }
        return this.contents;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getFeedback());
            this.form.add(getTitle());
            this.form.add(getMarker());
            this.form.add(getContents());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private FileUploadField getMarker() {
        if (this.marker == null) {
            this.marker = new FileUploadField(EArContents_.marker.getName());
        }
        return this.marker;
    }

    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new Button("submitter") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    ArContentsEditPage.this.handler.onSubmit();
                }
            };
        }
        return this.submitter;
    }

    private TextField<String> getTitle() {
        if (this.title == null) {
            this.title = new TextField<>(EArContents_.title.getName(), new PropertyModel<String>(this.arContents, EArContents_.title.getName()));
            ValidatorUtil.setSimpleStringValidator(this.title, EArContents.class, EArContents_.title);
        }
        return this.title;
    }

    private void initialize() {
        this.add(getForm());
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
                    final InputStream contentsData = getDataFromFileUpload(getContents()) //
            ) {
                ArContentsEditPage.this.arContents.setMarker(new ELargeData(markerData));
                ArContentsEditPage.this.arContents.setContents(new ELargeData(contentsData));
                ArContentsEditPage.this.arContentsService.insertOrUpdate( //
                        AppSession.get().getLoginUser() //
                        , ArContentsEditPage.this.arContents //
                        );
            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
    }
}
