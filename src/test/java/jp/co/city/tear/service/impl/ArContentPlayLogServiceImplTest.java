/**
 * 
 */
package jp.co.city.tear.service.impl;

import javax.persistence.EntityManagerFactory;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.WebStarter.Mode;
import jp.co.city.tear.service.IArContentPlayLogService;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class ArContentPlayLogServiceImplTest {

    /**
     * 
     */
    @Rule
    public JpaDaoRule<IArContentPlayLogService> tester = new JpaDaoRule<IArContentPlayLogService>() {
                                                           @Override
                                                           protected IArContentPlayLogService createService(
                                                                   final EntityManagerFactory pEntityManagerFactory) {
                                                               return new ArContentPlayLogServiceImpl(pEntityManagerFactory);
                                                           }
                                                       };

    /**
     * 
     */
    @Test
    public void _createDescriptor() {
        jabara.Debug.write(this.tester.getSut().createDescriptor());
    }

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

}
