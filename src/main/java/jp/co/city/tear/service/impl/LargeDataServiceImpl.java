/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;
import jabara.general.NotFound;
import jabara.general.io.DataOperation;
import jabara.general.io.DataOperation.Operation;
import jabara.general.io.IReadableData;
import jabara.jpa.JpaDaoBase;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.IDataStore;
import jp.co.city.tear.service.IDataStore.EmptyData;
import jp.co.city.tear.service.ILargeDataService;

/**
 * @author jabaraster
 */
public class LargeDataServiceImpl extends JpaDaoBase implements ILargeDataService {
    private static final long serialVersionUID = -5035332969651964368L;

    private final IDataStore  dataStore;

    /**
     * @param pEntityManagerFactory -
     * @param pDataStore -
     */
    @Inject
    public LargeDataServiceImpl( //
            final EntityManagerFactory pEntityManagerFactory //
            , final IDataStore pDataStore //
    ) {
        super(pEntityManagerFactory);
        this.dataStore = ArgUtil.checkNull(pDataStore, "pDataStore"); //$NON-NLS-1$
    }

    /**
     * @see jp.co.city.tear.service.ILargeDataService#delete(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public void delete(final ELargeData pData) {
        if (pData == null) {
            return;
        }
        final EntityManager em = getEntityManager();
        em.remove(em.merge(pData));
        em.flush();
        this.dataStore.delete(pData.getId().longValue());
    }

    /**
     * @see jp.co.city.tear.service.ILargeDataService#getDataInputStream(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public InputStream getDataInputStream(final ELargeData pData) throws NotFound {
        if (pData == null) {
            throw NotFound.GLOBAL;
        }
        if (!pData.isPersisted()) {
            throw NotFound.GLOBAL;
        }
        return this.dataStore.getDataInputStream(pData.getId().longValue());
    }

    /**
     * @see jp.co.city.tear.service.ILargeDataService#insertOrUpdate(jp.co.city.tear.entity.ELargeData, jabara.general.io.DataOperation)
     */
    @Override
    public void insertOrUpdate(final ELargeData pData, final DataOperation pOperation) {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$

        if (pData.isPersisted()) {
            updateCore(pData, pOperation);
        } else {
            insertCore(pData, pOperation);
        }
    }

    private void insertCore(final ELargeData pData, final DataOperation pOperation) {
        getEntityManager().persist(pData);

        if (pOperation.getOperation() == Operation.NOOP || pOperation.getOperation() == Operation.DELETE) {
            return;
        }
        final IReadableData stream = pOperation.getData();
        try (InputStream in = IoUtil.toBuffered(stream.getInputStream())) {
            pData.setContentType(stream.getContentType());
            pData.setDataLength(stream.getSize());
            pData.setDataName(stream.getName());
            this.dataStore.save(pData.getId().longValue(), in);

        } catch (final EmptyData e) {
            pData.clearData();
        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private void updateCore(final ELargeData pData, final DataOperation pOperation) {
        if (pOperation.getOperation() == Operation.NOOP) {
            return;
        }

        final ELargeData inDb = getEntityManager().merge(pData);
        inDb.clearData();
        this.dataStore.delete(pData.getId().longValue());
        if (pOperation.getOperation() == Operation.DELETE) {
            return;
        }

        final IReadableData stream = pOperation.getData();
        try (InputStream in = IoUtil.toBuffered(stream.getInputStream())) {
            this.dataStore.save(pData.getId().longValue(), in);
            inDb.setContentType(stream.getContentType());
            inDb.setDataLength(stream.getSize());
            inDb.setDataName(stream.getName());

        } catch (final EmptyData e) {
            inDb.clearData();
        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }
}
