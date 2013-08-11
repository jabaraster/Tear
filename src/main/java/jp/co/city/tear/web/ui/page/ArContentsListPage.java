/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.jpa.entity.EntityBase_;
import jabara.wicket.Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContents;
import jp.co.city.tear.service.IArContentsService;
import jp.co.city.tear.web.ui.AppSession;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author jabaraster
 */
public class ArContentsListPage extends RestrictedPageBase {
    private static final long                                 serialVersionUID      = 5244239824791113862L;

    private static final int                                  DEFAULT_ROWS_PER_PAGE = 20;

    @Inject
    IArContentsService                                        arContentsService;

    private AjaxFallbackDefaultDataTable<EArContents, String> arContents;

    /**
     * 
     */
    public ArContentsListPage() {
        this.add(getArContents());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ARコンテンツ一覧"); //$NON-NLS-1$
    }

    private AjaxFallbackDefaultDataTable<EArContents, String> getArContents() {
        if (this.arContents == null) {
            final List<IColumn<EArContents, String>> columns = new ArrayList<>();

            // TODO

            this.arContents = new AjaxFallbackDefaultDataTable<>( //
                    "arContents" // //$NON-NLS-1$
                    , columns //
                    , new ArContentsProvider() //
                    , DEFAULT_ROWS_PER_PAGE);

        }
        return this.arContents;
    }

    private class ArContentsProvider extends SortableDataProvider<EArContents, String> {
        private static final long serialVersionUID = -423941597951110011L;

        ArContentsProvider() {
            setSort(EntityBase_.id.getName(), SortOrder.ASCENDING);
        }

        @Override
        public Iterator<? extends EArContents> iterator(final long pFirst, final long pCount) {
            return ArContentsListPage.this.arContentsService.find(AppSession.get().getLoginUser(), pFirst, pCount).iterator();
        }

        @SuppressWarnings("serial")
        @Override
        public IModel<EArContents> model(final EArContents pObject) {
            return new LoadableDetachableModel<EArContents>() {
                @Override
                protected EArContents load() {
                    return pObject;
                }
            };
        }

        @Override
        public long size() {
            return ArContentsListPage.this.arContentsService.count(AppSession.get().getLoginUser());
        }

    }
}
