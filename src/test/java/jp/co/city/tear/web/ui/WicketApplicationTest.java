package jp.co.city.tear.web.ui;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.WebStarter.Mode;
import jp.co.city.tear.web.ui.WicketApplication.Resource;
import jp.co.city.tear.web.ui.page.LoginPage;
import jp.co.city.tear.web.ui.page.WicketRule;

import org.apache.wicket.request.resource.ResourceReference;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class WicketApplicationTest {

    /**
     * 
     */
    @Rule
    public WicketRule tester = WicketRule.newInstance(LoginPage.class);

    /**
     * 
     */
    @Test
    public void _test() {
        final ResourceReference ref = this.tester.getApplication().getSharedResourceReference(Resource.FAVICON);
        System.out.println(ref);
    }

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }
}
