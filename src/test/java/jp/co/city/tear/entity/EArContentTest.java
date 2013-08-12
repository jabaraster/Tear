/**
 * 
 */
package jp.co.city.tear.entity;

import java.io.InputStream;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import static org.hamcrest.core.Is.is;

/**
 * @author jabaraster
 */
public class EArContentTest {

    /**
     * 
     */
    @SuppressWarnings({ "boxing", "static-method" })
    @Test
    public void _hasMarker() {
        final EArContent c = new EArContent();
        assertThat(false, is(c.hasMarker()));
        assertThat(false, is(c.hasContent()));

        final ELargeData d = new ELargeData();
        c.setMarker(d);
        c.setContent(d);
        assertThat(false, is(c.hasMarker()));
        assertThat(false, is(c.hasContent()));

        final InputStream in = EArContentTest.class.getResourceAsStream(EArContentTest.class.getSimpleName() + ".class"); //$NON-NLS-1$
        if (in == null) {
            fail();
        }
        d.setData(in);

        assertThat(true, is(c.hasMarker()));
        assertThat(true, is(c.hasContent()));
    }

}
