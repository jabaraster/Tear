/**
 * 
 */
package jp.co.city.tear.model;

import jabara.general.ArgUtil;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author jabaraster
 */
public class LargeDataOperation implements Closeable {

    private Mode             mode = Mode.NOOP;
    private NamedInputStream data;

    /**
     * @return このオブジェクト自身.
     */
    public LargeDataOperation cancel() {
        this.mode = Mode.NOOP;
        this.data = null;
        return this;
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws IOException {
        if (this.data != null) {
            this.data.close();
        }
    }

    /**
     * @return このオブジェクト自身.
     */
    public LargeDataOperation delete() {
        this.mode = Mode.DELETE;
        this.data = null;
        return this;
    }

    /**
     * @return the data
     */
    public NamedInputStream getData() {
        if (this.data == null) {
            return null;
        }
        return this.data;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * @param pData -
     * @return このオブジェクト自身.
     */
    public LargeDataOperation update(final NamedInputStream pData) {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$
        this.mode = Mode.UPDATE;
        this.data = pData;
        return this;
    }

    /**
     * @author jabaraster
     */
    public enum Mode {
        /**
         * 
         */
        NOOP,
        /**
         * 
         */
        UPDATE,
        /**
         * 
         */
        DELETE, ;
    }
}
