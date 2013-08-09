package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;
import jabara.jpa.entity.EntityBase_;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import jp.co.city.tear.entity.EUser;
import jp.co.city.tear.entity.EUserPassword;
import jp.co.city.tear.entity.EUserPassword_;
import jp.co.city.tear.entity.EUser_;
import jp.co.city.tear.model.Duplicate;
import jp.co.city.tear.service.IUserService;

/**
 * 
 */
public class UserServiceImpl extends JpaDaoBase implements IUserService {
    private static final long serialVersionUID = 5771084556720067384L;

    /**
     * @param pEntityManagerFactory DBアクセス用オブジェクト.
     */
    @Inject
    public UserServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see jp.co.city.tear.service.IUserService#countAll()
     */
    @Override
    public long countAll() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<EUser> root = query.from(EUser.class);

        query.select(builder.count(root));

        try {
            return getSingleResult(em.createQuery(query)).longValue();
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @see jp.co.city.tear.service.IUserService#delete(jp.co.city.tear.entity.EUser)
     */
    @Override
    public void delete(final EUser pUser) {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final EUser c = em.merge(pUser);
        em.remove(findPasswordByUser(pUser));
        em.remove(c);
    }

    /**
     * @see jp.co.city.tear.service.IUserService#findById(long)
     */
    @Override
    public EUser findById(final long pId) throws NotFound {
        return this.findByIdCore(EUser.class, pId);
    }

    /**
     * @see jp.co.city.tear.service.IUserService#get(long, long, jabara.general.Sort)
     */
    @Override
    public List<EUser> get(final long pFirst, final long pCount, final Sort pSort) {
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$

        if (pFirst > Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUser> query = builder.createQuery(EUser.class);
        final Root<EUser> root = query.from(EUser.class);

        query.orderBy(convertOrder(pSort, builder, root));

        return em.createQuery(query) //
                .setFirstResult(convertToInt(pFirst, "pFirst")) // //$NON-NLS-1$
                .setMaxResults(convertToInt(pCount, "pCount")) // //$NON-NLS-1$
                .getResultList();
    }

    /**
     * @see jp.co.city.tear.service.IUserService#getAll(jabara.general.Sort)
     */
    @Override
    public List<EUser> getAll(final Sort pSort) {
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUser> query = builder.createQuery(EUser.class);
        final Root<EUser> root = query.from(EUser.class);
        query.orderBy(convertOrders(Arrays.asList(pSort), builder, root));
        return em.createQuery(query).getResultList();
    }

    /**
     * @see jp.co.city.tear.service.IUserService#insertAdministratorIfNotExists()
     */
    @Override
    public void insertAdministratorIfNotExists() {
        if (existsAministrator()) {
            return;
        }

        final EntityManager em = getEntityManager();

        final EUser member = new EUser();
        member.setAdministrator(true);
        member.setUserId(EUser.DEFAULT_ADMINISTRATOR_USER_ID);
        em.persist(member);

        final EUserPassword password = new EUserPassword();
        password.setPassword(EUser.DEFAULT_ADMINISTRATOR_PASSWORD);
        password.setUser(member);
        em.persist(password);
    }

    /**
     * @see jp.co.city.tear.service.IUserService#insertOrUpdate(jp.co.city.tear.entity.EUser, java.lang.String)
     */
    @Override
    public void insertOrUpdate(final EUser pUser, final String pPassword) throws Duplicate {
        ArgUtil.checkNull(pUser, "pUser"); //$NON-NLS-1$
        if (pUser.isPersisted()) {
            updateCore(pUser, pPassword);
        } else {
            insertCore(pUser, pPassword);
        }
    }

    private void checkCodeDuplicate(final EUser pUser, final DuplicateCheckMode pMode) throws Duplicate {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        final Root<EUser> root = query.from(EUser.class);

        final Subquery<String> sub = query.subquery(String.class);
        final Root<EUser> subRoot = sub.from(EUser.class);
        sub.select(getDummyExpression(builder));
        sub.where(new WhereBuilder() //
                .add(builder.equal(root.get(EntityBase_.id), subRoot.get(EntityBase_.id))) //
                .add(builder.equal(subRoot.get(EUser_.userId), pUser.getUserId())) //
                .addIf(pMode == DuplicateCheckMode.UPDATE, builder.notEqual(subRoot.get(EntityBase_.id), pUser.getId())) //
                .build() //
        );

        query.where(builder.exists(sub));
        query.select(getDummyExpression(builder));

        try {
            getSingleResult(em.createQuery(query));
            throw new Duplicate();
        } catch (final NotFound e) {
            // 重複なし
        }
    }

    private boolean existsAministrator() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        query.from(EUser.class);

        final String DUMMY = "X"; //$NON-NLS-1$
        query.select(builder.literal(DUMMY).alias(DUMMY));

        return !em.createQuery(query).setMaxResults(1).getResultList().isEmpty();
    }

    private EUserPassword findPasswordByUser(final EUser pUser) {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUserPassword> query = builder.createQuery(EUserPassword.class);
        final Root<EUserPassword> root = query.from(EUserPassword.class);
        query.where(builder.equal(root.get(EUserPassword_.user), pUser));
        try {
            return getSingleResult(em.createQuery(query));
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private void insertCore(final EUser pUser, final String pPassword) throws Duplicate {
        checkCodeDuplicate(pUser, DuplicateCheckMode.INSERT);
        final EntityManager em = getEntityManager();
        em.persist(pUser);
        em.persist(new EUserPassword(pUser, pPassword));
    }

    private void updateCore(final EUser pUser, final String pPassword) throws Duplicate {
        checkCodeDuplicate(pUser, DuplicateCheckMode.UPDATE);
        final EntityManager em = getEntityManager();
        if (!em.contains(pUser)) {
            final EUser target = em.merge(pUser);
            target.setUserId(pUser.getUserId());
        }

        final EUserPassword password = findPasswordByUser(pUser);
        password.setPassword(pPassword);
    }

    private enum DuplicateCheckMode {
        INSERT, UPDATE;
    }

}
