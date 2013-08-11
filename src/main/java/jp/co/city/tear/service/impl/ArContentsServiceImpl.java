/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.jpa.JpaDaoBase;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import jp.co.city.tear.entity.EArContents;
import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.model.LoginUser;
import jp.co.city.tear.service.IArContentsService;
import jp.co.city.tear.service.ILargeDataService;
import jp.co.city.tear.service.IUserService;

/**
 * @author jabaraster
 */
public class ArContentsServiceImpl extends JpaDaoBase implements IArContentsService {
    private static final long       serialVersionUID = -3178997561485535488L;

    private final IUserService      userService;
    private final ILargeDataService largeDataService;

    /**
     * @param pEntityManagerFactory -
     * @param pUserService -
     * @param pLargeDataService -
     */
    @Inject
    public ArContentsServiceImpl( //
            final EntityManagerFactory pEntityManagerFactory //
            , final IUserService pUserService //
            , final ILargeDataService pLargeDataService //
    ) {
        super(pEntityManagerFactory);
        this.userService = ArgUtil.checkNull(pUserService, "pUserService"); //$NON-NLS-1$
        this.largeDataService = ArgUtil.checkNull(pLargeDataService, "pLargeDataService"); //$NON-NLS-1$
    }

    @Override
    public long count(final LoginUser pLoginUser) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<EArContents> find(final LoginUser pLoginUser, final long pFirst, final long pCount) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see jp.co.city.tear.service.IArContentsService#findById(jp.co.city.tear.model.LoginUser, long)
     */
    @Override
    public EArContents findById(final LoginUser pUser, final long pId) throws NotFound {
        final EArContents ret = findByIdCore(EArContents.class, pId);
        if (pUser.isAdministrator()) {
            return ret;
        }
        if (ret.getOwner().getId().longValue() != pUser.getId()) {
            throw NotFound.GLOBAL;
        }
        return ret;
    }

    /**
     * @see jp.co.city.tear.service.IArContentsService#insertOrUpdate(LoginUser, jp.co.city.tear.entity.EArContents)
     */
    @Override
    public void insertOrUpdate( //
            final LoginUser pLoginUser //
            , final EArContents pArContents //
    ) {
        ArgUtil.checkNull(pArContents, "pArContents"); //$NON-NLS-1$

        try {
            final EUser loginUser = this.userService.findById(pLoginUser.getId());
            pArContents.setOwner(loginUser);

            if (pArContents.isPersisted()) {
                updateCore(pLoginUser, pArContents);
            } else {
                insertCore(pArContents);
            }
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private void insertCore(final EArContents pArContents) {
        this.largeDataService.insert(pArContents.getMarker());
        this.largeDataService.insert(pArContents.getContents());
        getEntityManager().persist(pArContents);
    }

    private void updateCore(final LoginUser pUser, final EArContents pArContents) {
        try {
            final EArContents c = findById(pUser, pArContents.getId().longValue());
            this.largeDataService.delete(c.getMarker());
            this.largeDataService.delete(c.getContents());

            this.largeDataService.insert(pArContents.getMarker());
            this.largeDataService.insert(pArContents.getContents());

            c.setTitle(pArContents.getTitle());
            c.setMarker(pArContents.getMarker());
            c.setContents(pArContents.getContents());

        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }
}
