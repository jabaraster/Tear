/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import jp.co.city.tear.Environment;
import jp.co.city.tear.service.ITempFileService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author jabaraster
 */
public class TempFileServiceImpl implements ITempFileService, Serializable {
    private static final long  serialVersionUID = 6638344987827521884L;

    /**
     * 
     */
    public static final String DEFAULT_SUFFIX   = ".tmp";              //$NON-NLS-1$

    private static final File  BASE_DIR         = getBaseDirectory();

    /**
     * @see jp.co.city.tear.service.ITempFileService#cleanUp()
     */
    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        throw new NotImplementedException();
    }

    /**
     * @see jp.co.city.tear.service.ITempFileService#create(java.lang.Class, java.lang.String)
     */
    @Override
    public File create(final Class<?> pPrefix, final String pSuffix) {
        ArgUtil.checkNull(pPrefix, "pPrefix"); //$NON-NLS-1$
        return this.create(pPrefix.getName(), pSuffix);
    }

    /**
     * @see jp.co.city.tear.service.ITempFileService#create(java.lang.String, java.lang.String)
     */
    @Override
    public File create(final String pPrefix, final String pSuffix) {
        try {
            return File.createTempFile(pPrefix, n(pSuffix), BASE_DIR);
        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static File getBaseDirectory() {
        final String s = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
        if (s == null) {
            throw new IllegalStateException();
        }
        final File ret = new File(s, Environment.getApplicationName());
        ret.mkdirs();
        if (!ret.isDirectory()) {
            throw new IllegalStateException();
        }
        return ret;
    }

    private static String n(final String pSuffix) {
        if (pSuffix == null) {
            return DEFAULT_SUFFIX;
        }
        if (pSuffix.trim().length() == 0) {
            return DEFAULT_SUFFIX;
        }
        if (pSuffix.charAt(0) != '.') {
            return "." + pSuffix; //$NON-NLS-1$
        }
        return pSuffix;
    }

}
