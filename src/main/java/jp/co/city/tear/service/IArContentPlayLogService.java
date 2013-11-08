/**
 * 
 */
package jp.co.city.tear.service;

import jabara.general.Sort;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.service.impl.ArContentPlayLogServiceImpl;
import jp.co.city.tear.web.ui.component.PagingCondition;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(ArContentPlayLogServiceImpl.class)
public interface IArContentPlayLogService {

    /**
     * @param pCondition -
     * @return 件数.
     */
    long countAll(FindCondition pCondition);

    /**
     * @return -
     */
    String createDescriptor();

    /**
     * @param pCondition -
     * @param pPagingCondition -
     * @param pSort -
     * @return -
     */
    List<EArContentPlayLog> find(FindCondition pCondition, PagingCondition pPagingCondition, Sort pSort);

    /**
     * @param pFirst -
     * @param pCount -
     * @return -
     */
    List<EArContentPlayLog> get(int pFirst, int pCount);

    /**
     * @param pLog -
     */
    void insert(ArContentPlayLog pLog);

    /**
     * @param pCondition -
     * @return -
     */
    InputStream makeCsv(FindCondition pCondition);

    /**
     * @author jabaraster
     */
    public static class FindCondition implements Serializable {
        private static final long serialVersionUID = -2488766912134421647L;

        private Date              from             = null;
        private Date              to               = null;

        /**
         * @param pFrom -
         * @param pTo -
         */
        public FindCondition(final Date pFrom, final Date pTo) {
            this.from = pFrom == null ? null : new Date(pFrom.getTime());
            this.to = pTo == null ? null : new Date(pTo.getTime());
        }

        /**
         * @return fromを返す.
         */
        public Date getFrom() {
            return this.from == null ? null : new Date(this.from.getTime());
        }

        /**
         * @return toを返す.
         */
        public Date getTo() {
            return this.to == null ? null : new Date(this.to.getTime());
        }
    }
}
