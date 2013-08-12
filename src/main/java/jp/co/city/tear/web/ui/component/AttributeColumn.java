package jp.co.city.tear.web.ui.component;

import jabara.bean.BeanProperties;
import jabara.wicket.Models;

import javax.persistence.metamodel.Attribute;

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
}