/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.general.SortRule;
import jabara.jpa.JpaDaoBase;
import jabara.jpa.entity.EntityBase_;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.entity.EArContentPlayLog_;
import jp.co.city.tear.entity.EPlayLogTrackingDescriptor;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.service.IArContentPlayLogService;

/**
 * @author jabaraster
 */
public class ArContentPlayLogServiceImpl extends JpaDaoBase implements IArContentPlayLogService {
    private static final long serialVersionUID = 8612970872846241243L;

    /**
     * @param pEntityManagerFactory -
     */
    @Inject
    public ArContentPlayLogServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see jp.co.city.tear.service.IArContentPlayLogService#countAll()
     */
    @Override
    public long countAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<EArContentPlayLog> root = query.from(EArContentPlayLog.class);

        query.select(builder.count(root));

        try {
            return getSingleResult(em.createQuery(query)).longValue();
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @see jp.co.city.tear.service.IArContentPlayLogService#createDescriptor()
     */
    @Override
    public String createDescriptor() {
        final int RETRY_COUNT = 3;
        for (int i = 0; i < RETRY_COUNT; i++) {
            try {
                return createDescriptorCore();
            } catch (final PersistenceException e) {
                // 再度識別子生成を試みる.
            }
        }
        throw new IllegalStateException("識別子が生成できませんでした."); //$NON-NLS-1$
    }

    /**
     * @see jp.co.city.tear.service.IArContentPlayLogService#find(jp.co.city.tear.service.IArContentPlayLogService.FindCondition)
     */
    @Override
    public List<EArContentPlayLog> find(final FindCondition pCondition) {
        ArgUtil.checkNull(pCondition, "pCondition"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EArContentPlayLog> query = builder.createQuery(EArContentPlayLog.class);
        final Root<EArContentPlayLog> root = query.from(EArContentPlayLog.class);

        final List<Predicate> where = new ArrayList<>();
        if (pCondition.getFrom() != null) {
            where.add(builder.greaterThanOrEqualTo(root.get(EArContentPlayLog_.playDatetime), omitSecond(pCondition.getFrom())));
        }
        if (pCondition.getTo() != null) {
            where.add(builder.lessThan(root.get(EArContentPlayLog_.playDatetime), omitSecond(addDay(pCondition.getTo(), 1))));
        }
        query.where(where.toArray(EMPTY_PREDICATE));

        final Sort sort = pCondition.getSort();
        if (sort != null) {
            final Order order = sort.getSortRule() == SortRule.ASC //
            ? builder.asc(root.get(sort.getColumnName())) //
                    : builder.desc(root.get(sort.getColumnName()));
            query.orderBy(order);
        }

        return em.createQuery(query).setFirstResult(pCondition.getFirst()).setMaxResults(pCondition.getCount()).getResultList();
    }

    /**
     * @see jp.co.city.tear.service.IArContentPlayLogService#get(int, int)
     */
    @Override
    public List<EArContentPlayLog> get(final int pFirst, final int pCount) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EArContentPlayLog> query = builder.createQuery(EArContentPlayLog.class);
        final Root<EArContentPlayLog> root = query.from(EArContentPlayLog.class);
        query.orderBy( //
                builder.desc(root.get(EArContentPlayLog_.playDatetime)) //
                , builder.desc(root.get(EntityBase_.created)) //
        );
        return em.createQuery(query).setFirstResult(pFirst).setMaxResults(pCount).getResultList();
    }

    /**
     * @see jp.co.city.tear.service.IArContentPlayLogService#insert(jp.co.city.tear.model.ArContentPlayLog)
     */
    @Override
    public void insert(final ArContentPlayLog pLog) {
        final EArContentPlayLog dbLog = new EArContentPlayLog();
        dbLog.setArContentId(Long.valueOf(pLog.getArContentId()));
        dbLog.setLatitude(pLog.getLatitude());
        dbLog.setLongitude(pLog.getLongitude());
        dbLog.setPlayDatetime(pLog.getPlayDatetime());
        dbLog.setTrackingDescriptor(pLog.getTrackingDescriptor());
        getEntityManager().persist(dbLog);
    }

    private String createDescriptorCore() {
        final String d = UUID.randomUUID().toString();
        final EPlayLogTrackingDescriptor descriptor = new EPlayLogTrackingDescriptor();
        descriptor.setDescriptor(d);

        final EntityManager em = getEntityManager();
        em.persist(descriptor);
        em.flush(); // DBの重複チェックを実行させる.

        return d;
    }

    private static Date addDay(final Date pDate, final int pValue) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        cal.add(Calendar.DAY_OF_MONTH, pValue);
        return cal.getTime();
    }

    /**
     * @param pDate
     * @return pDateの秒以下を0にした値.
     */
    private static Date omitSecond(final Date pDate) {
        final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
        try {
            return fmt.parse(fmt.format(pDate));
        } catch (final ParseException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }
}
