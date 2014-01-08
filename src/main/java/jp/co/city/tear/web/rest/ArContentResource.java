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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import jp.co.city.tear.web.WebUtil;
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

    private static final Method[]   METHODS                    = ArContentResource.class.getMethods();
    private static final Method     METHOD_LOADING_MOVIE       = findMethod("getLoadingMovie", METHODS); //$NON-NLS-1$
    private static final Method     METHOD_TRACKING_DATA       = findMethod("getTrackingData", METHODS); //$NON-NLS-1$

    private static final String     HEADER_CONTENT_DISPOSITION = "Content-Disposition";                 //$NON-NLS-1$
    private static final String     HEADER_CONTENT_TYPE        = "Content-Type";                        //$NON-NLS-1$

    private static final String     LOADING_MOVIE_NAME         = "loading.3gp";                         //$NON-NLS-1$

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
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("/index")
    @GET
    public List<ArContent> getAllContents() {
        final List<ArContent> ret = new ArrayList<>();
        for (final EArContent c : this.arContentService.getAll()) {
            final ArContent content = new ArContent(c);
            ret.add(content);
        }
        return ret;
    }

    /**
     * @param pId -
     * @param pDisposition -
     * @return -
     */
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{id}/content")
    @GET
    public Response getContentData(@PathParam("id") final long pId, @QueryParam("disposition") final String pDisposition) {
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
            }) //
                    .header(HEADER_CONTENT_TYPE, content.getContent().getContentType()) //
                    .header(HEADER_CONTENT_DISPOSITION, buildContentDisposition(pDisposition //
                            , content.getId().longValue() + "." + content.getContent().getType())) // //$NON-NLS-1$
                    .build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @param pDisposition -
     * @return -
     */
    @SuppressWarnings("static-method")
    @Path(LOADING_MOVIE_NAME)
    @GET
    @Produces({ MediaType.APPLICATION_OCTET_STREAM })
    public Response getLoadingMovie(@QueryParam("disposition") final String pDisposition) {
        final URL url = ArContentResource.class.getResource("/" + LOADING_MOVIE_NAME); //$NON-NLS-1$
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
        }) //
                .header(HEADER_CONTENT_DISPOSITION, buildContentDisposition(pDisposition, LOADING_MOVIE_NAME)) //
                .build();
    }

    /**
     * @param pId -
     * @param pDisposition -
     * @return -
     */
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("{id}/marker")
    @GET
    public Response getMarkerData(@PathParam("id") final long pId, @QueryParam("disposition") final String pDisposition) {
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
            }) //
                    .header(HEADER_CONTENT_TYPE, content.getContent().getContentType()) //
                    .header(HEADER_CONTENT_DISPOSITION, buildContentDisposition(pDisposition //
                            , content.getId().longValue() + "." + content.getMarker().getType())) // //$NON-NLS-1$
                    .build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @param pDisposition -
     * @return -
     */
    @Path("trackingData")
    @GET
    @Produces({ MediaType.TEXT_XML })
    public Response getTrackingData(@QueryParam("disposition") final String pDisposition) {
        final TrackingData ret = new TrackingData();
        final Sensor sensor = ret.sensors.get(0);

        final List<EArContent> contents = this.arContentService.getAll();
        for (int i = 0; i < contents.size(); i++) {
            final EArContent arContent = contents.get(i);

            final SensorCOS cos = new SensorCOS();
            cos.sensorCosID = "Patch" + (i + 1); //$NON-NLS-1$
            cos.parameters.referenceImage.name = "marker_" + arContent.getId().longValue() + "." + arContent.getMarker().getType(); //$NON-NLS-1$//$NON-NLS-2$
            cos.parameters.referenceImage.widthMM = 80;
            cos.parameters.referenceImage.heightMM = 80;
            cos.parameters.similarityThreshold = arContent.getSimilarityThreshold();
            sensor.sensorCOS.add(cos);

            final Connection connection = new Connection();
            connection.name = "MarkerlessCOS" + (i + 1); //$NON-NLS-1$
            connection.sensorSource.sensorCosID = cos.sensorCosID;
            ret.connections.add(connection);
        }
        return Response.ok(ret) //
                .header(HEADER_CONTENT_DISPOSITION, buildContentDisposition(pDisposition, "TrackingData.xml")) // //$NON-NLS-1$
                .build();
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

        final DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss"); //$NON-NLS-1$
        final String separator = ","; //$NON-NLS-1$
        for (final EArContent content : this.arContentService.getAll()) {
            sb.append(content.getId().longValue()) //
                    .append(separator).append(fmt.format(content.getUpdated())) //
                    .append(separator).append(buildMarkerAbsoluteUrl(content)) //
                    .append(separator).append(WebUtil.buildContentAbsoluteUrl(content.getId().longValue())) //
                    .append(lineSeparator);
        }

        return new String(sb);
    }

    private static String buildContentDisposition(final String pDisposition, final String pFileName) {
        final String disposition = "inline".equals(pDisposition) ? "inline" : "attachment"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        return disposition + "; filename=\"" + pFileName + "\""; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static String buildMarkerAbsoluteUrl(final EArContent pArContent) {
        final URI uri = UriBuilder.fromResource(ArContentResource.class).path(String.valueOf(pArContent.getId().longValue())).path("marker").build(); //$NON-NLS-1$
        return Environment.getAbsoluteRestUrlRoot() + uri.toASCIIString();
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
        private final String title;
        private final String markerUrl;
        private final String contentUrl;
        private final String markerFileName;
        private final String contentFileName;

        /**
         * @param pContent -
         */
        @SuppressWarnings("synthetic-access")
        public ArContent(final EArContent pContent) {
            ArgUtil.checkNull(pContent, "pContent"); //$NON-NLS-1$
            this.contentFileName = pContent.getContent().getDataName();
            this.contentUrl = WebUtil.buildContentAbsoluteUrl(pContent.getId().longValue());
            this.markerFileName = pContent.getMarker().getDataName();
            this.markerUrl = buildMarkerAbsoluteUrl(pContent);
            this.title = pContent.getTitle();
        }

        /**
         * @return contentFileNameを返す.
         */
        public String getContentFileName() {
            return this.contentFileName;
        }

        /**
         * @return contentUrlを返す.
         */
        public String getContentUrl() {
            return this.contentUrl;
        }

        /**
         * @return markerFileNameを返す.
         */
        public String getMarkerFileName() {
            return this.markerFileName;
        }

        /**
         * @return markerUrlを返す.
         */
        public String getMarkerUrl() {
            return this.markerUrl;
        }

        /**
         * @return titleを返す.
         */
        public String getTitle() {
            return this.title;
        }
    }
}
