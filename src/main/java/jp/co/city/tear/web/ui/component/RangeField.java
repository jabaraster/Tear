/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.general.ArgUtil;
import jabara.general.Empty;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * @param <C> -
 * @author jabaraster
 */
public class RangeField<C extends Comparable<C> & Serializable> extends FormComponent<C> {
    private static final long serialVersionUID = 8160930587883797894L;

    private IModel<C>         minModel;
    private IModel<C>         maxModel;
    private IModel<C>         stepModel;

    /**
     * @param pId -
     */
    public RangeField(final String pId) {
        super(pId);
    }

    /**
     * @param pId -
     * @param pValueType -
     * @param pValueModel -
     * @param pMinModel -
     * @param pMaxModel -
     * @param pStepModel -
     */
    public RangeField( //
            final String pId //
            , final Class<C> pValueType //
            , final IModel<C> pValueModel //
            , final IModel<C> pMinModel //
            , final IModel<C> pMaxModel //
            , final IModel<C> pStepModel //
    ) {
        super(pId, pValueModel);

        ArgUtil.checkNull(pValueType, "pValueType"); //$NON-NLS-1$
        ArgUtil.checkNull(pMinModel, "pMinModel"); //$NON-NLS-1$
        ArgUtil.checkNull(pMaxModel, "pMaxModel"); //$NON-NLS-1$
        ArgUtil.checkNull(pStepModel, "pStepModel"); //$NON-NLS-1$

        setType(pValueType);

        this.minModel = pMinModel;
        this.maxModel = pMaxModel;
        this.stepModel = pStepModel;
    }

    /**
     * @param pId -
     * @param pValueModel -
     */
    public RangeField(final String pId, final IModel<C> pValueModel) {
        super(pId, pValueModel);
    }

    /**
     * @return このオブジェクト自身.
     */
    public RangeField<C> setRangeValidator() {
        final C min = c(this.minModel);
        final C max = c(this.maxModel);
        if (min == null && max != null) {
            this.add(RangeValidator.maximum(max));

        } else if (min != null && max == null) {
            this.add(RangeValidator.minimum(min));

        } else if (min != null && max != null) {
            this.add(RangeValidator.range(min, max));

        } else {
            // 処理なし
        }
        return this;
    }

    /**
     * @see org.apache.wicket.markup.html.form.FormComponent#onComponentTag(org.apache.wicket.markup.ComponentTag)
     */
    @Override
    protected void onComponentTag(final ComponentTag pTag) {
        super.onComponentTag(pTag);

        putIfNotNull(pTag, getModel(), "value"); //$NON-NLS-1$
        putIfNotNull(pTag, this.minModel, "min"); //$NON-NLS-1$
        putIfNotNull(pTag, this.maxModel, "max"); //$NON-NLS-1$
        putIfNotNull(pTag, this.stepModel, "step"); //$NON-NLS-1$
    }

    private C c(final IModel<C> pModel) {
        if (pModel == null) {
            return null;
        }
        return pModel.getObject();
    }

    private static <T> void putIfNotNull(final ComponentTag pTag, final IModel<T> pModel, final String pAttributeName) {
        if (pModel != null) {
            pTag.put(pAttributeName, s(pModel.getObject()));
        }
    }

    private static CharSequence s(final Object o) {
        return o == null ? Empty.STRING : o.toString();
    }
}
