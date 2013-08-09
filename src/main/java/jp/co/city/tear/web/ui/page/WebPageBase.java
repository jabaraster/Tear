package jp.co.city.tear.web.ui.page;

import jabara.general.ArgUtil;
import jabara.wicket.Models;
import jp.co.city.tear.Environment;

import org.apache.wicket.Page;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 */
public abstract class WebPageBase extends WebPage {
    private static final long serialVersionUID = 9011478021815065944L;

    private Label             titleLabel;
    private Label             applicationNameInHeader;
    private Panel             headerPanel;

    /**
     * 
     */
    protected WebPageBase() {
        this(new PageParameters());
    }

    /**
     * @param pParameters -
     */
    protected WebPageBase(final PageParameters pParameters) {
        super(pParameters);
        this.add(getApplicationNameInHeader());
        this.add(getTitleLabel());
        this.add(getHeaderPanel());
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        renderCommonHead(pResponse);
    }

    /**
     * @param pId -
     * @return -
     */
    protected abstract Panel createHeaderPanel(String pId);

    /**
     * headerタグ内のアプリケーション名を表示するラベルです. <br>
     * このメソッドはサブクラスでコンポーネントIDの重複を避けるためにprotectedにしています. <br>
     * 
     * @return headerタグ内のアプリケーション名を表示するラベル.
     */
    protected Label getApplicationNameInHeader() {
        if (this.applicationNameInHeader == null) {
            this.applicationNameInHeader = new Label("applicationNameInHeader", Models.of(Environment.getApplicationName())); //$NON-NLS-1$
        }
        return this.applicationNameInHeader;
    }

    /**
     * titleタグの中を表示するラベルです. <br>
     * このメソッドはサブクラスでコンポーネントIDの重複を避けるためにprotectedにしています. <br>
     * 
     * @return titleタグの中を表示するラベル.
     */
    @SuppressWarnings({ "nls" })
    protected Label getTitleLabel() {
        if (this.titleLabel == null) {
            this.titleLabel = new Label("titleLabel", Models.of(getTitleLabelModel().getObject() + " - " + Environment.getApplicationName()));
        }
        return this.titleLabel;
    }

    /**
     * @return HTMLのtitleタグの内容
     */
    protected abstract IModel<String> getTitleLabelModel();

    private Panel getHeaderPanel() {
        if (this.headerPanel == null) {
            this.headerPanel = createHeaderPanel("headerPanel"); //$NON-NLS-1$
        }
        return this.headerPanel;
    }

    /**
     * @param pResponse -
     */
    public static void addJQueryJavaSriptReference(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WebPageBase.class, "jquery-1.8.3.min.js"))); //$NON-NLS-1$
    }

    /**
     * @param pResponse 書き込み用レスポンス.
     * @param pPageType CSSファイルの基準となるページクラス.
     */
    public static void addPageCssReference(final IHeaderResponse pResponse, final Class<? extends Page> pPageType) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pPageType, "pPageType"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(pPageType, pPageType.getSimpleName() + ".css"))); //$NON-NLS-1$
    }

    /**
     * @param pResponse 書き込み用レスポンス.
     * @param pPageType jsファイルの基準となるページクラス.
     */
    public static void addPageJavaScriptReference(final IHeaderResponse pResponse, final Class<? extends Page> pPageType) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        ArgUtil.checkNull(pPageType, "pPageType"); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(pPageType, pPageType.getSimpleName() + ".js"))); //$NON-NLS-1$
    }

    /**
     * @param pResponse 全ての画面に共通して必要なheadタグ内容を出力します.
     */
    public static void renderCommonHead(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(WebPageBase.class, "bootstrap/css/bootstrap.min.css"))); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(WebPageBase.class, "App.css"))); //$NON-NLS-1$
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WebPageBase.class, JavaScriptUtil.COMMON_JS_FILE_PATH)));
    }

}
