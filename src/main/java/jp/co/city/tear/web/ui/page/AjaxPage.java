/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class AjaxPage extends WebPageBase {
    private static final long  serialVersionUID = 4650212383556797093L;

    private final Handler      handler          = new Handler();

    private Link<?>            reloader;

    private Form<?>            fileForm;
    private Label              now;
    private FileUploadField    upload;
    private TextField<String>  text;
    private AjaxButton         ajaxUploader;
    private WebMarkupContainer customAjaxUploader;
    private Button             submitUploader;

    /**
     * 
     */
    public AjaxPage() {
        this.add(getFileForm());
        this.add(getReloader());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addBodyCssReference(pResponse);
        JavaScriptUtil.addComponentJavaScriptReference(pResponse, AjaxPage.class);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("Ajaxの研究"); //$NON-NLS-1$
    }

    private AjaxButton getAjaxUploader() {
        if (this.ajaxUploader == null) {
            this.ajaxUploader = new IndicatingAjaxButton("ajaxUploader") { //$NON-NLS-1$
                @SuppressWarnings("unused")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, final Form<?> pForm) {
                    AjaxPage.this.handler.debug();
                }
            };
        }
        return this.ajaxUploader;
    }

    private WebMarkupContainer getCustomAjaxUploader() {
        if (this.customAjaxUploader == null) {
            this.customAjaxUploader = new WebMarkupContainer("customAjaxUploader"); //$NON-NLS-1$
            this.customAjaxUploader.add(new UploadBehavior());
        }
        return this.customAjaxUploader;
    }

    private Form<?> getFileForm() {
        if (this.fileForm == null) {
            this.fileForm = new Form<>("fileForm"); //$NON-NLS-1$
            this.fileForm.add(getAjaxUploader());
            this.fileForm.add(getCustomAjaxUploader());
            this.fileForm.add(getSubmitUploader());
            this.fileForm.add(getNow());
            this.fileForm.add(getUpload());
            this.fileForm.add(getText());
        }
        return this.fileForm;
    }

    @SuppressWarnings("nls")
    private Label getNow() {
        if (this.now == null) {
            this.now = new Label("now", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                }
            });
        }
        return this.now;
    }

    private Link<?> getReloader() {
        if (this.reloader == null) {
            this.reloader = new BookmarkablePageLink<>("reloader", this.getClass()); //$NON-NLS-1$
        }
        return this.reloader;
    }

    private Button getSubmitUploader() {
        if (this.submitUploader == null) {
            this.submitUploader = new Button("submitUploader") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    AjaxPage.this.handler.debug();
                }
            };
        }
        return this.submitUploader;
    }

    private TextField<String> getText() {
        if (this.text == null) {
            this.text = new TextField<>("text", new Model<String>()); //$NON-NLS-1$
        }
        return this.text;
    }

    private FileUploadField getUpload() {
        if (this.upload == null) {
            this.upload = new FileUploadField("upload"); //$NON-NLS-1$
        }
        return this.upload;
    }

    private class Handler implements Serializable {

        private void debug() {
            jabara.Debug.write(getUpload().getFileUpload());
            jabara.Debug.write(getText().getModelObject());
        }
    }

    private class UploadBehavior extends AbstractDefaultAjaxBehavior {
        @SuppressWarnings("nls")
        @Override
        public void renderHead(final Component pComponent, final IHeaderResponse pResponse) {
            super.renderHead(pComponent, pResponse);

            final String script = "$(function() { prepareFileUpload('${buttonId}', '${formId}', '${callbackUrl}'); });";
            final Map<String, Object> params = new HashMap<>();
            params.put("buttonId", getCustomAjaxUploader().getMarkupId());
            params.put("formId", getFileForm().getMarkupId());
            params.put("callbackUrl", getCallbackUrl());

            final MapVariableInterpolator s = new MapVariableInterpolator(script, params);
            pResponse.render(JavaScriptHeaderItem.forScript(s.toString(), null));
        }

        @Override
        protected void respond(@SuppressWarnings("unused") final AjaxRequestTarget pTarget) {
            AjaxPage.this.handler.debug();
        }
    }
}
