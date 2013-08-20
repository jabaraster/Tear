/**
 * 
 */
package jp.co.city.tear.model;

import jabara.general.ArgUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jabaraster
 */
public class LargeDataOperation implements AutoCloseable {

    private Mode        mode = Mode.NOOP;
    private InputStream data;

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
    public InputStream getData() {
        if (this.data == null) {
            throw new IllegalStateException();
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
    public LargeDataOperation update(final InputStream pData) {
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
