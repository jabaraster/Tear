/**
 * 
 */
package jp.co.city.tear.web.rest;

import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.multipart.FormDataParam;

/**
 * @author jabaraster
 */
@Path("arContent")
public class ArContentResource {

    /**
     * @return -
     */
    @Path("hoge")
    @GET
    public Viewable get() {
        return new Viewable("/plist.jsp", new HashMap<>()); //$NON-NLS-1$
    }

    /**
     * @param pArContentId -
     * @param pIn -
     * @return -
     */
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Path("{id}/content")
    @POST
    public Response postContent( //
            @PathParam("id") final long pArContentId //
            , @FormDataParam("data") final InputStream pIn //
            , @Context final HttpServletRequest pRequest //
    ) {
        return Response.status(Status.NOT_ACCEPTABLE).build();
    }
}
