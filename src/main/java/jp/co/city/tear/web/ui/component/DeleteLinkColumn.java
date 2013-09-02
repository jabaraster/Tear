/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.general.IProducer2;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @param <E> -
 * @author jabaraster
 */
public class DeleteLinkColumn<E> extends BootstrapLinkColumn<E> {
    private static final long serialVersionUID = 4289217514684795082L;

    /**
     * @param pLinkLabelModel -
     * @param pDestination -
     * @param pParametersProducer -
     */
    public DeleteLinkColumn( //
            final IModel<String> pLinkLabelModel //
            , final Class<? extends Page> pDestination //
            , final IProducer2<E, PageParameters> pParametersProducer //
    ) {
        super(pLinkLabelModel, pDestination, pParametersProducer, IconType.TRASH, ButtonType.WARNING);
    }
}
