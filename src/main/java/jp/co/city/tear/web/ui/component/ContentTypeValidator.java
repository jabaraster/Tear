/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.general.ArgUtil;

import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author jabaraster -
 */
public class ContentTypeValidator implements IValidator<List<FileUpload>> {
    private static final long serialVersionUID = 4152879158528889669L;

    private final String      type;

    /**
     * @param pType -
     */
    public ContentTypeValidator(final String pType) {
        this.type = pType;
    }

    /**
     * @see org.apache.wicket.validation.IValidator#validate(org.apache.wicket.validation.IValidatable)
     */
    @Override
    public void validate(final IValidatable<List<FileUpload>> pValidatable) {
        final List<FileUpload> uploads = pValidatable.getValue();
        if (uploads.isEmpty()) {
            return;
        }
        final String contentType = uploads.get(0).getContentType();
        if (!contentType.startsWith(this.type)) {
            final ValidationError error = new ValidationError(this);
            error.setVariable("contentType", contentType); //$NON-NLS-1$
            error.setVariable("type", this.type); //$NON-NLS-1$
            pValidatable.error(error);
        }
    }

    /**
     * typeが一致するかどうかを検証するインスタンスを生成します.
     * 
     * @param pType -
     * @return -
     */
    public static ContentTypeValidator typeCheck(final String pType) {
        ArgUtil.checkNullOrEmpty(pType, "pType"); //$NON-NLS-1$
        return new ContentTypeValidator(pType);
    }

}
