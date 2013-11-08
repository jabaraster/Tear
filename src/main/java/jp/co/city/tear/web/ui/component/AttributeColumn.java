package jp.co.city.tear.web.ui.component;

import jabara.bean.BeanProperties;
import jabara.general.ArgUtil;
import jabara.wicket.Models;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;

/**
 * @param <E> 行の値となるオブジェクトの型.
 * @author jabaraster
 */
public class AttributeColumn<E> extends PropertyColumn<E, String> {
    private static final long serialVersionUID = -8311427284072422312L;

    /**
     * @param pMeta -
     * @param pAttribute -
     */
    public AttributeColumn(final BeanProperties pMeta, final Attribute<?, ? extends Comparable<?>> pAttribute) {
        super( //
                Models.readOnly(pMeta.get(pAttribute.getName()).getLocalizedName()) //
                , pAttribute.getName() //
                , pAttribute.getName() //
        );
    }

    private AttributeColumn(final BeanProperties pMeta, final Attribute<?, ?> pAttribute //
            , @SuppressWarnings("unused") final Object pDummy) {
        super( //
                Models.readOnly(pMeta.get(pAttribute.getName()).getLocalizedName()) //
                , pAttribute.getName() //
        );
    }

    /**
     * @param <V> -
     * @param pMeta -
     * @param pAttribute -
     * @return -
     */
    public static <V> AttributeColumn<V> sortable(final BeanProperties pMeta, final SingularAttribute<?, ? extends Comparable<?>> pAttribute) {
        ArgUtil.checkNull(pMeta, "pMeta"); //$NON-NLS-1$
        ArgUtil.checkNull(pAttribute, "pAttribute"); //$NON-NLS-1$
        return new AttributeColumn<>(pMeta, pAttribute);
    }

    /**
     * @param <V> -
     * @param pMeta -
     * @param pAttribute -
     * @return -
     */
    public static <V> AttributeColumn<V> unsortable(final BeanProperties pMeta, final Attribute<?, ?> pAttribute) {
        ArgUtil.checkNull(pMeta, "pMeta"); //$NON-NLS-1$
        ArgUtil.checkNull(pAttribute, "pAttribute"); //$NON-NLS-1$
        return new AttributeColumn<>(pMeta, pAttribute, null);
    }
}