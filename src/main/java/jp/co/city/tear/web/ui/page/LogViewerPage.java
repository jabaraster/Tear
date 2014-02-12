/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.bean.BeanProperties;
import jabara.bean.annotation.Localized;
import jabara.general.Sort;
import jabara.jpa.entity.EntityBase_;
import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.Models;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.entity.EArContentPlayLog_;
import jp.co.city.tear.service.IArContentPlayLogService;
import jp.co.city.tear.service.IArContentPlayLogService.FindCondition;
import jp.co.city.tear.service.PagingCondition;
import jp.co.city.tear.web.ui.component.AttributeColumn;
import jp.co.city.tear.web.ui.component.BodyCssHeaderItem;
import jp.co.city.tear.web.ui.component.DateField;
import jp.co.city.tear.web.ui.component.DateTimeColumn;
import jp.co.city.tear.web.ui.component.RangeField;
import jp.co.city.tear.web.ui.component.StreamResourceStream;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.lang.Args;

/**
 * @author jabaraster
 */
@SuppressWarnings({ "serial", "synthetic-access" })
public class LogViewerPage extends AdministrationPageBase {
    private static final long                                             serialVersionUID     = 3338940907109172606L;

    /**
     * 
     */
    public static final int                                               DEFAULT_ROW_PER_PAGE = 50;

    private final Handler                                                 handler              = new Handler();

    @Inject
    IArContentPlayLogService                                              arContentPlayLogService;

    private FeedbackPanel                                                 feedback;
    private Form<?>                                                       form;
    private DateField                                                     from;
    private DateField                                                     to;
    private AjaxButton                                                    searcher;
    private RangeField<Byte>                                              validPlayLogPeriod;
    private Button                                                        csvDownloader;
    private AjaxFallbackDefaultDataTable<IndexedArContentPlayLog, String> logs;

    /**
     * 
     */
    public LogViewerPage() {
        this.add(getFeedback());
        this.add(getForm());
        this.add(getLogs());
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
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("ログを見る"); //$NON-NLS-1$
    }

