/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.jpa.JpaDaoBase;
import jabara.jpa.entity.EntityBase_;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
}
