/**
 * 
 */
package jp.co.city.tear.web.rest;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.multipart.FormDataParam;

/**
 * @author jabaraster
 */
@Path("arContent")
public class ArContentResource {

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
