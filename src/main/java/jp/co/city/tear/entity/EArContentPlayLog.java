/**
 * 
 */
package jp.co.city.tear.entity;

import jabara.jpa.entity.EntityBase;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author jabaraster
 */
@Entity
public class EArContentPlayLog extends EntityBase<EArContentPlayLog> {
    private static final long serialVersionUID                   = 1737456524802201838L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_TRACKING_DESCRIPTOR = 50;

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date            playDatetime;

    /**
     * 
     */
    @Column(nullable = false)
    protected Long            arContentId;

    /**
     * 緯度.
     */
    @Column(nullable = true)
    protected Double          latitude;

    /**
     * 経度
     */
    @Column(nullable = true)
    protected Double          longitude;
    /**
     * 同じ端末で再生されたARに付与される識別子.
     */
    @Column(nullable = true, length = MAX_CHAR_COUNT_TRACKING_DESCRIPTOR)
    protected String          trackingDescriptor;

    /**
     * @return the arContentId
     */
    public Long getArContentId() {
        return this.arContentId;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return this.longitude;
    }

    /**
     * @return the playDatetime
     */
    public Date getPlayDatetime() {
        return this.playDatetime == null ? null : new Date(this.playDatetime.getTime());
    }

    /**
     * @return the trackingDescriptor
     */
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

}
