/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.bean.BeanProperties;
import jabara.bean.annotation.Localized;
import jabara.bean.annotation.Order;
import jabara.general.ArgUtil;
import jabara.jpa.entity.EntityBase;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jabaraster
 */
@Entity
public class EArContent extends EntityBase<EArContent> {
    private static final long           serialVersionUID             = 9049354788625755282L;

    private static final BeanProperties _properties                  = BeanProperties.getInstance(EArContent.class);

    /**
     * 
     */
    public static final int             MAX_CHAR_COUNT_TITLE         = 100;

    /**
     * 
     */
    public static final float           DEFAULT_SIMILARITY_THRESHOLD = 0.7f;

    /**
     * 
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    protected EUser                     owner;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_TITLE * 3)
    @NotNull
    @Size(min = 1, max = MAX_CHAR_COUNT_TITLE)
    protected String                    title;

    /**
     * 
     */
    protected float                     similarityThreshold          = DEFAULT_SIMILARITY_THRESHOLD;

    /**
     * 
     */
    @OneToOne
    @JoinColumn(nullable = false)
    protected ELargeData                marker                       = new ELargeData();

    /**
     * 
     */
    @OneToOne
    @JoinColumn(nullable = false)
    protected ELargeData                content                      = new ELargeData();

    /**
     * @param pOperation -
     */
    public void contentOperation(final IOperation pOperation) {
        ArgUtil.checkNull(pOperation, "pOperation"); //$NON-NLS-1$
        if (this.content != null) {
            pOperation.run(this.content);
        }
    }

    /**
     * @return contentsを返す.
     */
    public ELargeData getContent() {
        return this.content;
    }

    /**
     * @see jabara.jpa.entity.EntityBase#getCreated()
     */
    @Override
    @Localized
    @Order(300)
    public Date getCreated() {
        return super.getCreated();
    }

    /**
     * @see jabara.jpa.entity.EntityBase#getId()
     */
    @Override
    @Localized
    @Order(0)
    public Long getId() {
        return super.getId();
    }

    /**
     * @return the marker
     */
    public ELargeData getMarker() {
        return this.marker;
    }

    /**
     * @return ownerを返す.
     */
    @Localized
    @Order(200)
    public EUser getOwner() {
        return this.owner;
    }

    /**
     * @return the similarityThreshold
     */
    public float getSimilarityThreshold() {
        return this.similarityThreshold;
    }

    /**
     * @return titleを返す.
     */
    @Localized
    @Order(100)
    public String getTitle() {
        return this.title;
    }

    /**
     * @see jabara.jpa.entity.EntityBase#getUpdated()
     */
    @Override
    @Localized
    @Order(400)
    public Date getUpdated() {
        return super.getUpdated();
    }

    /**
     * @param pOperation markerがnullでないときに実行する処理.
     */
    public void markerOperation(final IOperation pOperation) {
        ArgUtil.checkNull(pOperation, "pOperation"); //$NON-NLS-1$
        if (this.marker != null) {
            pOperation.run(this.marker);
        }
    }

    /**
     * @param pOwner ownerを設定.
     */
    public void setOwner(final EUser pOwner) {
        this.owner = pOwner;
    }

    /**
     * @param pSimilarityThreshold the similarityThreshold to set
     */
    public void setSimilarityThreshold(final float pSimilarityThreshold) {
        this.similarityThreshold = pSimilarityThreshold;
    }

    /**
     * @param pTitle titleを設定.
     */
    public void setTitle(final String pTitle) {
        this.title = pTitle;
    }

    /**
     * @return -
     */
    public static BeanProperties getMeta() {
        return _properties;
    }

    /**
     * @author jabaraster
     */
    public interface IOperation {
        /**
         * @param pData
         */
        void run(ELargeData pData);
    }
}
