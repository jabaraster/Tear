package jp.co.city.tear.web.ui.page;

import jabara.general.Empty;
import jabara.wicket.CssUtil;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;

import java.io.Serializable;

import jp.co.city.tear.model.FailAuthentication;
import jp.co.city.tear.web.ui.AppSession;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.StringValue;

/**
 * 
 */
@SuppressWarnings("synthetic-access")
public class LoginPage extends WebPageBase {
    private static final long serialVersionUID = 1925170327965147328L;

    private final Handler     handler          = new Handler();

    private FeedbackPanel     feedback;
    private StatelessForm<?>  form;
    private TextField<String> userId;
    private FeedbackPanel     userIdFeedback;
    private PasswordTextField password;
    private FeedbackPanel     passwordFeedback;
    private Button            submitter;

    /**
     * 
     */
    public LoginPage() {
        this.add(getFeedback());
        this.add(getForm());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, LoginPage.class);
        JavaScriptUtil.addFocusScript(pResponse, getUserId());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#createHeaderPanel(java.lang.String)
     */
    @Override
    protected Panel createHeaderPanel(final String pId) {
        return new EmptyPanel(pId);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly(getString("pageTitle")); //$NON-NLS-1$
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new ComponentFeedbackPanel("feedback", this); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private StatelessForm<?> getForm() {
        if (this.form == null) {
            this.form = new StatelessForm<>("form"); //$NON-NLS-1$
            this.form.add(getUserId());
            this.form.add(getUserIdFeedback());
            this.form.add(getPassword());
            this.form.add(getPasswordFeedback());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            this.password = new PasswordTextField("password", Models.of(Empty.STRING)); //$NON-NLS-1$
        }
        return this.password;
    }

    private FeedbackPanel getPasswordFeedback() {
        if (this.passwordFeedback == null) {
            this.passwordFeedback = new ComponentFeedbackPanel("passwordFeedback", getPassword()); //$NON-NLS-1$
        }
        return this.passwordFeedback;
    }

    @SuppressWarnings("serial")
    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new Button("submitter") { //$NON-NLS-1$
                @Override
                public void onError() {
                    LoginPage.this.handler.onSubmitterError();
                }

                @Override
                public void onSubmit() {
                    LoginPage.this.handler.tryLogin();
                }
            };
        }
        return this.submitter;
    }

    private TextField<String> getUserId() {
        if (this.userId == null) {
            this.userId = new TextField<>("userId", Models.of(Empty.STRING)); //$NON-NLS-1$
            this.userId.setRequired(true);
        }
        return this.userId;
    }

    private FeedbackPanel getUserIdFeedback() {
        if (this.userIdFeedback == null) {
            this.userIdFeedback = new ComponentFeedbackPanel("userIdFeedback", getUserId()); //$NON-NLS-1$
        }
        return this.userIdFeedback;
    }

    private class Handler implements Serializable {
        private static final long        serialVersionUID   = 6317461189636878176L;

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender();

        private void onSubmitterError() {
            this.errorClassAppender.addErrorClass(getForm());
        }

        private void tryLogin() {
            try {
                AppSession.get().login(getUserId().getModelObject(), getPassword().getModelObject());
                final StringValue url = getPageParameters().get("u"); //$NON-NLS-1$
                if (!url.isEmpty() && !url.isNull()) {
                    setResponsePage(new RedirectPage(url.toString()));
                } else {
                    setResponsePage(getApplication().getHomePage());
                }
            } catch (final FailAuthentication e) {
                error(getString("message.failLogin")); //$NON-NLS-1$
                this.errorClassAppender.addErrorClass(getForm());
            }
        }
    }

}
