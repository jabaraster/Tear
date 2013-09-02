package jp.co.city.tear.web.ui.component;

import jabara.general.ArgUtil;
import jabara.general.IProducer2;
import jabara.wicket.Models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @param <E> 行の値となるオブジェクトの型.
 * @author jabaraster
 */
public class LinkColumn<E> extends AbstractColumn<E, String> {
    private static final long                   serialVersionUID  = 7430577515667494582L;

    private static final IModel<String>         EMPTY_LABEL_MODEL = Models.readOnly("　"); //$NON-NLS-1$

    private final IModel<String>                linkLabelModel;
    private final Class<? extends Page>         destination;
    private final IProducer2<E, PageParameters> parametersProducer;
    private final AttributeModifier             iconClassModifier;
    private final List<AttributeModifier>       linkLabelAttributeModifiers;

    /**
     * @param pLinkLabelModel -
     * @param pDestination -
     * @param pParametersProducer -
     * @param pIconClassModifier -
     * @param pLinkLabelAttributeModifiers -
     */
    public LinkColumn( //
            final IModel<String> pLinkLabelModel //
            , final Class<? extends Page> pDestination //
            , final IProducer2<E, PageParameters> pParametersProducer //
            , final AttributeModifier pIconClassModifier //
            , final AttributeModifier... pLinkLabelAttributeModifiers //
    ) {
        super(EMPTY_LABEL_MODEL);
        this.linkLabelModel = ArgUtil.checkNull(pLinkLabelModel, "pLinkLabelModel"); //$NON-NLS-1$
        this.destination = ArgUtil.checkNull(pDestination, "pDestination"); //$NON-NLS-1$
        this.parametersProducer = ArgUtil.checkNull(pParametersProducer, "pParametersProducer"); //$NON-NLS-1$
        this.iconClassModifier = pIconClassModifier;
        this.linkLabelAttributeModifiers = pLinkLabelAttributeModifiers == null //
        ? Collections.<AttributeModifier> emptyList() //
                : Arrays.asList(pLinkLabelAttributeModifiers);
    }

    /**
     * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item,
     *      java.lang.String, org.apache.wicket.model.IModel)
     */
    @Override
    public void populateItem(final Item<ICellPopulator<E>> pCellItem, final String pComponentId, final IModel<E> pRowModel) {
        final PageParameters params = this.parametersProducer.produce(pRowModel.getObject());
        final LinkPanel link = new LinkPanel(pComponentId, this.linkLabelModel, params, this.destination);

        link.getIcon().add(this.iconClassModifier);

        for (final AttributeModifier am : this.linkLabelAttributeModifiers) {
            link.getLink().add(am);
        }
        pCellItem.add(link);
    }
}