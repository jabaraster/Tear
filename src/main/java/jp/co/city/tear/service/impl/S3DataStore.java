/**
 * 
 */
package jp.co.city.tear.service.impl;

import jabara.general.ArgUtil;
import jabara.general.IoUtil;
import jp.co.city.tear.Environment;
import jp.co.city.tear.entity.ELargeData;
import jp.co.city.tear.service.IDataStore;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * @author jabaraster
 */
public class S3DataStore implements IDataStore {
    private static final long serialVersionUID = 367806927392618725L;

    /**
     * @see jp.co.city.tear.service.IDataStore#delete(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public void delete(final ELargeData pData) {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$

        final String fileName = buildFileName(pData.getId().longValue());
        final DeleteObjectRequest request = new DeleteObjectRequest(Environment.getAwsBucketName(), fileName);
        final AmazonS3Client s3Client = createS3Client();

        s3Client.deleteObject(request);
    }

    /**
     * @see jp.co.city.tear.service.IDataStore#save(jp.co.city.tear.entity.ELargeData)
     */
    @Override
    public int save(final ELargeData pData) {
        ArgUtil.checkNull(pData, "pData"); //$NON-NLS-1$

        final String fileName = buildFileName(pData.getId().longValue());
        final ObjectMetadata meta = new ObjectMetadata();
        final PutObjectRequest request = new PutObjectRequest(Environment.getAwsBucketName(), fileName, IoUtil.toBuffered(pData.getData()), meta);
        final AmazonS3Client s3Client = createS3Client();

        s3Client.putObject(request);

        return -1;
    }

    private static String buildFileName(final long pId) {
        return ELargeData.class.getSimpleName() + "_" + pId + ".dat"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static AmazonS3Client createS3Client() {
        return new AmazonS3Client(new BasicAWSCredentials(Environment.getAwsAccessKey(), Environment.getAwsSecretKey()));
    }

}
