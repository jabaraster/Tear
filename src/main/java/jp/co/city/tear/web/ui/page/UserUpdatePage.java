/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.NotFound;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ErrorClassAppender;

import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.model.Duplicate;
import jp.co.city.tear.model.UnmatchPassword;
import jp.co.city.tear.service.IUserService;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.component.BodyCssHeaderItem;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * @author jabaraster
 */
public class UserUpdatePage extends RestrictedPageBase {
    private static final long   serialVersionUID = -1979174828823637569L;

    /**
     * 
     */
    protected final EUser       userValue;

    @SuppressWarnings("synthetic-access")
    private final PasswordValue passwordValue    = new PasswordValue();

    @Inject
    IUserService                userService;

    @SuppressWarnings("synthetic-access")
    private final Handler       handler          = new Handler();

    private Label               userId;

    private Form<?>             authorityForm;
    private CheckBox            administrator;
    private AjaxButton          authoritySubmitter;

    private Form<?>             passwordForm;
    private FeedbackPanel       feedback;
    private PasswordTextField   currentPassword;
    private FeedbackPanel       currentPasswordFeedback;
    private PasswordTextField   newPassword;
    private FeedbackPanel       newPasswordFeedback;
    private PasswordTextField   newPasswordConfirmation;
    private FeedbackPanel       newPasswordConfirmationFeedback;
    private AjaxButton          passwordSubmitter;

    /**
     * @param pParameters -
     */
    public UserUpdatePage(final PageParameters pParameters) {
        super(pParameters);
        try {
            this.userValue = this.userService.findById(pParameters.get(0).toLong());
            initialize();
        } catch (StringValueConversionException | NotFound e) {
            throw new RestartResponseException(WebApplication.get().getHomePage());
        }
    }

