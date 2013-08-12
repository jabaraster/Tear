/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.IProducer2;
import jabara.general.Sort;
import jabara.jpa.entity.EntityBase_;
import jabara.wicket.CssUtil;
import jabara.wicket.Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.service.IUserService;
import jp.co.city.tear.web.ui.component.AttributeColumn;
import jp.co.city.tear.web.ui.component.LinkColumn;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
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

    @SuppressWarnings("serial")
    private AjaxFallbackDefaultDataTable<EUser, String> getUsers() {
        if (this.users == null) {
            final List<IColumn<EUser, String>> columns = new ArrayList<>();
            columns.add(new AttributeColumn<EUser>(EUser.getMeta(), EntityBase_.id));
            columns.add(new AttributeColumn<EUser>(EUser.getMeta(), EUser_.userId));
            columns.add(new AttributeColumn<EUser>(EUser.getMeta(), EUser_.administrator));
            columns.add(new AttributeColumn<EUser>(EUser.getMeta(), EntityBase_.created));
            columns.add(new AttributeColumn<EUser>(EUser.getMeta(), EntityBase_.updated));

            final IProducer2<EUser, PageParameters> p = new IProducer2<EUser, PageParameters>() {
                @Override
                public PageParameters produce(final EUser pArgument) {
                    return UserEditPage.createParameters(pArgument);
                }
            };
            columns.add(new LinkColumn<>(Models.readOnly("編集"), UserUpdatePage.class, p)); //$NON-NLS-1$
            columns.add(new LinkColumn<>(Models.readOnly("削除"), UserDeletePage.class, p)); //$NON-NLS-1$

            this.users = new AjaxFallbackDefaultDataTable<>( //
                    "users" // //$NON-NLS-1$
                    , columns //
                    , new UserDataProvider(this.userService) //
                    , DEFAULT_ROW_PER_PAGE //
            );
        }
        return this.users;
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
