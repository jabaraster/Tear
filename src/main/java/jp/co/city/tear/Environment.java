package jp.co.city.tear;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
@SuppressWarnings("nls")
public final class Environment {

    private static final String        APPLICATION_NAME_LOWER       = "tear";
    private static final String        APPLICATION_NAME             = "Tear";

    /**
     * 
     */
    public static final String         PARAM_PREFIX                 = APPLICATION_NAME_LOWER + ".";

    /**
     * 
     */
    public static final String         PARAM_DATA_STORE_MODE        = PARAM_PREFIX + "dataStoreMode";

    /**
     * 
     */
    public static final String         PARAM_AWS_BUCKET_NAME        = PARAM_PREFIX + "awsBucketName";

    /**
     * 
     */
    public static final String         PARAM_AWS_ACCESS_KEY         = PARAM_PREFIX + "awsAccessKey";

    /**
     * 
     */
    public static final String         PARAM_AWS_SECRET_KEY         = PARAM_PREFIX + "awsSecretKey";

    /**
     * 
     */
    public static final String         PARAM_ABSOLUTE_REST_URL_ROOT = PARAM_PREFIX + "absoluteRestUrlRoot";

    private static final AtomicBoolean _dataStoreDirectoryCreated   = new AtomicBoolean(false);

    /**
     * @return -
     */
    public static String getAbsoluteRestUrlRoot() {
        return getString(PARAM_ABSOLUTE_REST_URL_ROOT, "http://localhost:8081/rest/");
    }

    /**
     * @return アプリケーション名.
     */
    public static String getApplicationName() {
        return APPLICATION_NAME;
    }

    /**
     * @return -
     */
    public static String getAwsAccessKey() {
        return getString(PARAM_AWS_ACCESS_KEY, null);
    }

    /**
     * @return -
     */
    public static String getAwsBucketName() {
        return getString(PARAM_AWS_BUCKET_NAME, null);
    }

    /**
     * @return -
     */
    public static String getAwsSecretKey() {
        return getString(PARAM_AWS_SECRET_KEY, null);
    }

    /**
     * @return -
     */
    public static File getDataStoreDirectory() {
        final File file = new File("~/." + APPLICATION_NAME_LOWER + "/data"); //$NON-NLS-1$
        if (!_dataStoreDirectoryCreated.get()) {
            file.mkdirs();
        }
        if (!file.isDirectory()) {
            throw new IllegalStateException();
        }

        _dataStoreDirectoryCreated.set(true);

        return file;
    }

    /**
     * @return -
     */
    public static DataStoreMode getDataStoreMode() {
        return DataStoreMode.valueOf(getString(PARAM_DATA_STORE_MODE, DataStoreMode.FILE));
    }

    private static String getString(final String pParameterName, final Object pDefaultValue) {
        String value = System.getenv(pParameterName);
        if (value == null) {
            value = System.getProperty(pParameterName); // テスト用.
        }
        if (value != null) {
            return value;
        }
        return pDefaultValue == null ? null : pDefaultValue.toString(); //$NON-NLS-1$
    }

    /**
     * @author jabaraster
     */
    public enum DataStoreMode {
        /**
         * 
         */
        FILE,
        /**
         * 
         */
        S3;
    }
}
