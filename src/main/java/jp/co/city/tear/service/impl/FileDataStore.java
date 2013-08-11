/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jp.co.city.tear.Environment;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.IDataStore;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
public class FileDataStore implements IDataStore {
    private static final long serialVersionUID = 3197919762689255185L;

    /**
     * @see jp.co.city.tear.service.IDataStore#delete(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public void delete(final ELargeData pData) {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$
        buildPath(pData.getId().longValue()).delete();
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#save(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public int save(final ELargeData pData) {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$

        final File saveFile = buildPath(pData.getId().longValue());
        try (final OutputStream out = new FileOutputStream(saveFile); //
                final BufferedOutputStream bufOut = new BufferedOutputStream(out)) {

            return IOUtils.copy(IoUtil.toBuffered(pData.getData()), bufOut);

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static File buildPath(final long pId) {
        final String fileName = FileDataStore.class.getSimpleName() + "_" + pId + ".dat"; //$NON-NLS-1$ //$NON-NLS-2$
        return new File(Environment.getDataStoreDirectory(), fileName);
    }
}
