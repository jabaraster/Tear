/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import java.io.InputStream;

import javax.inject.Inject;
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
        getEntityManager().remove(pData);
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
        return this.dataStore.getDataInputStream(pData.getId().longValue());
    }

    /**
     * @see jp.co.city.tear.service.ILargeDataService#insertOrUpdate(jp.co.city.tear.entity.ELargeData, java.io.InputStream)
     */
    @Override
    public void insertOrUpdate(final ELargeData pData, final InputStream pStream) {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$

        if (pData.isPersisted()) {
            updateCore(pData, pStream);
        } else {
            insertCore(pData, pStream);
        }
    }

    private void insertCore(final ELargeData pData, final InputStream pStream) {
        try {
            getEntityManager().persist(pData);

            final int len = this.dataStore.save(pData.getId().longValue(), pStream);
            pData.setDataLength(len);
        } catch (final EmptyData e) {
            pData.clearData();
        }
    }

    private void updateCore(final ELargeData pData, final InputStream pStream) {
        final ELargeData inDb = getEntityManager().merge(pData);
        this.dataStore.delete(pData.getId().longValue());

        try {
            final int len = this.dataStore.save(pData.getId().longValue(), pStream);
            inDb.setDataLength(len);
        } catch (final EmptyData e) {
            inDb.clearData();
        }
    }
}
