/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.bean.BeanProperties;
import jabara.wicket.Models;

import java.text.SimpleDateFormat;

import javax.persistence.metamodel.Attribute;

import org.apache.wicket.model.IModel;

/**
 * @param <E> 行の値を保持するオブジェクトの型.
 * @author jabaraster
 */
public class DateColumnBase<E> extends AttributeColumn<E> {
    private static final long serialVersionUID = -1114541703952510486L;

    private final String      format;

    /**
     * @param pMeta -
     * @param pAttribute -
     * @param pFormat -
     */
    @SuppressWarnings("unused")
    public DateColumnBase( //
            final BeanProperties pMeta //
            , final Attribute<?, ? extends Comparable<?>> pAttribute //
            , final String pFormat) {

        super(pMeta, pAttribute);
        this.format = pFormat;
        new SimpleDateFormat(pFormat); // 書式の妥当性チェック
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn#getDataModel(org.apache.wicket.model.IModel)
     */
    @Override
    public IModel<Object> getDataModel(final IModel<E> pRowModel) {
        final IModel<Object> model = super.getDataModel(pRowModel);
        final Object data = model.getObject();
        if (data == null) {
            return model;
        }
        return Models.<Object> readOnly(new SimpleDateFormat(this.format).format(data));
    }
}
