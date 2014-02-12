/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jabara.wicket.ComponentCssHeaderItem;
import jabara.wicket.ComponentJavaScriptHeaderItem;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.service.IArContentPlayLogService;
import jp.co.city.tear.service.IArContentService;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class LogRegisterPage extends RestrictedPageBase {
    private static final long  serialVersionUID = 2085927010402176089L;

    private final Handler      handler          = new Handler();

    @Inject
    IArContentService          arContentService;

    @Inject
    IArContentPlayLogService   arContentPlayLogService;

    private final List<String> descriptorsValue;
    private final List<Long>   arContentIdsValue;

    private Form<?>            form;
    private ListView<String>   descriptors;

    /**
     * 
     */
    public LogRegisterPage() {
        this.descriptorsValue = createDescriptors(5);
        this.arContentIdsValue = conv(this.arContentService.getAll());
        this.add(getForm());
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        pResponse.render(ComponentCssHeaderItem.forType(LogRegisterPage.class));
        pResponse.render(ComponentJavaScriptHeaderItem.forType(LogRegisterPage.class));
    }

    /**
     * @see jp.co.city.tear.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("AR再生ログの登録"); //$NON-NLS-1$
    }

    @SuppressWarnings("nls")
    private ListView<String> getDescriptors() {
        if (this.descriptors == null) {
            this.descriptors = new ListView<String>("descriptors", this.descriptorsValue) {
                @Override
                protected void populateItem(final ListItem<String> pDescriptorItem) {
                    final String descriptor = pDescriptorItem.getModelObject();
                    pDescriptorItem.add(new Label("descriptor", descriptor));

                    final ListView<Long> arContentIds = new ListView<Long>("arContentIds", LogRegisterPage.this.arContentIdsValue) {
                        @Override
                        protected void populateItem(final ListItem<Long> pIdItem) {
                            final IndicatingAjaxButton registerer = new IndicatingAjaxButton("registerer") {
                                @Override
                                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                                    LogRegisterPage.this.handler.registerLog(pTarget, descriptor, pIdItem.getModelObject().longValue());
                                }
                            };
                            registerer.add(new Label("id", pIdItem.getModelObject()));
                            pIdItem.add(registerer);
                        }
                    };
                    pDescriptorItem.add(arContentIds);
                }
            };
        }
        return this.descriptors;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getDescriptors());
        }
        return this.form;
    }

    private static List<Long> conv(final List<EArContent> pList) {
        final List<Long> ret = new ArrayList<>();
        for (final EArContent c : pList) {
            ret.add(c.getId());
        }
        return ret;
    }

    private static List<String> createDescriptors(final int pCount) {
        final List<String> ret = new ArrayList<>();
        for (int i = 0; i < pCount; i++) {
            ret.add(UUID.randomUUID().toString());
        }
        return ret;
    }

    private class Handler implements Serializable {

        @SuppressWarnings("nls")
        void registerLog(final AjaxRequestTarget pTarget, final String pDescriptor, final long pArContentId) {
            final ArContentPlayLog log = new ArContentPlayLog();
            log.setArContentId(pArContentId);
            log.setLatitude(null);
            log.setLongitude(null);
            log.setPlayDatetime(Calendar.getInstance().getTime());
            log.setTrackingDescriptor(pDescriptor);
            LogRegisterPage.this.arContentPlayLogService.insert(log);
            pTarget.appendJavaScript("appendLog('" + pDescriptor + "'," + pArContentId + ")");
        }

    }
}
