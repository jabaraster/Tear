/**
 * 
 */
package jp.co.city.tear.web.rest;

import jabara.general.Sort;
import jabara.jax_rs.velocity.VelocityTemplate;
import jabara.jpa.entity.EntityBase_;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import jp.co.city.tear.service.IUserService;

/**
 * @author jabaraster
 */
@Path("jsonp")
public class JsonpResource {

    private final IUserService userService;

    /**
     * @param pUserService
     */
    @Inject
    public JsonpResource(final IUserService pUserService) {
        this.userService = pUserService;
    }

    /**
     * @param pCallback
     * @return -
     */
    @SuppressWarnings({ "nls" })
    @Path("s")
    @Produces("text/javascript")
    @GET
    public VelocityTemplate s(@QueryParam("callback") final String pCallback) {
        final Map<String, Object> context = new HashMap<>();
        context.put("callback", pCallback);
        context.put("users", this.userService.getAll(Sort.asc(EntityBase_.id.getName())));
        return new VelocityTemplate(getClass(), "a.vm", context);
    }
}
