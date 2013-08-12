/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.IProducer2;
import jabara.general.Sort;
import jabara.jpa.entity.EntityBase_;
import jabara.wicket.CssUtil;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.component.AttributeColumn;
import jp.co.city.tear.web.ui.component.LinkColumn;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class ArContentListPage extends RestrictedPageBase {
    private static final long                                serialVersionUID      = 5244239824791113862L;

    private static final int                                 DEFAULT_ROWS_PER_PAGE = 20;

    @Inject
    IArContentService                                        arContentService;

    private AjaxFallbackDefaultDataTable<EArContent, String> arContents;

    /**
     * 
     */
    public ArContentListPage() {
        this.add(getArContents());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, ArContentListPage.class);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ARコンテンツ一覧"); //$NON-NLS-1$
    }

    private AjaxFallbackDefaultDataTable<EArContent, String> getArContents() {
        if (this.arContents == null) {
            final List<IColumn<EArContent, String>> columns = new ArrayList<>();
            columns.add(new AttributeColumn<EArContent>(EArContent.getMeta(), EntityBase_.id));
            columns.add(new AttributeColumn<EArContent>(EArContent.getMeta(), EArContent_.title));
            columns.add(new OwnerColumn());
            columns.add(new AttributeColumn<EArContent>(EArContent.getMeta(), EntityBase_.created));
            columns.add(new AttributeColumn<EArContent>(EArContent.getMeta(), EntityBase_.updated));

            final ParametersProducer p = new ParametersProducer();
            columns.add(new LinkColumn<EArContent>(Models.readOnly("編集"), ArContentUpdatePage.class, p)); //$NON-NLS-1$
            columns.add(new LinkColumn<EArContent>(Models.readOnly("削除"), ArContentDeletePage.class, p)); //$NON-NLS-1$

            this.arContents = new AjaxFallbackDefaultDataTable<>("arContents", columns, new ArContentsProvider(), DEFAULT_ROWS_PER_PAGE); //$NON-NLS-1$
        }
        return this.arContents;
    }

    private class ArContentsProvider extends SortableDataProvider<EArContent, String> {
        private static final long serialVersionUID = -423941597951110011L;

        ArContentsProvider() {
            setSort(EntityBase_.id.getName(), SortOrder.ASCENDING);
        }

        @Override
        public Iterator<? extends EArContent> iterator(final long pFirst, final long pCount) {
            final SortParam<String> sort = getSort();
            final Sort s = sort.isAscending() ? Sort.asc(sort.getProperty()) : Sort.desc(sort.getProperty());
            return ArContentListPage.this.arContentService.find(AppSession.get().getLoginUser(), pFirst, pCount, s).iterator();
        }

        @SuppressWarnings("serial")
        @Override
        public IModel<EArContent> model(final EArContent pObject) {
            return new LoadableDetachableModel<EArContent>() {
                @Override
                protected EArContent load() {
                    return pObject;
                }
            };
        }

        @Override
        public long size() {
            return ArContentListPage.this.arContentService.count(AppSession.get().getLoginUser());
        }

    }

    private static class OwnerColumn extends AbstractColumn<EArContent, String> {
        private static final long serialVersionUID = 3891966725239620408L;

        public OwnerColumn() {
            super( //
                    Models.readOnly(EArContent.getMeta().get(EArContent_.owner.getName()).getLocalizedName()) //
                    , EArContent_.owner.getName() + "." + EUser_.userId.getName() // //$NON-NLS-1$
            );
        }

        @Override
        public void populateItem(final Item<ICellPopulator<EArContent>> pCellItem, final String pComponentId, final IModel<EArContent> pRowModel) {
            pCellItem.add(new Label(pComponentId, pRowModel.getObject().getOwner().getUserId()));
        }
    }

    private static class ParametersProducer implements IProducer2<EArContent, PageParameters>, Serializable {
        private static final long serialVersionUID = 7424283512982530434L;

        @Override
        public PageParameters produce(final EArContent pArgument) {
            return ArContentEditPage.createParameters(pArgument);
        }
    }
}
