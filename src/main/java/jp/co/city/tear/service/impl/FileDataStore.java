/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;
import jabara.general.NotFound;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.city.tear.Environment;
import jp.co.city.tear.service.IDataStore;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
public class FileDataStore implements IDataStore {
    private static final long serialVersionUID = 3197919762689255185L;

    /**
     * @see jp.co.city.tear.service.IDataStore#delete(long)
     */
    @Override
    public void delete(final long pDataId) {
        buildPath(pDataId).delete();
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#getDataInputStream(long)
     */
    @Override
    public InputStream getDataInputStream(final long pDataId) throws NotFound {
        try {
            return new FileInputStream(buildPath(pDataId));
        } catch (final FileNotFoundException e) {
            throw NotFound.GLOBAL;
        }
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#save(long, java.io.InputStream)
     */
    @Override
    public int save(final long pDataId, final InputStream pStream) throws EmptyData {
        if (pStream == null) {
            throw EmptyData.GLOBAL;
        }

        final File saveFile = buildPath(pDataId);
        try (final OutputStream out = new FileOutputStream(saveFile); //
                final BufferedOutputStream bufOut = new BufferedOutputStream(out)) {

            return IOUtils.copy(IoUtil.toBuffered(pStream), bufOut);

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static File buildPath(final long pId) {
        final String fileName = FileDataStore.class.getSimpleName() + "_" + pId + ".dat"; //$NON-NLS-1$ //$NON-NLS-2$
        return new File(Environment.getDataStoreDirectory(), fileName);
    }
}
