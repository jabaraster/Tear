/**
 * 
 */
package jp.co.city.tear.service;

import jabara.general.Sort;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.entity.EArContentPlayLog_;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.service.impl.ArContentPlayLogServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(ArContentPlayLogServiceImpl.class)
public interface IArContentPlayLogService {

    /**
     * @return 全件数.
     */
    long countAll();

    /**
     * @return -
     */
    String createDescriptor();

    /**
     * @param pCondition -
     * @return -
     */
    List<EArContentPlayLog> find(FindCondition pCondition);

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
     * @author jabaraster
     */
    public static class FindCondition implements Serializable {
        private static final long serialVersionUID = -2488766912134421647L;
        /**
         * 
         */
        public static final int   DEFAULT_FIRST    = 0;
        /**
         * 
         */
        public static final int   DEFAULT_COUNT    = 50;

        private Sort              sort             = Sort.desc(EArContentPlayLog_.playDatetime.getName());
        private int               first            = DEFAULT_FIRST;
        private int               count            = DEFAULT_COUNT;
        private Date              from             = null;
        private Date              to               = null;

        /**
         * @return countを返す.
         */
        public int getCount() {
            return this.count;
        }

        /**
         * @return firstを返す.
         */
        public int getFirst() {
            return this.first;
        }

        /**
         * @return fromを返す.
         */
        public Date getFrom() {
            return this.from == null ? null : new Date(this.from.getTime());
        }

        /**
         * @return sortを返す.
         */
        public Sort getSort() {
            return this.sort;
        }

        /**
         * @return toを返す.
         */
        public Date getTo() {
            return this.to == null ? null : new Date(this.to.getTime());
        }

        /**
         * @param pCount countを設定.
         */
        public void setCount(final int pCount) {
            this.count = pCount;
        }

        /**
         * @param pFirst firstを設定.
         */
        public void setFirst(final int pFirst) {
            this.first = pFirst;
        }

        /**
         * @param pFrom fromを設定.
         */
        public void setFrom(final Date pFrom) {
            this.from = pFrom;
        }

        /**
         * @param pSort sortを設定.
         */
        public void setSort(final Sort pSort) {
            this.sort = pSort;
        }

        /**
         * @param pTo toを設定.
         */
        public void setTo(final Date pTo) {
            this.to = pTo;
        }
    }
}
