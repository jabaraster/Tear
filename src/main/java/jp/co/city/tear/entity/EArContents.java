/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import com.esotericsoftware.kryo.NotNull;

/**
 * @author jabaraster
 */
@Entity
public class EArContents extends EntityBase<EArContents> {
    private static final long serialVersionUID     = 9049354788625755282L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_TITLE = 100;

    /**
     * 
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    protected EUser           owner;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_TITLE * 3)
    @NotNull
    @Size(min = 1, max = MAX_CHAR_COUNT_TITLE)
    protected String          title;

    /**
     * 
     */
    @OneToOne
    @JoinColumn(nullable = true)
    protected ELargeData      marker;

    /**
     * 
     */
    @OneToOne
    @JoinColumn(nullable = true)
    protected ELargeData      contents;

    /**
     * @return contentsを返す.
     */
    public ELargeData getContents() {
        return this.contents;
    }

    /**
     * @return markerを返す.
     */
    public ELargeData getMarker() {
        return this.marker;
    }

    /**
     * @return ownerを返す.
     */
    public EUser getOwner() {
        return this.owner;
    }

    /**
     * @return titleを返す.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param pContents contentsを設定.
     */
    public void setContents(final ELargeData pContents) {
        this.contents = pContents;
    }

    /**
     * @param pMarker markerを設定.
     */
    public void setMarker(final ELargeData pMarker) {
        this.marker = pMarker;
    }

    /**
     * @param pOwner ownerを設定.
     */
    public void setOwner(final EUser pOwner) {
        this.owner = pOwner;
    }

    /**
     * @param pTitle titleを設定.
     */
    public void setTitle(final String pTitle) {
        this.title = pTitle;
    }
}
