/**
 * 
 */
package jp.co.city.tear.service.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import jabara.general.ExceptionUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import jp.co.city.tear.WebStarter;
import jp.co.city.tear.WebStarter.Mode;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.service.IArContentPlayLogService.FindCondition;
import jp.co.city.tear.service.impl.ArContentPlayLogServiceImpl.PlayLogWithFlag;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

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
    public JpaDaoRule<ArContentPlayLogServiceImpl> tester = new JpaDaoRule<ArContentPlayLogServiceImpl>() {
                                                              @Override
                                                              protected ArContentPlayLogServiceImpl createService(
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
    @SuppressWarnings("boxing")
    @Test
    public void _find_最後のデータが無効() {
        // 前準備
        final List<ArContentPlayLog> logs = loadLogsFromJson("makeCsv_test_02.json"); //$NON-NLS-1$
        for (final ArContentPlayLog log : logs) {
            this.tester.getSut().insert(log);
        }

        // テスト本体
        final List<PlayLogWithFlag> l = this.tester.getSut().find(3, new FindCondition(null, null));
        assertThat(l.size(), is(logs.size()));
        assertThat(l.get(0).isTruePlay(), is(false));
        assertThat(l.get(1).isTruePlay(), is(true));
        assertThat(l.get(2).isTruePlay(), is(true));
        assertThat(l.get(3).isTruePlay(), is(true));
        assertThat(l.get(4).isTruePlay(), is(false));
        assertThat(l.get(5).isTruePlay(), is(true));
    }

    /**
     * 
     */
    @SuppressWarnings("boxing")
    @Test
    public void _find_最後のデータが有効() {
        // 前準備
        final List<ArContentPlayLog> logs = loadLogsFromJson("makeCsv_test_01.json"); //$NON-NLS-1$
        for (final ArContentPlayLog log : logs) {
            this.tester.getSut().insert(log);
        }

        // テスト本体
        final List<PlayLogWithFlag> l = this.tester.getSut().find(3, new FindCondition(null, null));
        assertThat(l.size(), is(logs.size()));
        assertThat(l.get(0).isTruePlay(), is(false));
        assertThat(l.get(1).isTruePlay(), is(true));
        assertThat(l.get(2).isTruePlay(), is(true));
        assertThat(l.get(3).isTruePlay(), is(false));
        assertThat(l.get(4).isTruePlay(), is(true));
        assertThat(l.get(5).isTruePlay(), is(true));
        assertThat(l.get(6).isTruePlay(), is(true));
    }

    /**
     * 
     */
    @BeforeClass
    public static void beforeClass() {
        WebStarter.initializeDataSource(Mode.UNIT_TEST);
    }

    private static List<ArContentPlayLog> loadLogsFromJson(final String pFileName) {
        try {
            final ArContentPlayLog[] ary = JSON
                    .decode(ArContentPlayLogServiceImplTest.class.getResourceAsStream(pFileName), ArContentPlayLog[].class);
            return Arrays.asList(ary);
        } catch (final JSONException | IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

}