    private Button getCsvDownloader() {
        if (this.csvDownloader == null) {
            this.csvDownloader = new Button("csvDownloader") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    LogViewerPage.this.handler.onCsvDownload();
                }
            };
        }
        return this.csvDownloader;
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
            this.form.add(getValidPlayLogPeriod());
            this.form.add(getCsvDownloader());
        }
        return this.form;
    }

    private DateField getFrom() {
        if (this.from == null) {
            this.from = new DateField("from", Models.of((Date) null)); //$NON-NLS-1$
        }
        return this.from;
    }

    private AjaxFallbackDefaultDataTable<IndexedArContentPlayLog, String> getLogs() {
        if (this.logs == null) {
            final List<IColumn<IndexedArContentPlayLog, String>> columns = new ArrayList<>();
            columns.add(new NoColumn());
            columns.add(AttributeColumn.<IndexedArContentPlayLog> unsortable(IndexedArContentPlayLog.getMeta(), EntityBase_.id));
            columns.add(new DateTimeColumn<IndexedArContentPlayLog>(IndexedArContentPlayLog.getMeta(), EArContentPlayLog_.playDatetime));
            columns.add(AttributeColumn.<IndexedArContentPlayLog> sortable(IndexedArContentPlayLog.getMeta(), EArContentPlayLog_.arContentId));
            columns.add(AttributeColumn.<IndexedArContentPlayLog> sortable(IndexedArContentPlayLog.getMeta(), EArContentPlayLog_.trackingDescriptor));
            columns.add(AttributeColumn.<IndexedArContentPlayLog> unsortable(IndexedArContentPlayLog.getMeta(), EArContentPlayLog_.latitude));
            columns.add(AttributeColumn.<IndexedArContentPlayLog> unsortable(IndexedArContentPlayLog.getMeta(), EArContentPlayLog_.longitude));
            this.logs = new AjaxFallbackDefaultDataTable<>("logs", columns, new Provider(), DEFAULT_ROW_PER_PAGE); //$NON-NLS-1$
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

    @SuppressWarnings("boxing")
    private RangeField<Byte> getValidPlayLogPeriod() {
        if (this.validPlayLogPeriod == null) {
            final IModel<Byte> valueModel = Models.of((byte) 1);
            final IModel<Byte> minModel = Models.readOnly((byte) 1);
            final IModel<Byte> maxModel = Models.readOnly((byte) 120);
            final IModel<Byte> stepModel = Models.readOnly((byte) 1);
            this.validPlayLogPeriod = new RangeField<>("validPlayLogPeriod", Byte.class, valueModel, minModel, maxModel, stepModel); //$NON-NLS-1$
            this.validPlayLogPeriod.setRangeValidator();
        }
        return this.validPlayLogPeriod;
    }

    private class Handler implements Serializable {

        @SuppressWarnings("resource")
        void onCsvDownload() {
            final FindCondition condition = new FindCondition(getFrom().getModelObject(), getTo().getModelObject());
            final int validPlayLogPeriodSecond = getValidPlayLogPeriod().getModelObject().intValue();
            final InputStream in = LogViewerPage.this.arContentPlayLogService.makeCsv(validPlayLogPeriodSecond, condition);
            final String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".csv"; //$NON-NLS-1$ //$NON-NLS-2$
            @SuppressWarnings("hiding")
            final ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(new StreamResourceStream(in, "text/csv"), fileName); //$NON-NLS-1$
            getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
        }

        void onError(final AjaxRequestTarget pTarget) {
            pTarget.add(getFeedback());
        }

        void onSubmit(final AjaxRequestTarget pTarget) {
            pTarget.add(getLogs());
            pTarget.add(getFeedback());
        }
    }

    private static class IndexedArContentPlayLog implements Serializable {
        private static BeanProperties   _properties = BeanProperties.getInstance(IndexedArContentPlayLog.class);

        private final int               index;
        private final EArContentPlayLog log;

        IndexedArContentPlayLog(final int pIndex, final EArContentPlayLog pLog) {
            this.index = pIndex;
            this.log = pLog;
        }

        /**
         * @return -
         * @see jp.co.city.tear.entity.EArContentPlayLog#getArContentId()
         */
        @Localized("ARコンテンツID")
        public Long getArContentId() {
            return this.log.getArContentId();
        }

        /**
         * @return -
         * @see jabara.jpa.entity.EntityBase#getId()
         */
        @SuppressWarnings("unused")
        public Long getId() {
            return this.log.getId();
        }

        /**
         * @return the index
         */
        public int getIndex() {
            return this.index;
        }

        /**
         * @return -
         * @see jp.co.city.tear.entity.EArContentPlayLog#getLatitude()
         */
        @Localized("緯度")
        public Double getLatitude() {
            return this.log.getLatitude();
        }

        /**
         * @return -
         * @see jp.co.city.tear.entity.EArContentPlayLog#getLongitude()
         */
        @Localized("経度")
        public Double getLongitude() {
            return this.log.getLongitude();
        }

        /**
         * @return -
         * @see jp.co.city.tear.entity.EArContentPlayLog#getPlayDatetime()
         */
        @Localized("動作再生日時")
        public Date getPlayDatetime() {
            return this.log.getPlayDatetime();
        }

        /**
         * @return -
         * @see jp.co.city.tear.entity.EArContentPlayLog#getTrackingDescriptor()
         */
        @Localized("トラッキング用識別子")
        public String getTrackingDescriptor() {
            return this.log.getTrackingDescriptor();
        }

        /**
         * @return -
         */
        static BeanProperties getMeta() {
            return _properties;
        }
    }

    private static class NoColumn extends AbstractColumn<IndexedArContentPlayLog, String> {

        private static final IModel<String> _model = Models.readOnly("No"); //$NON-NLS-1$

        NoColumn() {
            super(_model);
        }

        @Override
        public void populateItem( //
                final Item<ICellPopulator<IndexedArContentPlayLog>> pCellItem //
                , final String pComponentId //
                , final IModel<IndexedArContentPlayLog> pRowModel) {
            pCellItem.add(new Label(pComponentId, String.valueOf(pRowModel.getObject().getIndex() + 1)));
        }

    }

    private class Provider extends SortableDataProvider<IndexedArContentPlayLog, String> {

        Provider() {
            this.setSort(EArContentPlayLog_.playDatetime.getName(), SortOrder.DESCENDING);
        }

        @SuppressWarnings("boxing")
        @Override
        public Iterator<? extends IndexedArContentPlayLog> iterator(final long pFirst, final long pCount) {
            Args.withinRange(Long.valueOf(Integer.MIN_VALUE), Long.valueOf(Integer.MAX_VALUE), pFirst, "pFirst"); //$NON-NLS-1$

            final PagingCondition paging = new PagingCondition((int) pFirst, (int) pCount);
            final FindCondition condition = new FindCondition(getFrom().getModelObject(), getTo().getModelObject());
            final SortParam<String> sort = getSort();
            return convert(LogViewerPage.this.arContentPlayLogService.find( //
                    condition //
                    , paging //
                    , sort.isAscending() ? Sort.asc(sort.getProperty()) : Sort.desc(sort.getProperty()) //
                    ).iterator());
        }

        @Override
        public IModel<IndexedArContentPlayLog> model(final IndexedArContentPlayLog pObject) {
            return Models.readOnly(pObject);
        }

        @Override
        public long size() {
            final FindCondition condition = new FindCondition(getFrom().getModelObject(), getTo().getModelObject());
            return LogViewerPage.this.arContentPlayLogService.countAll(condition);
        }

        private Iterator<? extends IndexedArContentPlayLog> convert(final Iterator<EArContentPlayLog> pIterator) {
            return new Iterator<IndexedArContentPlayLog>() {

                private int index = -1;

                @Override
                public boolean hasNext() {
                    return pIterator.hasNext();
                }

                @Override
                public IndexedArContentPlayLog next() {
                    final EArContentPlayLog original = pIterator.next();
                    this.index++;
                    return new IndexedArContentPlayLog(this.index, original);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

}
