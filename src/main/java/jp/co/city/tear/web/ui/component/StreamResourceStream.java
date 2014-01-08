package jp.co.city.tear.web.ui.component;

import jabara.general.ArgUtil;
import jabara.general.IoUtil;

import java.io.InputStream;

import org.apache.wicket.util.resource.AbstractResourceStream;

/**
 * @author jabaraster
 */
public class StreamResourceStream extends AbstractResourceStream {
    private static final long serialVersionUID = 6106999405854759923L;

    private InputStream       in;
    private final String      contentType;

    /**
     * @param pIn -
     * @param pContentType -
     */
    public StreamResourceStream(final InputStream pIn, final String pContentType) {
        ArgUtil.checkNull(pIn, "pIn"); //$NON-NLS-1$
        ArgUtil.checkNullOrEmpty(pContentType, "pContentType"); //$NON-NLS-1$
        this.in = pIn;
        this.contentType = pContentType;
    }

    /**
     * @see org.apache.wicket.util.resource.IResourceStream#close()
     */
    @Override
    public void close() {
        IoUtil.close(this.in);
        this.in = null;
    }

    /**
     * @see org.apache.wicket.util.resource.AbstractResourceStream#getContentType()
     */
    @Override
    public String getContentType() {
        return this.contentType;
    }

    /**
     * @see org.apache.wicket.util.resource.IResourceStream#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        return this.in;
    }

}