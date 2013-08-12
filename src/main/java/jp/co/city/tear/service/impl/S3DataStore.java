/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ExceptionUtil;
import jabara.general.NotFound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jp.co.city.tear.Environment;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.IDataStore;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * @author jabaraster
 */
public class S3DataStore implements IDataStore {
    private static final long serialVersionUID = 367806927392618725L;

    /**
     * @see jp.co.city.tear.service.IDataStore#delete(long)
     */
    @Override
    public void delete(final long pDataId) {
        final String fileName = buildFileName(pDataId);
        final DeleteObjectRequest request = new DeleteObjectRequest(Environment.getAwsBucketName(), fileName);
        final AmazonS3Client s3Client = createS3Client();
        s3Client.deleteObject(request);
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#getDataInputStream(long)
     */
    @Override
    public InputStream getDataInputStream(final long pDataId) throws NotFound {
        final String fileName = buildFileName(pDataId);
        final GetObjectRequest request = new GetObjectRequest(Environment.getAwsBucketName(), fileName);
        final AmazonS3Client s3Client = createS3Client();
        final File tempFile = buildTemporarryFileName();
        s3Client.getObject(request, tempFile);

        try {
            return new FileInputStream(tempFile) {
                @Override
                public void close() throws IOException {
                    try {
                        super.close();
                    } finally {
                        tempFile.delete();
                    }
                }
            };
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

        final String fileName = buildFileName(pDataId);
        final ObjectMetadata meta = new ObjectMetadata();
        final PutObjectRequest request = new PutObjectRequest(Environment.getAwsBucketName(), fileName, pStream, meta);
        final AmazonS3Client s3Client = createS3Client();
        s3Client.putObject(request);
        return -1;
    }

    private static String buildFileName(final long pId) {
        return ELargeData.class.getSimpleName() + "_" + pId + ".dat"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static File buildTemporarryFileName() {
        try {
            return File.createTempFile(S3DataStore.class.getName(), ".dat"); //$NON-NLS-1$
        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static AmazonS3Client createS3Client() {
        return new AmazonS3Client(new BasicAWSCredentials(Environment.getAwsAccessKey(), Environment.getAwsSecretKey()));
    }
}
