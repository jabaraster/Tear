/**
 * 
 */
package jp.co.city.tear.web.ui.page;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.WebStarter.Mode;

import org.apache.wicket.util.tester.FormTester;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class ArContentEditPageTest {

    /**
     * 
     */
    @Rule
    public WicketRule tester = WicketRule.loggedin(ArContentEditPage.class);

    /**
     * 
     */
    @Test
    public void _test() {
        this.tester.assertRenderedPage(ArContentEditPage.class);

        final ArContentEditPage page = (ArContentEditPage) this.tester.getLastRenderedPage();
        final FormTester formTester = this.tester.newFormTester(page.getForm().getId());
        formTester.submit(page.getSubmitter());

        this.tester.assertRenderedPage(ArContentEditPage.class);
        this.tester.assertErrorMessages("タイトルは必須入力です."); //$NON-NLS-1$
    }

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

}
