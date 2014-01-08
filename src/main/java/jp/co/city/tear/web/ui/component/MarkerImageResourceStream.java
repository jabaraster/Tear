package jp.co.city.tear.web.ui.component;

import jabara.general.IoUtil;
import jabara.general.NotFound;

import java.io.InputStream;

import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.service.IArContentService;

import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * @author jabaraster
 */
public class MarkerImageResourceStream extends AbstractResourceStream {
    private static final long       serialVersionUID = -7458902723032820185L;

    private final IArContentService arContentService;
    private final EArContent        arContent;

    private InputStream             stream;

    /**
     * @param pArContentService -
     * @param pArContent -
     */
    public MarkerImageResourceStream(final IArContentService pArContentService, final EArContent pArContent) {
        this.arContentService = pArContentService;
        this.arContent = pArContent;
    }

    /**
     * @see org.apache.wicket.util.resource.IResourceStream#close()
     */
    @Override
    public void close() {
        IoUtil.close(this.stream);
        this.stream = null;
    }

    /**
     * @see org.apache.wicket.util.resource.IResourceStream#getInputStream()
     */
    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        if (this.stream != null) {
            return this.stream;
        }
        try {
            this.stream = this.arContentService.getDataInputStream(this.arContent.getMarker());
            return this.stream;
        } catch (final NotFound e) {
            throw new ResourceStreamNotFoundException();
        }
    }

}
