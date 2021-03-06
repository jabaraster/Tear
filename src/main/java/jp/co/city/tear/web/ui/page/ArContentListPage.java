/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.Empty;
import jabara.general.IProducer2;
import jabara.general.Sort;
import jabara.jpa.entity.EntityBase_;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.web.WebUtil;
import jp.co.city.tear.web.ui.AppSession;
import jp.co.city.tear.web.ui.component.AttributeColumn;
import jp.co.city.tear.web.ui.component.BodyCssHeaderItem;
import jp.co.city.tear.web.ui.component.DateTimeColumn;
import jp.co.city.tear.web.ui.component.DeleteLinkColumn;
import jp.co.city.tear.web.ui.component.EditLinkColumn;
import jp.co.city.tear.web.ui.component.MarkerImageResourceStream;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.image.resource.ThumbnailImageResource;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceStreamResource;

/**
 * @author jabaraster
 */
public class ArContentListPage extends RestrictedPageBase {
    private static final long                                serialVersionUID      = 5244239824791113862L;

    private static final int                                 DEFAULT_ROWS_PER_PAGE = 10;

    @Inject
    IArContentService                                        arContentService;

    private AjaxFallbackDefaultDataTable<EArContent, String> arContents;
    private Link<?>                                          adder;

    /**
     * 
     */
    public ArContentListPage() {
        this.add(getAdder());
        this.add(getArContents());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(BodyCssHeaderItem.get());
        pResponse.render(ComponentCssHeaderItem.forType(ArContentListPage.class));
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ARコンテンツ一覧"); //$NON-NLS-1$
    }

    private Link<?> getAdder() {
        if (this.adder == null) {
            this.adder = new BookmarkablePageLink<>("adder", ArContentInsertPage.class); //$NON-NLS-1$
        }
        return this.adder;
    }

    @SuppressWarnings({ "nls", "serial" })
    private AjaxFallbackDefaultDataTable<EArContent, String> getArContents() {
        if (this.arContents == null) {
            final List<IColumn<EArContent, String>> columns = new ArrayList<>();
            columns.add(new AttributeColumn<EArContent>(EArContent.getMeta(), EntityBase_.id));
            columns.add(new AttributeColumn<EArContent>(EArContent.getMeta(), EArContent_.title));
            columns.add(new OwnerColumn());
            columns.add(new MarkerImageColumn(Models.readOnly("マーカ画像")));

            columns.add(new ContentColumn("コンテンツ"));
            columns.add(new AttributeColumn<EArContent>(EArContent.getMeta(), EArContent_.similarityThreshold));
            columns.add(new DateTimeColumn<EArContent>(EArContent.getMeta(), EntityBase_.created));

            final String newestUpdatedColumnName = "newestUpdated";
            columns.add(new PropertyColumn<EArContent, String>( //
                    Models.readOnly(EArContent.getMeta().get(newestUpdatedColumnName).getLocalizedName()) //
                    , newestUpdatedColumnName //
                    , newestUpdatedColumnName) {
                @Override
                public IModel<Object> getDataModel(final IModel<EArContent> pRowModel) {
                    final Date d = pRowModel.getObject().getNewestUpdated();
                    if (d == null) {
                        return Models.<Object> readOnly(Empty.STRING);
                    }
                    return Models.<Object> readOnly(new SimpleDateFormat(DateTimeColumn.FORMAT).format(d));
                }
            });

            final IProducer2<EArContent, PageParameters> p = new IProducer2<EArContent, PageParameters>() {
                @Override
                public final PageParameters produce(final EArContent pArgument) {
                    return ArContentEditPage.createParameters(pArgument);
                }
            };
            columns.add(new EditLinkColumn<>(Models.readOnly("編集"), ArContentUpdatePage.class, p)); //$NON-NLS-1$
            columns.add(new DeleteLinkColumn<>(Models.readOnly("削除"), ArContentDeletePage.class, p)); //$NON-NLS-1$

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

    private static class ContentColumn extends AbstractColumn<EArContent, String> {
        private static final long serialVersionUID = 4151926507136485310L;

        ContentColumn(final String pDisplayLabel) {
            super(Models.readOnly(pDisplayLabel));
        }

        @Override
        public void populateItem(final Item<ICellPopulator<EArContent>> pCellItem, final String pComponentId, final IModel<EArContent> pRowModel) {
            pCellItem.add(new ContentPanel(pComponentId, pRowModel.getObject()));
        }
    }

    private static class ContentPanel extends Panel {
        private static final long  serialVersionUID = -215758806035680909L;

        private final EArContent   content;

        private Label              label;
        private WebMarkupContainer downloadLink;
        private Label              description;

        ContentPanel(final String pId, final EArContent pContent) {
            super(pId);
            this.content = pContent;
            this.add(getLabel());
            this.add(getDownloadLink());
            this.add(getDescription());
        }

        private Label getDescription() {
            if (this.description == null) {
                this.description = new Label("description", this.content.getContentDescription()); //$NON-NLS-1$
            }
            return this.description;
        }

        private WebMarkupContainer getDownloadLink() {
            if (this.downloadLink == null) {
                this.downloadLink = new WebMarkupContainer("downloadLink") { //$NON-NLS-1$
                    private static final long serialVersionUID = 3476418772502673965L;

                    @SuppressWarnings("synthetic-access")
                    @Override
                    public boolean isVisible() {
                        return ContentPanel.this.content.getContent().hasData();
                    }
                };
                this.downloadLink.add(AttributeModifier.append("href", WebUtil.buildContentAbsoluteUrl(this.content.getId().longValue()))); //$NON-NLS-1$
            }
            return this.downloadLink;
        }

        private Label getLabel() {
            if (this.label == null) {
                final boolean hasData = this.content.getContent().hasData();
                final String s = hasData ? "登録あり" : "登録なし"; //$NON-NLS-1$//$NON-NLS-2$
                this.label = new Label("label", s); //$NON-NLS-1$
                this.label.add(AttributeModifier.append("class", hasData ? "label label-success" : "label label-default")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            }
            return this.label;
        }
    }

    private class ImagePanel extends Panel {
        private static final long serialVersionUID = -4684544684054772480L;

        ImagePanel(final String pId, final IModel<EArContent> pModel) {
            super(pId);
            final EArContent ar = pModel.getObject();
            @SuppressWarnings("resource")
            final MarkerImageResourceStream srs = new MarkerImageResourceStream(ArContentListPage.this.arContentService, ar);
            final ThumbnailImageResource r = new ThumbnailImageResource(new ResourceStreamResource(srs), 128);
            final Image markerImage = new Image("image", r); //$NON-NLS-1$
            this.add(markerImage);
        }
    }

    private class MarkerImageColumn extends AbstractColumn<EArContent, String> {
        private static final long serialVersionUID = -8894278574933262462L;

        MarkerImageColumn(final IModel<String> pDisplayModel) {
            super(pDisplayModel);
        }

        @Override
        public void populateItem(final Item<ICellPopulator<EArContent>> pCellItem, final String pComponentId, final IModel<EArContent> pRowModel) {
            if (pRowModel.getObject().getMarker().hasData()) {
                pCellItem.add(new ImagePanel(pComponentId, pRowModel));
            } else {
                final Label l = new Label(pComponentId, "登録なし"); //$NON-NLS-1$
                l.add(AttributeModifier.append("class", "label label-default")); //$NON-NLS-1$//$NON-NLS-2$
                pCellItem.add(l);
            }
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
}
