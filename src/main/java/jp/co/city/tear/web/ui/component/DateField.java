/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * TODO value属性の処理が不完全.
 * 
 * @author jabaraster
 */
public class DateField extends FormComponent<Date> {
    private static final long   serialVersionUID = 4106555986867442354L;

    private static final String FORMAT           = "yyyy-MM-dd";        //$NON-NLS-1$

    /**
     * @param pId -
     */
    public DateField(final String pId) {
        this(pId, null);
    }

    /**
     * @param pId -
     * @param pModel -
     */
    public DateField(final String pId, final IModel<Date> pModel) {
        super(pId, pModel);
        setType(Date.class);
    }

    /**
     * @see org.apache.wicket.Component#getConverter(java.lang.Class)
     */
    @Override
    public <C> IConverter<C> getConverter(final Class<C> pType) {
        if (!Date.class.isAssignableFrom(pType)) {
            throw new UnsupportedOperationException();
        }

        return new IConverter<C>() {
            private static final long serialVersionUID = -6599704739269932842L;

            @SuppressWarnings("unchecked")
            @Override
            public C convertToObject(final String pValue, @SuppressWarnings("unused") final Locale pLocale) {
                try {
                    return (C) new SimpleDateFormat(FORMAT).parse(pValue);
                } catch (final ParseException e) {
                    return null;
                }
            }

            @Override
            public String convertToString(final C pValue, @SuppressWarnings("unused") final Locale pLocale) {
                if (pValue == null) {
                    return null;
                }
                return new SimpleDateFormat(FORMAT).format(pValue);
            }
        };
    }

    /**
     * {@link TextField} のonComponentTagを参考に実装.
     * 
     * @see org.apache.wicket.markup.html.form.FormComponent#onComponentTag(org.apache.wicket.markup.ComponentTag)
     */
    @SuppressWarnings("nls")
    @Override
    protected void onComponentTag(final ComponentTag pTag) {
        // Must be attached to an input tag
        checkComponentTag(pTag, "input");

        // check for text type
        if (pTag.getAttributes().containsKey("type")) {
            checkComponentTagAttribute(pTag, "type", "date");
        }

        pTag.put("value", getValue());

        // Default handling for component tag
        super.onComponentTag(pTag);
    }
}
