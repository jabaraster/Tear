/**
 * 
 */
package jp.co.city.tear.web.rest;

import jabara.general.ArgUtil;
import jabara.general.IoUtil;
import jabara.general.NotFound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;

import jp.co.city.tear.Environment;
import jp.co.city.tear.entity.EArContent;
import jp.co.city.tear.service.IArContentService;
import jp.co.city.tear.service.ILargeDataService;
import jp.co.city.tear.web.rest.trackingData.Connection;
import jp.co.city.tear.web.rest.trackingData.Sensor;
import jp.co.city.tear.web.rest.trackingData.SensorCOS;
import jp.co.city.tear.web.rest.trackingData.TrackingData;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
@Path("arContent")
public class ArContentResource {

    private static final Method[]   METHODS              = ArContentResource.class.getMethods();
    private static final Method     METHOD_LOADING_MOVIE = findMethod("getLoadingMovie", METHODS); //$NON-NLS-1$
    private static final Method     METHOD_TRACKING_DATA = findMethod("getTrackingData", METHODS); //$NON-NLS-1$

    private final IArContentService arContentService;
    private final ILargeDataService largeDataServicde;

    /**
     * @param pArContentService -
     * @param pLargeDataService -
     */
    @Inject
    public ArContentResource( //
            final IArContentService pArContentService //
            , final ILargeDataService pLargeDataService //
    ) {
        this.arContentService = ArgUtil.checkNull(pArContentService, "pArContentService"); //$NON-NLS-1$
        this.largeDataServicde = ArgUtil.checkNull(pLargeDataService, "pLargeDataService"); //$NON-NLS-1$
    }

    /**
     * @return -
     */
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/index")
    @GET
    public List<ArContent> getAllContents() {
        final List<ArContent> ret = new ArrayList<>();
        for (final EArContent c : this.arContentService.getAll()) {
            final ArContent content = new ArContent();
            content.setMarkerUrl(buildMarkerAbsoluteUrl(c));
            content.setContentUrl(buildContentAbsoluteUrl(c));
            ret.add(content);
        }
        return ret;
    }

    /**
     * @param pId -
     * @return -
     */
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{id}/content")
    @GET
    public Response getContentData(@PathParam("id") final long pId) {
        try {
            final EArContent content = this.arContentService.findById(pId);
            return Response.ok(new StreamingOutput() {
                @SuppressWarnings("synthetic-access")
                @Override
                public void write(final OutputStream pOutput) throws IOException, WebApplicationException {
                    try (InputStream in = ArContentResource.this.largeDataServicde.getDataInputStream(content.getContent())) {
                        IOUtils.copy(IoUtil.toBuffered(in), pOutput);
                    } catch (final NotFound e) {
                        throw new WebApplicationException(Status.NOT_FOUND);
                    }
                }
            }).build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @return -
     */
    @SuppressWarnings("static-method")
    @Path("loading.3gp")
    @GET
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public Response getLoadingMovie() {
        final URL url = ArContentResource.class.getResource("/loading.3gp"); //$NON-NLS-1$
        if (url == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(new StreamingOutput() {
            @Override
            public void write(final OutputStream pOutput) throws IOException, WebApplicationException {
                try (InputStream in = url.openStream()) {
                    IOUtils.copy(IoUtil.toBuffered(in), pOutput);
                }
            }
        }).build();
    }

    /**
     * @param pId -
     * @return -
     */
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{id}/marker")
    @GET
    public Response getMarkerData(@PathParam("id") final long pId) {
        try {
            final EArContent content = this.arContentService.findById(pId);
            return Response.ok(new StreamingOutput() {
                @SuppressWarnings("synthetic-access")
                @Override
                public void write(final OutputStream pOutput) throws IOException, WebApplicationException {
                    try (InputStream in = ArContentResource.this.largeDataServicde.getDataInputStream(content.getMarker())) {
                        IOUtils.copy(IoUtil.toBuffered(in), pOutput);
                    } catch (final NotFound e) {
                        throw new WebApplicationException(Status.NOT_FOUND);
                    }
                }
            }).build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @return -
     */
    @Path("trackingData")
    @GET
    @Produces({ MediaType.TEXT_XML })
    public TrackingData getTrackingData() {
        final TrackingData ret = new TrackingData();
        final Sensor sensor = ret.sensors.get(0);
        for (long i = 0, len = this.arContentService.countAll(); i < len; i++) {
            final SensorCOS cos = new SensorCOS();
            cos.sensorCosID = "Patch" + (i + 1); //$NON-NLS-1$
            cos.parameters.referenceImage.name = "marker_" + (i + 1) + ".png"; //$NON-NLS-1$ //$NON-NLS-2$
            sensor.sensorCOS.add(cos);

            final Connection connection = new Connection();
            connection.name = "MarkerlessCOS" + (i + 1); //$NON-NLS-1$
            connection.sensorSource.sensorCosID = cos.sensorCosID;
            ret.connections.add(connection);
        }
        return ret;
    }

    /**
     * @return -
     */
    @Path("urls")
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    public String getUrls() {
        final String absoluteUrlRoot = Environment.getAbsoluteRestUrlRoot();
        final URI trackingDataUri = buildMethodUri(METHOD_TRACKING_DATA);
        final URI loadedMovieUri = buildMethodUri(METHOD_LOADING_MOVIE);

        final String lineSeparator = "\r\n"; //$NON-NLS-1$
        final StringBuilder sb = new StringBuilder();
        sb.append(absoluteUrlRoot).append(trackingDataUri.toASCIIString()).append(lineSeparator);
        sb.append(absoluteUrlRoot).append(loadedMovieUri.toASCIIString()).append(lineSeparator);

        final String separator = ","; //$NON-NLS-1$
        final List<EArContent> contents = this.arContentService.getAll();
        for (int i = 0; i < contents.size(); i++) {
            final EArContent content = contents.get(i);
            sb.append(i + 1) //
                    .append(separator).append(buildMarkerAbsoluteUrl(content)) //
                    .append(separator).append(buildContentAbsoluteUrl(content)) //
                    .append(lineSeparator);
        }

        return new String(sb);
    }

    private static String buildContentAbsoluteUrl(final EArContent pArContent) {
        return Environment.getAbsoluteRestUrlRoot() + "arContent/" + pArContent.getId().longValue() + "/content"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static String buildMarkerAbsoluteUrl(final EArContent pArContent) {
        return Environment.getAbsoluteRestUrlRoot() + "arContent/" + pArContent.getId().longValue() + "/marker"; //$NON-NLS-1$//$NON-NLS-2$
    }

    private static URI buildMethodUri(final Method pMethod) {
        return UriBuilder.fromResource(ArContentResource.class).path(pMethod).build();
    }

    private static Method findMethod(final String pMethodName, final Method[] pMethods) {
        for (final Method method : pMethods) {
            if (method.getName().equals(pMethodName)) {
                return method;
            }
        }
        throw new IllegalStateException();
    }

    /**
     * @author jabaraster
     */
    public static class ArContent {
        private String markerUrl;
        private String contentUrl;

        /**
         * @return the contentUrl
         */
        public String getContentUrl() {
            return this.contentUrl;
        }

        /**
         * @return the markerUrl
         */
        public String getMarkerUrl() {
            return this.markerUrl;
        }

        /**
         * @param pContentUrl the contentUrl to set
         */
        public void setContentUrl(final String pContentUrl) {
            this.contentUrl = pContentUrl;
        }

        /**
         * @param pMarkerUrl the markerUrl to set
         */
        public void setMarkerUrl(final String pMarkerUrl) {
            this.markerUrl = pMarkerUrl;
        }
    }
}
