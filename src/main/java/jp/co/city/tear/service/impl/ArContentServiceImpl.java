/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;
import jabara.jpa.entity.EntityBase_;

import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.entity.EArContent_;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.model.LoginUser;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.service.ILargeDataService;
import jp.co.city.tear.service.IUserService;

/**
 * @author jabaraster
 */
public class ArContentServiceImpl extends JpaDaoBase implements IArContentService {
    private static final long       serialVersionUID = -3178997561485535488L;

    private final IUserService      userService;
    private final ILargeDataService largeDataService;

    /**
     * @param pEntityManagerFactory -
     * @param pUserService -
     * @param pLargeDataService -
     */
    @Inject
    public ArContentServiceImpl( //
            final EntityManagerFactory pEntityManagerFactory //
            , final IUserService pUserService //
            , final ILargeDataService pLargeDataService //
    ) {
        super(pEntityManagerFactory);
        this.userService = ArgUtil.checkNull(pUserService, "pUserService"); //$NON-NLS-1$
        this.largeDataService = ArgUtil.checkNull(pLargeDataService, "pLargeDataService"); //$NON-NLS-1$
    }

    /**
     * @see jp.co.city.tear.service.IArContentService#count(jp.co.city.tear.model.LoginUser)
     */
    @Override
    public long count(final LoginUser pLoginUser) {
        ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<EArContent> root = query.from(EArContent.class);

        query.select(builder.count(root));

        if (!pLoginUser.isAdministrator()) {
            query.where( //
            builder.equal(root.get(EArContent_.owner).get(EntityBase_.id), Long.valueOf(pLoginUser.getId())) //
            );
        }

        try {
            return getSingleResult(em.createQuery(query)).longValue();
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @see jp.co.city.tear.service.IArContentService#delete(jp.co.city.tear.entity.EArContent)
     */
    @Override
    public void delete(final EArContent pArContent) {
        ArgUtil.checkNull(pArContent, "pArContent"); //$NON-NLS-1$

        this.largeDataService.delete(pArContent.getMarker());
        this.largeDataService.delete(pArContent.getContent());

        final EntityManager em = getEntityManager();
        em.remove(em.merge(pArContent));
    }

    /**
     * @see jp.co.city.tear.service.IArContentService#find(jp.co.city.tear.model.LoginUser, long, long, jabara.general.Sort)
     */
    @Override
    public List<EArContent> find(final LoginUser pLoginUser, final long pFirst, final long pCount, final Sort pSort) {
        ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$
        final int first = convertToInt(pFirst, "pFirst"); //$NON-NLS-1$
        final int count = convertToInt(pCount, "pCount"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EArContent> query = builder.createQuery(EArContent.class);
        final Root<EArContent> root = query.from(EArContent.class);

        query.select(root);
        root.fetch(EArContent_.marker, JoinType.LEFT);
        root.fetch(EArContent_.content, JoinType.LEFT);

        if (!pLoginUser.isAdministrator()) {
            query.where( //
            builder.equal(root.get(EArContent_.owner).get(EntityBase_.id), Long.valueOf(pLoginUser.getId())) //
            );
        }

        query.orderBy( //
        convertOrder(pSort, builder, root) //
        );

        return em.createQuery(query).setFirstResult(first).setMaxResults(count).getResultList();
    }

    /**
     * @see jp.co.city.tear.service.IArContentService#findById(jp.co.city.tear.model.LoginUser, long)
     */
    @Override
    public EArContent findById(final LoginUser pUser, final long pId) throws NotFound {
        final EArContent ret = findByIdCore(EArContent.class, pId);
        if (pUser.isAdministrator()) {
            return ret;
        }
        if (ret.getOwner().getId().longValue() != pUser.getId()) {
            throw NotFound.GLOBAL;
        }
        return ret;
    }

    /**
     * @see jp.co.city.tear.service.IArContentService#getDataInputStream(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public InputStream getDataInputStream(final ELargeData pData) throws NotFound {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$
        return this.largeDataService.getDataInputStream(pData);
    }

    /**
     * @see jp.co.city.tear.service.IArContentService#insertOrUpdate(jp.co.city.tear.model.LoginUser, jp.co.city.tear.entity.EArContent,
     *      java.io.InputStream, java.io.InputStream)
     */
    @Override
    public void insertOrUpdate( //
            final LoginUser pLoginUser //
            , final EArContent pArContent //
            , final InputStream pMarkerData //
            , final InputStream pContentData //
    ) {
        ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$
        ArgUtil.checkNull(pArContent, "pArContent"); //$NON-NLS-1$

        try {
            final EUser loginUser = this.userService.findById(pLoginUser.getId());
            pArContent.setOwner(loginUser);

            if (pArContent.isPersisted()) {
                updateCore(pArContent, pMarkerData, pContentData);
            } else {
                insertCore(pArContent, pMarkerData, pContentData);
            }

        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private void insertCore(final EArContent pArContent, final InputStream pMarkerData, final InputStream pContentData) {
        this.largeDataService.insertOrUpdate(pArContent.getMarker(), pMarkerData);
        this.largeDataService.insertOrUpdate(pArContent.getContent(), pContentData);
        getEntityManager().persist(pArContent);
    }

    private void updateCore(final EArContent pArContent, final InputStream pMarkerData, final InputStream pContentData) {
        this.largeDataService.insertOrUpdate(pArContent.getMarker(), pMarkerData);
        this.largeDataService.insertOrUpdate(pArContent.getContent(), pContentData);
        final EArContent inDb = getEntityManager().merge(pArContent);
        inDb.setTitle(pArContent.getTitle());
    }
}
