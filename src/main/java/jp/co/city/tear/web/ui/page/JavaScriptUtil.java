package jp.co.city.tear.web.ui.page;

import jabara.general.ArgUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;

/**
 *
 */
public final class JavaScriptUtil {

    private static final Logger _logger             = Logger.getLogger(JavaScriptUtil.class);

    /**
     * 
     */
    public static final String  COMMON_JS_FILE_PATH = "App.js";                              //$NON-NLS-1$

    private JavaScriptUtil() {
        // 処理なし
    }

    /**
     * @param pTag フォーカスを当てる対象のタグ. <br>
     *            {@link Component#setOutputMarkupId(boolean)}にtrueをセットしていることが前提.
     * @return pTagにフォーカスを当てるJavaScriptコード.
     */
    @SuppressWarnings("nls")
    public static String getFocusScript(final Component pTag) {
        ArgUtil.checkNull(pTag, "pTag"); //$NON-NLS-1$

        if (!pTag.getOutputMarkupId()) {
            _logger.warn(pTag.getId() + "(型：" + pTag.getClass().getName() + ") のoutputMarkupIdプロパティがfalseであるため、"
                    + JavaScriptUtil.class.getSimpleName() + "#getFocusScript()は正常に動作しません.");
        }
        return "App.focus('" + pTag.getMarkupId() + "');"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
