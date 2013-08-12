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
        this.dataStore.delete(pData);
    }

    /**
     * @see jp.co.city.tear.service.ILargeDataService#getDataInputStream(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public InputStream getDataInputStream(final ELargeData pData) throws NotFound {
        if (pData == null) {
            throw NotFound.GLOBAL;
        }
        return this.dataStore.getDataInputStream(pData);
    }

    /**
     * @see jp.co.city.tear.service.ILargeDataService#insert(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public void insert(final ELargeData pData) {
        if (pData == null || pData.getData() == null) {
            return;
        }

        getEntityManager().persist(pData);

        final int length = this.dataStore.save(pData);
        pData.setLength(length);
    }
}
