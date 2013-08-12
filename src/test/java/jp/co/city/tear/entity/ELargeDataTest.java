/**
 * 
 */
package jp.co.city.tear.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author jabaraster
 */
public class ELargeDataTest {

    /**
     * Test method for {@link jp.co.city.tear.entity.ELargeData#clearData()}.
     */
    @SuppressWarnings({ "boxing", "static-method" })
    @Test
    public void _clearData() {
        final int LENGTH = 10;
        final ELargeData d = new ELargeData();
        d.setDataLength(LENGTH);

        assertThat(true, is(d.hasData()));
        assertThat(LENGTH, is(d.getLength()));

        d.clearData();

        assertThat(false, is(d.hasData()));
        assertThat(null, is(d.getLength()));
    }

    /**
     * Test method for {@link jp.co.city.tear.entity.ELargeData#setDataLength(int)}.
     */
    @SuppressWarnings({ "boxing", "static-method" })
    @Test
    public void _setDataLength_負数をセット() {
        final int LENGTH = -1;
        final ELargeData d = new ELargeData();
        d.setDataLength(LENGTH);

        assertThat(true, is(d.hasData()));
        assertThat(LENGTH, is(d.getLength()));
    }
}
