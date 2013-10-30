/**
 * 
 */
package jp.co.city.tear.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author jabaraster
 */
@Entity
public class EPlayLogTrackingDescriptor extends TearEntityBase<EPlayLogTrackingDescriptor> {
    private static final long serialVersionUID          = 7816070476300283311L;

    /**
     * 
     */
    public static final int   MAX_CHAR_COUNT_DESCRIPTOR = 128;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_DESCRIPTOR, unique = true)
    protected String          descriptor;

    /**
     * @return the descriptor
     */
    public String getDescriptor() {
        return this.descriptor;
    }

    /**
     * @param pDescriptor the descriptor to set
     */
    public void setDescriptor(final String pDescriptor) {
        this.descriptor = pDescriptor;
    }
}
