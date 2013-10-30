/**
 * 
 */
package jp.co.city.tear.util;

import java.util.Date;
import java.util.TimeZone;

import jp.co.city.tear.Environment;

/**
 * @author jabaraster
 */
public final class DateUtil {

    private static final int TZ_ASIA_TOKYO_RAW_OFFSET = TimeZone.getTimeZone(Environment.getApplicationTimeZone()).getRawOffset();
    private static final int TZ_DEFAULT_RAW_OFFSET    = TimeZone.getDefault().getRawOffset();

    private DateUtil() {
        //
    }

    /**
     * @param pSource
     * @return 東京時刻に変換. pSourceがnullの場合はnull.
     */
    public static Date toApplicationTimeZone(final Date pSource) {
        if (pSource == null) {
            return null;
        }
        return new Date(pSource.getTime() - TZ_DEFAULT_RAW_OFFSET + TZ_ASIA_TOKYO_RAW_OFFSET);
    }

}
