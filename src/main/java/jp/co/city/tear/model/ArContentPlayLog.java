/**
 * 
 */
package jp.co.city.tear.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jabaraster
 */
public class ArContentPlayLog implements Serializable {
    private static final long serialVersionUID = -545232662431045755L;

    private Date              playDatetime;

    private long              arContentId;

    /**
     * 緯度.
     */
    private Double            latitude;

    /**
     * 経度
     */
    private Double            longitude;

    /**
     * 同じ端末で再生されたARに付与される識別子.
     */
    private String            trackingDescriptor;

    /**
     * @return the arContentId
     */
    public long getArContentId() {
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
        return this.playDatetime;
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
    public void setArContentId(final long pArContentId) {
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
        this.playDatetime = pPlayDatetime;
    }

    /**
     * @param pTrackingDescriptor the trackingDescriptor to set
     */
    public void setTrackingDescriptor(final String pTrackingDescriptor) {
        this.trackingDescriptor = pTrackingDescriptor;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "ArContentPlayLog [playDatetime=" + this.playDatetime + ", arContentId=" + this.arContentId + ", latitude=" + this.latitude
                + ", longitude=" + this.longitude + ", trackingDescriptor=" + this.trackingDescriptor + "]";
    }
}
