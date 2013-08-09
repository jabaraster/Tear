/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.Sort;
import jabara.jpa.entity.EntityBase_;
import jabara.wicket.CssUtil;
import jabara.wicket.Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.metamodel.SingularAttribute;

import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.service.IUserService;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author jabaraster
 */
public class UserListPage extends AdministrationPageBase {
    private static final long                           serialVersionUID     = 1125709413157102080L;

    private static final int                            DEFAULT_ROW_PER_PAGE = 20;

    @Inject
    IUserService                                        userService;

    private AjaxFallbackDefaultDataTable<EUser, String> users;

    /**
     * 
     */
    public UserListPage() {
        this.add(getUsers());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, UserListPage.class);
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.of("ユーザ一覧"); //$NON-NLS-1$
    }

    private AjaxFallbackDefaultDataTable<EUser, String> getUsers() {
        if (this.users == null) {
            final List<IColumn<EUser, String>> columns = new ArrayList<IColumn<EUser, String>>();
            columns.add(new Column(EntityBase_.id));
            columns.add(new Column(EUser_.userId));
            columns.add(new Column(EUser_.administrator));
            columns.add(new Column(EntityBase_.created));
            columns.add(new Column(EntityBase_.updated));
            columns.add(new LinkColumn(Models.of("編集"), UserUpdatePage.class)); //$NON-NLS-1$
            columns.add(new LinkColumn(Models.of("削除"), UserDeletePage.class)); //$NON-NLS-1$

            this.users = new AjaxFallbackDefaultDataTable<EUser, String>( //
                    "users" // //$NON-NLS-1$
                    , columns //
                    , new UserDataProvider(this.userService) //
                    , DEFAULT_ROW_PER_PAGE //
            );
        }
        return this.users;
    }

    /**
     * @param pUser -
     * @return -
     */
    public static PageParameters createParameterForUserId(final EUser pUser) {
        final PageParameters ret = new PageParameters();
        ret.set(0, pUser.getId());
        return ret;
    }

    private static class Column extends PropertyColumn<EUser, String> {
        private static final long serialVersionUID = 1290184092925657774L;

        Column(final SingularAttribute<?, ? extends Comparable<?>> pAttribute) {
            super(Models.of(EUser.getMeta().get(pAttribute.getName()).getLocalizedName()), pAttribute.getName(), pAttribute.getName());
        }
    }

    private static class LinkColumn extends AbstractColumn<EUser, String> {
        private static final long           serialVersionUID  = 7430577515667494582L;

        private static final IModel<String> EMPTY_LABEL_MODEL = Models.readOnly("　"); //$NON-NLS-1$

        private final IModel<String>        linkLabelModel;
        private final Class<? extends Page> destination;

        /**
         * @param pLinkLabelModel -
         * @param pDestination -
         */
        public LinkColumn( //
                final IModel<String> pLinkLabelModel //
                , final Class<? extends Page> pDestination //
        ) {
            super(EMPTY_LABEL_MODEL);
            this.linkLabelModel = pLinkLabelModel;
            this.destination = pDestination;
        }

        @Override
        public void populateItem(final Item<ICellPopulator<EUser>> pCellItem, final String pComponentId, final IModel<EUser> pRowModel) {
            final PageParameters params = createParameterForUserId(pRowModel.getObject());
            pCellItem.add(new LinkPanel(pComponentId, this.linkLabelModel, params, this.destination));
        }
    }

    private static class LinkPanel extends Panel {
        private static final long           serialVersionUID = -870163225127196393L;

        private final IModel<String>        linkLabelModel;
        private final PageParameters        destinationParameter;
        private final Class<? extends Page> destination;

        private Link<?>                     link;
        private Label                       linkLabel;

        /**
         * @param pId -
         * @param pLinkLabelModel -
         * @param pDestinationParameter -
         * @param pDestination -
         */
        public LinkPanel( //
                final String pId //
                , final IModel<String> pLinkLabelModel //
                , final PageParameters pDestinationParameter //
                , final Class<? extends Page> pDestination //
        ) {
            super(pId);
            this.destinationParameter = pDestinationParameter;
            this.destination = pDestination;
            this.linkLabelModel = pLinkLabelModel;
            this.add(getLink());
        }

        private Link<?> getLink() {
            if (this.link == null) {
                this.link = new BookmarkablePageLink<>("go", this.destination, this.destinationParameter); //$NON-NLS-1$
                this.link.add(getLinkLabel());
            }
            return this.link;
        }

        private Label getLinkLabel() {
            if (this.linkLabel == null) {
                this.linkLabel = new Label("linkLabel", this.linkLabelModel); //$NON-NLS-1$
            }
            return this.linkLabel;
        }
    }

    private static class UserDataProvider extends SortableDataProvider<EUser, String> {
        private static final long  serialVersionUID = 4686111168995047510L;

        private final IUserService userService;

        UserDataProvider(final IUserService pUserService) {
            this.userService = pUserService;
            this.setSort(EntityBase_.id.getName(), SortOrder.ASCENDING);
        }

        @Override
        public Iterator<? extends EUser> iterator(final long pFirst, final long pCount) {
            final SortParam<String> sort = getSort();
            final Sort s = sort.isAscending() ? Sort.asc(sort.getProperty()) : Sort.desc(sort.getProperty());
            return this.userService.get(pFirst, pCount, s).iterator();
        }

        @SuppressWarnings("serial")
        @Override
        public IModel<EUser> model(final EUser pObject) {
            return new LoadableDetachableModel<EUser>() {
                @Override
                protected EUser load() {
                    return pObject;
                }
            };
        }

        @Override
        public long size() {
            return this.userService.countAll();
        }

    }
}
