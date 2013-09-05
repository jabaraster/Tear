package jp.co.city.tear.web.ui.page;

import jp.co.city.tear.web.ui.AppSession;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;

/**
 * 
 */
public class LogoutPage extends WebPage {
    private static final long serialVersionUID = -3810270407936165942L;

    /**
     * 
     */
    public LogoutPage() {
        AppSession.get().invalidateNow();
        throw new RestartResponseException(LoginPage.class);
    }
}
