/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.wicket.CssUtil;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.beaneditor.BeanEditor;
import jabara.wicket.beaneditor.PropertyEditor;

import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.entity.EUserPassword_;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.model.Duplicate;
import jp.co.city.tear.service.IUserService;
import jp.co.city.tear.web.ui.AppSession;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValueConversionException;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public abstract class UserEditPage extends RestrictedPageBase {
    private static final long   serialVersionUID = 7454930682959012116L;

    /**
     * 
     */
    protected final EUser       userValue;
    private final PasswordValue passwordValue    = new PasswordValue();

    @Inject
    IUserService                userService;

    private final Handler       handler          = new Handler();

    private FeedbackPanel       feedback;

    private Form<?>             form;
    private BeanEditor<EUser>   editor;
    private PasswordTextField   password;
    private FeedbackPanel       passwordFeedback;
    private PasswordTextField   passwordConfirmation;
    private FeedbackPanel       passwordConfirmationFeedback;
    private AjaxButton          submitter;

    /**
     * 
     */
    public UserEditPage() {
        this(new EUser());
    }

    /**
     * @param pParameters -
     */
    public UserEditPage(final PageParameters pParameters) {
        super(pParameters);
        try {
            this.userValue = this.userService.findById(pParameters.get(0).toLong());
            initialize();
        } catch (StringValueConversionException | NotFound e) {
            throw new RestartResponseException(WebApplication.get().getHomePage());
        }
    }

    /**
     * @param pUser -
     */
    private UserEditPage(final EUser pUser) {
        this.userValue = pUser;
        initialize();
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addBodyCssReference(pResponse);
        CssUtil.addComponentCssReference(pResponse, UserEditPage.class);
        try {
            JavaScriptUtil.addFocusScript(pResponse, getEditor().findInputComponent(EUser_.userId.getName()).getFirstFormComponent());
        } catch (final NotFound e) {
            // 処理なし
        }
    }

    /**
     * @see org.apache.wicket.Page#onBeforeRender()
     */
    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        addAdministratorValidator();
        setAdministratorEditorVisibility();
    }

    @SuppressWarnings("serial")
    private void addAdministratorValidator() {
        final FormComponent<?> cp = getAdministratorEditor().getFirstFormComponent();
        cp.add(new IValidator<Object>() {
            @Override
            public void validate(final IValidatable<Object> pValidatable) {
                UserEditPage.this.handler.checkAdministration(pValidatable);
            }
        });
    }

    private PropertyEditor getAdministratorEditor() {
        try {
            return getEditor().findInputComponent(EUser_.administrator.getName());
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private BeanEditor<EUser> getEditor() {
        if (this.editor == null) {
            this.editor = new BeanEditor<>("editor", this.userValue); //$NON-NLS-1$
        }
        return this.editor;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new ComponentFeedbackPanel("feedback", this); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getFeedback());
            this.form.add(getEditor());
            this.form.add(getPassword());
            this.form.add(getPasswordFeedback());
            this.form.add(getPasswordConfirmation());
            this.form.add(getPasswordConfirmationFeedback());
            this.form.add(getSubmitter());
            this.form.add(new EqualPasswordInputValidator(getPassword(), getPasswordConfirmation()));
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            final String s = EUserPassword_.password.getName();
            this.password = new PasswordTextField(s, new PropertyModel<String>(this.passwordValue, s));
        }
        return this.password;
    }

    private PasswordTextField getPasswordConfirmation() {
        if (this.passwordConfirmation == null) {
            final String s = EUserPassword_.password.getName() + "Confirmation"; //$NON-NLS-1$
            this.passwordConfirmation = new PasswordTextField(s, new PropertyModel<String>(this.passwordValue, s));
        }
        return this.passwordConfirmation;
    }

    private FeedbackPanel getPasswordConfirmationFeedback() {
        if (this.passwordConfirmationFeedback == null) {
            this.passwordConfirmationFeedback = new ComponentFeedbackPanel(getPasswordConfirmation().getId() + "Feedback", getPasswordConfirmation()); //$NON-NLS-1$
        }
        return this.passwordConfirmationFeedback;
    }

    private FeedbackPanel getPasswordFeedback() {
        if (this.passwordFeedback == null) {
            this.passwordFeedback = new ComponentFeedbackPanel(getPassword().getId() + "Feedback", getPassword()); //$NON-NLS-1$
        }
        return this.passwordFeedback;
    }

    @SuppressWarnings("serial")
    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") { //$NON-NLS-1$
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    UserEditPage.this.handler.onError(pTarget);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    UserEditPage.this.handler.onSubmit(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private void initialize() {
        this.add(getForm());
    }

    private void setAdministratorEditorVisibility() {
        if (getSession().currentUserIsAdministrator()) {
            return;
        }
        getAdministratorEditor().setVisible(false);
    }

    /**
     * @param pUser -
     * @return -
     */
    public static PageParameters createParameters(final EUser pUser) {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
        if (!pUser.isPersisted()) {
            throw new IllegalArgumentException("永続化されていないエンティティは処理出来ません."); //$NON-NLS-1$
        }
        return createParameters(pUser.getId().longValue());
    }

    /**
     * @param pEUserId -
     * @return -
     */
    public static PageParameters createParameters(final long pEUserId) {
        final PageParameters ret = new PageParameters();
        ret.set(0, Long.valueOf(pEUserId));
        return ret;
    }

    private class Handler implements Serializable {
        private static final long        serialVersionUID   = 6149418547207914836L;

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender();

        private void checkAdministration(final IValidatable<Object> pValidatable) {
            if (!UserEditPage.this.userValue.isPersisted()) {
                return;
            }

            // ログインユーザが管理者ユーザの場合、自身を管理者でなくする操作は禁止する.
            final AppSession session = getSession();
            if (!session.currentUserIsAdministrator()) {
                return;
            }
            if (session.getLoginUser().getId() != UserEditPage.this.userValue.getId().longValue()) {
                return;
            }
            final Boolean admin = (Boolean) pValidatable.getValue();
            if (Boolean.FALSE.equals(admin)) {
                pValidatable.error(new ValidationError("自身を管理者でなくすることはできません.")); //$NON-NLS-1$
            }
        }

        private void onError(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getForm());
        }

        private void onSubmit(final AjaxRequestTarget pTarget) {
            try {
                UserEditPage.this.userService.insertOrUpdate(UserEditPage.this.userValue, getPassword().getModelObject());
                setResponsePage(UserListPage.class);
            } catch (final Duplicate e) {
                error("ユーザIDは既に使われています."); //$NON-NLS-1$
                this.errorClassAppender.addErrorClass(getForm());
                pTarget.add(getForm());
            }
        }
    }

    @SuppressWarnings("unused")
    private static class PasswordValue implements Serializable {
        private static final long serialVersionUID = 8620839379550673825L;

        private String            password;
        private String            passwordConfirmation;

        /**
         * @return the password
         */
        public String getPassword() {
            return this.password;
        }

        /**
         * @return the passwordConfirmation
         */
        public String getPasswordConfirmation() {
            return this.passwordConfirmation;
        }

        /**
         * @param pPassword the password to set
         */
        public void setPassword(final String pPassword) {
            this.password = pPassword;
        }

        /**
         * @param pPasswordConfirmation the passwordConfirmation to set
         */
        public void setPasswordConfirmation(final String pPasswordConfirmation) {
            this.passwordConfirmation = pPasswordConfirmation;
        }

    }
}
