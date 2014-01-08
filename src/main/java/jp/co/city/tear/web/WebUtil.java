/**
 * 
 */
package jp.co.city.tear.web;

import jp.co.city.tear.Environment;

/**
 * @author jabaraster
 */
public final class WebUtil {

    private WebUtil() {
        // 処理なし
    }

    /**
     * @param pArContentId -
     * @return -
     */
    public static String buildContentAbsoluteUrl(final long pArContentId) {
        return Environment.getAbsoluteRestUrlRoot() + "arContent/" + pArContentId + "/content"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
