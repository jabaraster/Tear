/**
 * 
 */
package jp.co.city.tear.web.rest;

import java.io.InputStream;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.multipart.FormDataParam;

/**
 * @author jabaraster
 */
@Path("arContent")
public class ArContentResource {

    private final HttpSession session;

    /**
     * @param pSession
     */
    @Inject
    public ArContentResource(final HttpSession pSession) {
        this.session = pSession;
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
    ) {
        for (final Enumeration<String> names = this.session.getAttributeNames(); names.hasMoreElements();) {
            final String k = names.nextElement();
            System.out.println("-----------------");
            System.out.println(k);
            System.out.println(this.session.getAttribute(k));
        }
        return Response.status(Status.NOT_ACCEPTABLE).build();
    }
}
