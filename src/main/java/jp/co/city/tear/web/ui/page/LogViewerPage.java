/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.general.Sort;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.entity.EArContentPlayLog_;
import jp.co.city.tear.service.IArContentPlayLogService;
import jp.co.city.tear.service.IArContentPlayLogService.FindCondition;
import jp.co.city.tear.web.ui.component.AttributeColumn;
import jp.co.city.tear.web.ui.component.BodyCssHeaderItem;
import jp.co.city.tear.web.ui.component.DateField;
import jp.co.city.tear.web.ui.component.DateTimeColumn;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

/**
 * @author jabaraster
 */
@SuppressWarnings({ "serial", "synthetic-access" })
public class LogViewerPage extends AdministrationPageBase {
    private static final long                                       serialVersionUID     = 3338940907109172606L;

    /**
     * 
     */
    public static final int                                         DEFAULT_ROW_PER_PAGE = 200;

    private final Handler                                           handler              = new Handler();

    @Inject
    IArContentPlayLogService                                        arContentPlayLogService;

    private int                                                     rowPerPage           = DEFAULT_ROW_PER_PAGE;

    private FeedbackPanel                                           feedback;
    private Form<?>                                                 form;
    private DateField                                               from;
    private DateField                                               to;
    private AjaxButton                                              searcher;
    private AjaxFallbackDefaultDataTable<EArContentPlayLog, String> logs;

    /**
     * 
     */
    public LogViewerPage() {
        this.add(getFeedback());
        this.add(getForm());
        this.add(getLogs());
    }

    /**
     * @return rowPerPageを返す.
     */
    public int getRowPerPage() {
        return this.rowPerPage;
    }

    /**
     * @see jp.co.city.tear.web.ui.page.RestrictedPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(BodyCssHeaderItem.get());
        pResponse.render(ComponentCssHeaderItem.forType(LogViewerPage.class));
    }

    /**
     * @param pRowPerPage rowPerPageを設定.
     */
    public void setRowPerPage(final int pRowPerPage) {
        this.rowPerPage = pRowPerPage;
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ログを見る"); //$NON-NLS-1$
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new FeedbackPanel("feedback"); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getFrom());
            this.form.add(getTo());
            this.form.add(getSearcher());
        }
        return this.form;
    }

    private DateField getFrom() {
        if (this.from == null) {
            this.from = new DateField("from", Models.of((Date) null)); //$NON-NLS-1$
        }
        return this.from;
    }

    private AjaxFallbackDefaultDataTable<EArContentPlayLog, String> getLogs() {
        if (this.logs == null) {
            final List<IColumn<EArContentPlayLog, String>> columns = new ArrayList<>();
            columns.add(new DateTimeColumn<EArContentPlayLog>(EArContentPlayLog.getMeta(), EArContentPlayLog_.playDatetime));
            columns.add(AttributeColumn.<EArContentPlayLog> sortable(EArContentPlayLog.getMeta(), EArContentPlayLog_.arContentId));
            columns.add(AttributeColumn.<EArContentPlayLog> sortable(EArContentPlayLog.getMeta(), EArContentPlayLog_.trackingDescriptor));
            columns.add(AttributeColumn.<EArContentPlayLog> unsortable(EArContentPlayLog.getMeta(), EArContentPlayLog_.latitude));
            columns.add(AttributeColumn.<EArContentPlayLog> unsortable(EArContentPlayLog.getMeta(), EArContentPlayLog_.longitude));
            this.logs = new AjaxFallbackDefaultDataTable<>("logs", columns, new Provider(), this.rowPerPage); //$NON-NLS-1$
        }
        return this.logs;
    }

    private AjaxButton getSearcher() {
        if (this.searcher == null) {
            this.searcher = new IndicatingAjaxButton("searcher") { //$NON-NLS-1$
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    LogViewerPage.this.handler.onError(pTarget);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    LogViewerPage.this.handler.onSubmit(pTarget);
                }
            };
        }
        return this.searcher;
    }

    private DateField getTo() {
        if (this.to == null) {
            this.to = new DateField("to", Models.of((Date) null)); //$NON-NLS-1$
        }
        return this.to;
    }

    private class Handler implements Serializable {

        void onError(final AjaxRequestTarget pTarget) {
            pTarget.add(getFeedback());
        }

        void onSubmit(final AjaxRequestTarget pTarget) {
            pTarget.add(getLogs());
            pTarget.add(getFeedback());
        }
    }

    private class Provider extends SortableDataProvider<EArContentPlayLog, String> {

        Provider() {
            this.setSort(EArContentPlayLog_.playDatetime.getName(), SortOrder.DESCENDING);
        }

        @SuppressWarnings("boxing")
        @Override
        public Iterator<? extends EArContentPlayLog> iterator(final long pFirst, final long pCount) {
            Args.withinRange(Long.valueOf(Integer.MIN_VALUE), Long.valueOf(Integer.MAX_VALUE), pFirst, "pFirst"); //$NON-NLS-1$

            final SortParam<String> sort = getSort();

            final FindCondition condition = new FindCondition();
            condition.setCount((int) pCount);
            condition.setFirst((int) pFirst);
            condition.setFrom(getFrom().getModelObject());
            condition.setSort(sort.isAscending() ? Sort.asc(sort.getProperty()) : Sort.desc(sort.getProperty()));
            condition.setTo(getTo().getModelObject());
            return LogViewerPage.this.arContentPlayLogService.find(condition).iterator();
        }

        @Override
        public IModel<EArContentPlayLog> model(final EArContentPlayLog pObject) {
            return Models.of(pObject);
        }

        @Override
        public long size() {
            return LogViewerPage.this.arContentPlayLogService.countAll();
        }
    }

}
