/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.city.tear.entity.EDataStore;
import jp.co.city.tear.entity.EDataStore_;
import jp.co.city.tear.service.IDataStore;

import org.apache.commons.io.IOUtils;

/**
 * {@link EDataStore}を通してLOBとしてデータを保存する{@link IDataStore}の実装.
 * 
 * @author jabaraster
 */
public class LobDataStore extends JpaDaoBase implements IDataStore {
    private static final long serialVersionUID = -2061878019988571964L;

    /**
     * @param pEntityManagerFactory
     */
    @Inject
    public LobDataStore(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#delete(long)
     */
    @Override
    public void delete(final long pDataId) {
        try {
            final EDataStore e = findByDataId(pDataId);
            getEntityManager().remove(e);
        } catch (final NotFound e) {
            // 処理なし
        }
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#getDataInputStream(long)
     */
    @Override
    public InputStream getDataInputStream(final long pDataId) throws NotFound {
        return new ByteArrayInputStream(findByDataId(pDataId).getData());
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#save(long, java.io.InputStream)
     */
    @Override
    public int save(final long pDataId, final InputStream pStream) throws EmptyData {
        if (pStream == null) {
            throw EmptyData.GLOBAL;
        }
        try {
            final byte[] d = IOUtils.toByteArray(pStream);
            final EDataStore e = new EDataStore();
            e.setData(d);
            e.setDataId(pDataId);
            getEntityManager().persist(e);
            return d.length;

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private EDataStore findByDataId(final long pDataId) throws NotFound {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EDataStore> query = builder.createQuery(EDataStore.class);
        final Root<EDataStore> root = query.from(EDataStore.class);
        query.where(builder.equal(root.get(EDataStore_.dataId), Long.valueOf(pDataId)));
        return getSingleResult(em.createQuery(query));
    }

}
