/**
 * 
 */
package jp.co.city.tear.entity;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

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
        final long LENGTH = 10;
        final ELargeData d = new ELargeData();
        d.setDataLength(LENGTH);

        assertThat(true, is(d.hasData()));
        assertThat(LENGTH, is(d.getLength()));

        d.clearData();

        assertThat(false, is(d.hasData()));
        assertThat(null, is(d.getLength()));
    }

    /**
     * 
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public void _getLastToken() {
        assertThat(create("hoge.png").getType(), is("png"));
        assertThat(create(".png").getType(), is("png"));
        assertThat(create("png").getType(), is(""));
        assertThat(create("png.").getType(), is(""));
    }

    /**
     * Test method for {@link jp.co.city.tear.entity.ELargeData#setDataLength(long)}.
     */
    @SuppressWarnings({ "boxing", "static-method" })
    @Test
    public void _setDataLength_負数をセット() {
        final long LENGTH = -1;
        final ELargeData d = new ELargeData();
        d.setDataLength(LENGTH);

        assertThat(true, is(d.hasData()));
        assertThat(LENGTH, is(d.getLength()));
    }

    private static ELargeData create(final String pName) {
        final ELargeData ret = new ELargeData();
        ret.setDataName(pName);
        return ret;
    }
}
