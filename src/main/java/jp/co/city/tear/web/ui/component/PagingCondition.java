/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import java.io.Serializable;

/**
 * @author jabaraster
 */
public class PagingCondition implements Serializable {
    private static final long serialVersionUID = 5884079860148123024L;

    private final int         first;
    private final int         count;

    /**
     * @param pFirst -
     * @param pCount -
     */
    public PagingCondition(final int pFirst, final int pCount) {
        this.first = pFirst;
        this.count = pCount;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * @return the first
     */
    public int getFirst() {
        return this.first;
    }

}
