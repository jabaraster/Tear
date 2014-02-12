/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.bean.BeanProperties;
import jabara.bean.annotation.Localized;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author jabaraster
 */
@Entity
public class EArContentPlayLog extends TearEntityBase<EArContentPlayLog> {
    private static final long     serialVersionUID                   = 1737456524802201838L;

    /**
     * 
     */
    public static final int       MAX_CHAR_COUNT_TRACKING_DESCRIPTOR = 50;

    private static BeanProperties _properties                        = BeanProperties.getInstance(EArContentPlayLog.class);

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date                playDatetime;

    /**
     * 
     */
    @Column(nullable = false)
    protected Long                arContentId;

    /**
     * 緯度.
     */
    @Column(nullable = true)
    protected Double              latitude;

    /**
     * 経度
     */
    @Column(nullable = true)
    protected Double              longitude;

    /**
     * 同じ端末で再生されたARに付与される識別子.
     */
    @Column(nullable = true, length = MAX_CHAR_COUNT_TRACKING_DESCRIPTOR)
    protected String              trackingDescriptor;

    /**
     * @return the arContentId
     */
    @Localized("ARコンテンツID")
    public Long getArContentId() {
        return this.arContentId;
    }

    /**
     * @return the latitude
     */
    @Localized("緯度")
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * @return the longitude
     */
    @Localized("経度")
    public Double getLongitude() {
        return this.longitude;
    }

    /**
     * @return the playDatetime
     */
    @Localized("再生日時")
    public Date getPlayDatetime() {
        return this.playDatetime == null ? null : new Date(this.playDatetime.getTime());
    }

    /**
     * @return the trackingDescriptor
     */
    @Localized("トラッキング用識別子")
    public String getTrackingDescriptor() {
        return this.trackingDescriptor;
    }

    /**
     * @param pArContentId the arContentId to set
     */
    public void setArContentId(final Long pArContentId) {
        this.arContentId = pArContentId;
    }

    /**
     * @param pLatitude the latitude to set
     */
    public void setLatitude(final Double pLatitude) {
        this.latitude = pLatitude;
    }

    /**
     * @param pLongitude the longitude to set
     */
    public void setLongitude(final Double pLongitude) {
        this.longitude = pLongitude;
    }

    /**
     * @param pPlayDatetime the playDatetime to set
     */
    public void setPlayDatetime(final Date pPlayDatetime) {
        this.playDatetime = pPlayDatetime == null ? null : new Date(pPlayDatetime.getTime());
    }

    /**
     * @param pTrackingDescriptor the trackingDescriptor to set
     */
    public void setTrackingDescriptor(final String pTrackingDescriptor) {
        this.trackingDescriptor = pTrackingDescriptor;
    }

    /**
     * @return プロパティ情報.
     */
    public static BeanProperties getMeta() {
        return _properties;
    }
}
