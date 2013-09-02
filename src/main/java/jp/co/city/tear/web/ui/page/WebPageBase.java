package jp.co.city.tear.web.ui.page;

import jabara.general.ArgUtil;
import jabara.wicket.Models;

import java.util.HashMap;
import java.util.Map;

import jp.co.city.tear.Environment;
import jp.co.city.tear.web.ui.AppSession;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.TextTemplateResourceReference;

/**
 *
 */
public abstract class WebPageBase extends WebPage {
    private static final long serialVersionUID = 9011478021815065944L;

    private Label             titleLabel;

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
        this.add(getTitleLabel());
    }

    /**
     * @see org.apache.wicket.Component#getSession()
     */
    @Override
    public AppSession getSession() {
        return (AppSession) super.getSession();
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

    /**
     * @param pResponse 全ての画面に共通して必要なheadタグ内容を出力します.
     */
    public static void renderCommonHead(final IHeaderResponse pResponse) {
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(new CssResourceReference(WebPageBase.class, "bootstrap/css/bootstrap.min.css"))); //$NON-NLS-1$
        pResponse.render(CssHeaderItem.forReference(buildAppCssReference()));
        pResponse.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WebPageBase.class, "bootstrap/js/bootstrap.min.js"))); //$NON-NLS-1$
    }

    private static TextTemplateResourceReference buildAppCssReference() {
        final Map<String, Object> params = new HashMap<>();
        final Request request = RequestCycle.get().getRequest();
        params.put("bodyBackground", request.getContextPath() + request.getFilterPath() + "/back"); //$NON-NLS-1$ //$NON-NLS-2$
        return new TextTemplateResourceReference(WebPageBase.class, "App.css", "text/css", Models.readOnly(params)); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
