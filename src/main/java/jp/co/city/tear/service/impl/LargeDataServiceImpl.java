/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.jpa.JpaDaoBase;

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
     * @see jp.co.city.tear.service.ILargeDataService#insert(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public void insert(final ELargeData pData) {
        if (pData == null) {
            return;
        }

        getEntityManager().persist(pData);

        if (pData.getData() == null) {
            pData.setLength(0);
            return;
        }

        final int length = this.dataStore.save(pData);
        pData.setLength(length);
    }
}
