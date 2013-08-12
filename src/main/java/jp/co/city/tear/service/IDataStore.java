/**
 * 
 */
package jp.co.city.tear.service;

import jabara.general.NotFound;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author jabaraster
 */
public interface IDataStore extends Serializable {
    /**
     * @param pDataId -
     */
    void delete(long pDataId);

    /**
     * @param pDataId -
     * @return -
     * @throws NotFound -
     */
    InputStream getDataInputStream(long pDataId) throws NotFound;

    /**
     * @param pDataId -
     * @param pStream
     * @return 保存したデータ長です. <br>
     *         データ長が計測不能の場合は-1を返します.
     * @throws EmptyData pStreamがnullの場合.
     */
    int save(long pDataId, InputStream pStream) throws EmptyData;

    /**
     * @author jabaraster
     */
    public static class EmptyData extends Exception {
        private static final long  serialVersionUID = 7471082738552244692L;

        /**
         * 
         */
        @SuppressWarnings("synthetic-access")
        public static final Global GLOBAL           = new Global();

        /**
         * @author jabaraster
         */
        public static final class Global extends EmptyData {
            private static final long serialVersionUID = 6130290738469194915L;

            private Global() {
                //
            }
        }
    }
}