    /**
     * @see jp.co.city.tear.web.ui.page.RestrictedPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(BodyCssHeaderItem.get());
        pResponse.render(ComponentCssHeaderItem.forType(UserUpdatePage.class));
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Model.of("ユーザ情報更新"); //$NON-NLS-1$
    }

    private CheckBox getAdministrator() {
        if (this.administrator == null) {
            this.administrator = new CheckBox( //
                    EUser_.administrator.getName() //
                    , new PropertyModel<Boolean>(this.userValue, EUser_.administrator.getName()));
        }
        return this.administrator;
    }

    @SuppressWarnings("serial")
    private Form<?> getAuthorityForm() {
        if (this.authorityForm == null) {
            this.authorityForm = new Form<Object>("authorityForm") { //$NON-NLS-1$
                @Override
                public boolean isVisible() {
                    return UserUpdatePage.this.userService.enableDelete(AppSession.get().getLoginUser(), UserUpdatePage.this.userValue);
                }
            };
            this.authorityForm.add(getAdministrator());
            this.authorityForm.add(getAuthoritySubmitter());
        }
        return this.authorityForm;
    }

    @SuppressWarnings("serial")
    private AjaxButton getAuthoritySubmitter() {
        if (this.authoritySubmitter == null) {
            this.authoritySubmitter = new IndicatingAjaxButton("authoritySubmitter") { //$NON-NLS-1$
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    UserUpdatePage.this.handler.onAuthoritySubmit(pTarget);
                }
            };
        }
        return this.authoritySubmitter;
    }

    private PasswordTextField getCurrentPassword() {
        if (this.currentPassword == null) {
            final String s = "currentPassword"; //$NON-NLS-1$
            this.currentPassword = new PasswordTextField(s, new PropertyModel<String>(this.passwordValue, s));
        }
        return this.currentPassword;
    }

    private FeedbackPanel getCurrentPasswordFeedback() {
        if (this.currentPasswordFeedback == null) {
            this.currentPasswordFeedback = new ComponentFeedbackPanel(getCurrentPassword().getId() + "Feedback", getCurrentPassword()); //$NON-NLS-1$
        }
        return this.currentPasswordFeedback;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new ComponentFeedbackPanel("feedback", this); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private PasswordTextField getNewPassword() {
        if (this.newPassword == null) {
            final String s = "newPassword"; //$NON-NLS-1$
            this.newPassword = new PasswordTextField(s, new PropertyModel<String>(this.passwordValue, s));
        }
        return this.newPassword;
    }

    private PasswordTextField getNewPasswordConfirmation() {
        if (this.newPasswordConfirmation == null) {
            final String s = getNewPassword().getId() + "Confirmation"; //$NON-NLS-1$
            this.newPasswordConfirmation = new PasswordTextField(s, new PropertyModel<String>(this.passwordValue, s));
        }
        return this.newPasswordConfirmation;
    }

    private FeedbackPanel getNewPasswordConfirmationFeedback() {
        if (this.newPasswordConfirmationFeedback == null) {
            this.newPasswordConfirmationFeedback = new ComponentFeedbackPanel(
                    getNewPasswordConfirmation().getId() + "Feedback", getNewPasswordConfirmation()); //$NON-NLS-1$
        }
        return this.newPasswordConfirmationFeedback;
    }

    private FeedbackPanel getNewPasswordFeedback() {
        if (this.newPasswordFeedback == null) {
            this.newPasswordFeedback = new ComponentFeedbackPanel(getNewPassword().getId() + "Feedback", getNewPassword()); //$NON-NLS-1$
        }
        return this.newPasswordFeedback;
    }

    private Form<?> getPasswordForm() {
        if (this.passwordForm == null) {
            this.passwordForm = new Form<>("passwordForm"); //$NON-NLS-1$
            this.passwordForm.add(getFeedback());
            this.passwordForm.add(getCurrentPassword());
            this.passwordForm.add(getCurrentPasswordFeedback());
            this.passwordForm.add(getNewPassword());
            this.passwordForm.add(getNewPasswordFeedback());
            this.passwordForm.add(getNewPasswordConfirmation());
            this.passwordForm.add(getNewPasswordConfirmationFeedback());
            this.passwordForm.add(getPasswordSubmitter());
            this.passwordForm.add(new EqualPasswordInputValidator(getNewPassword(), getNewPasswordConfirmation()));
        }
        return this.passwordForm;
    }

    @SuppressWarnings("serial")
    private AjaxButton getPasswordSubmitter() {
        if (this.passwordSubmitter == null) {
            this.passwordSubmitter = new IndicatingAjaxButton("passwordSubmitter") { //$NON-NLS-1$
                @SuppressWarnings("synthetic-access")
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    UserUpdatePage.this.handler.onError(pTarget);
                }

                @SuppressWarnings("synthetic-access")
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    UserUpdatePage.this.handler.onPasswordSubmit(pTarget);
                }
            };
        }
        return this.passwordSubmitter;
    }

    private Label getUserId() {
        if (this.userId == null) {
            this.userId = new Label(EUser_.userId.getName(), new PropertyModel<String>(this.userValue, EUser_.userId.getName()));
        }
        return this.userId;
    }

    private void initialize() {
        this.add(getUserId());
        this.add(getAuthorityForm());
        this.add(getPasswordForm());
    }

    @SuppressWarnings("synthetic-access")
    private class Handler implements Serializable {
        private static final long        serialVersionUID   = 6149418547207914836L;

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender();

        private void onAuthoritySubmit(final AjaxRequestTarget pTarget) {
            try {
                UserUpdatePage.this.userService.update(UserUpdatePage.this.userValue);
                setResponsePage(UserListPage.class);
            } catch (final Duplicate e) {
                error("ユーザIDは既に使われています."); //$NON-NLS-1$
                this.errorClassAppender.addErrorClass(getPasswordForm());
                pTarget.add(getPasswordForm());
            }
        }

        private void onError(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getPasswordForm());
            pTarget.add(getPasswordForm());
        }

        private void onPasswordSubmit(final AjaxRequestTarget pTarget) {
            try {
                UserUpdatePage.this.userService.updatePassword( //
                        UserUpdatePage.this.userValue //
                        , getCurrentPassword().getModelObject() //
                        , getNewPassword().getModelObject() //
                        );
                setResponsePage(UserListPage.class);
            } catch (final UnmatchPassword e) {
                error("パスワードが一致しません."); //$NON-NLS-1$
                this.errorClassAppender.addErrorClass(getPasswordForm());
                pTarget.add(getPasswordForm());
            }
        }
    }

    @SuppressWarnings("unused")
    private static class PasswordValue implements Serializable {
        private static final long serialVersionUID = 8620839379550673825L;

        private String            currentPassword;
        private String            newPassword;
        private String            newPasswordConfirmation;

        /**
         * @return currentPasswordを返す.
         */
        public String getCurrentPassword() {
            return this.currentPassword;
        }

        /**
         * @return the newPassword
         */
        public String getNewPassword() {
            return this.newPassword;
        }

        /**
         * @return the newPasswordConfirmation
         */
        public String getNewPasswordConfirmation() {
            return this.newPasswordConfirmation;
        }

        /**
         * @param pCurrentPassword currentPasswordを設定.
         */
        public void setCurrentPassword(final String pCurrentPassword) {
            this.currentPassword = pCurrentPassword;
        }

        /**
         * @param pPassword the newPassword to set
         */
        public void setNewPassword(final String pPassword) {
            this.newPassword = pPassword;
        }

        /**
         * @param pPasswordConfirmation the newPasswordConfirmation to set
         */
        public void setNewPasswordConfirmation(final String pPasswordConfirmation) {
            this.newPasswordConfirmation = pPasswordConfirmation;
        }
    }
}
