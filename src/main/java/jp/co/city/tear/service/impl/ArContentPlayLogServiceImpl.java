/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.bean.BeanProperties;
import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;
import jabara.jpa.entity.EntityBase_;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.entity.EArContentPlayLog_;
import jp.co.city.tear.entity.EPlayLogTrackingDescriptor;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.service.IArContentPlayLogService;
import jp.co.city.tear.service.PagingCondition;

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
     * @see jp.co.city.tear.service.IArContentPlayLogService#countAll(jp.co.city.tear.service.IArContentPlayLogService.FindCondition)
     */
    @Override
    public long countAll(final FindCondition pCondition) {
        ArgUtil.checkNull(pCondition, "pCondition"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<EArContentPlayLog> root = query.from(EArContentPlayLog.class);

        query.select(builder.count(root));
        query.where(buildFindWhere(pCondition, builder, root));

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
     * @see jp.co.city.tear.service.IArContentPlayLogService#find(jp.co.city.tear.service.IArContentPlayLogService.FindCondition,
     *      jp.co.city.tear.service.PagingCondition, jabara.general.Sort)
     */
    @Override
    public List<EArContentPlayLog> find(final FindCondition pCondition, final PagingCondition pPagingCondition, final Sort pSort) {
        ArgUtil.checkNull(pCondition, "pCondition"); //$NON-NLS-1$
        ArgUtil.checkNull(pPagingCondition, "pPagingCondition"); //$NON-NLS-1$
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EArContentPlayLog> query = builder.createQuery(EArContentPlayLog.class);
        final Root<EArContentPlayLog> root = query.from(EArContentPlayLog.class);

        query.where(buildFindWhere(pCondition, builder, root));
        if (pSort != null) {
            query.orderBy(convertOrder(pSort, builder, root));
        }

        return em.createQuery(query) //
                .setFirstResult(pPagingCondition.getFirst()) //
                .setMaxResults(pPagingCondition.getCount()) //
                .getResultList();
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

    /**
     * @see jp.co.city.tear.service.IArContentPlayLogService#makeCsv(jp.co.city.tear.service.IArContentPlayLogService.FindCondition)
     */
    @SuppressWarnings("nls")
    @Override
    public InputStream makeCsv(final FindCondition pCondition) {
        ArgUtil.checkNull(pCondition, "pCondition"); //$NON-NLS-1$

        File temp = null;
        try {
            temp = File.createTempFile(ArContentPlayLogServiceImpl.class.getName(), ".csv"); //$NON-NLS-1$
            try (OutputStream out = new FileOutputStream(temp); //
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8"))) // //$NON-NLS-1$
            ) {
                final BeanProperties meta = EArContentPlayLog.getMeta();
                writer.append(meta.get(EArContentPlayLog_.playDatetime.getName()).getLocalizedName());

                writer.append(",");
                writer.append(meta.get(EArContentPlayLog_.trackingDescriptor.getName()).getLocalizedName());

                writer.append(",");
                writer.append(meta.get(EArContentPlayLog_.arContentId.getName()).getLocalizedName());

                writer.append(",");
                writer.append(meta.get(EArContentPlayLog_.latitude.getName()).getLocalizedName());

                writer.append(",");
                writer.append(meta.get(EArContentPlayLog_.longitude.getName()).getLocalizedName());

                writer.newLine();

                final DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                for (final EArContentPlayLog log : find(pCondition)) {
                    writer.append(fmt.format(log.getPlayDatetime()));

                    writer.append(",");
                    writer.append(String.valueOf(log.getTrackingDescriptor()));

                    writer.append(",");
                    writer.append(String.valueOf(log.getArContentId()));

                    writer.append(",");
                    writer.append(String.valueOf(log.getLatitude()));

                    writer.append(",");
                    writer.append(String.valueOf(log.getLongitude()));

                    writer.newLine();
                }
            }

            return new AutoDeleteFileInputStream(temp);

        } catch (final IOException e) {
            if (temp != null) {
                temp.delete();
            }
            throw ExceptionUtil.rethrow(e);
        }
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

    private List<EArContentPlayLog> find(final FindCondition pCondition) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EArContentPlayLog> query = builder.createQuery(EArContentPlayLog.class);
        final Root<EArContentPlayLog> root = query.from(EArContentPlayLog.class);

        query.where(buildFindWhere(pCondition, builder, root));
        query.orderBy(builder.desc(root.get(EArContentPlayLog_.playDatetime)));

        // TODO 最大件数の扱いをどうするか・・・
        return em.createQuery(query).setMaxResults(10000).getResultList();
    }

    private static Date addDay(final Date pDate, final int pValue) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        cal.add(Calendar.DAY_OF_MONTH, pValue);
        return cal.getTime();
    }

    private static Predicate[] buildFindWhere(final FindCondition pCondition, final CriteriaBuilder builder, final Root<EArContentPlayLog> root) {
        final List<Predicate> where = new ArrayList<>();
        if (pCondition.getFrom() != null) {
            where.add(builder.greaterThanOrEqualTo(root.get(EArContentPlayLog_.playDatetime), omitSecond(pCondition.getFrom())));
        }
        if (pCondition.getTo() != null) {
            where.add(builder.lessThan(root.get(EArContentPlayLog_.playDatetime), omitSecond(addDay(pCondition.getTo(), 1))));
        }
        return where.toArray(EMPTY_PREDICATE);
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

    private static class AutoDeleteFileInputStream extends FileInputStream {

        private final File file;

        public AutoDeleteFileInputStream(final File pFile) throws FileNotFoundException {
            super(pFile);
            ArgUtil.checkNull(pFile, "pFile"); //$NON-NLS-1$
            this.file = pFile;
        }

        @Override
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                this.file.delete();
            }
        }
    }
}
