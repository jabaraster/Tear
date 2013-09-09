/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.bean.BeanProperties;

import javax.persistence.metamodel.Attribute;

/**
 * @param <E> 行の値を保持するオブジェクトの型.
 * @author jabaraster
 */
public class DateTimeColumn<E> extends DateColumnBase<E> {
    private static final long  serialVersionUID = -9222794771582716966L;

    /**
     * 
     */
    public static final String FORMAT           = "yyyy/MM/dd HH:mm:ss"; //$NON-NLS-1$

    /**
     * @param pMeta -
     * @param pAttribute -
     */
    /**
     * @param pMeta
     * @param pAttribute
     */
    public DateTimeColumn(final BeanProperties pMeta, final Attribute<?, ? extends Comparable<?>> pAttribute) {
        super(pMeta, pAttribute, FORMAT);
    }

}
