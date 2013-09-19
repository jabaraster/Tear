/**
 * 
 */
package jp.co.city.tear.model;

import jabara.general.ArgUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

/**
 * @author jabaraster
 */
public class NamedInputStream implements Closeable {

    private final String      name;
    private final InputStream inputStream;

    /**
     * @param pName
     * @param pInputStream
     */
    public NamedInputStream(final String pName, final InputStream pInputStream) {
        ArgUtil.checkNullOrEmpty(pName, "pName"); //$NON-NLS-1$
        ArgUtil.checkNull(pInputStream, "pInputStream"); //$NON-NLS-1$
        this.name = pName;
        this.inputStream = pInputStream;
    }

    /**
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }

    /**
     * @return inputStreamを返す.
     */
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * @return nameを返す.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return -
     */
    public String getType() {
        return StringUtils.substringAfterLast(this.name, "."); //$NON-NLS-1$
    }

}
