/**
 * 
 */
package jp.co.city.tear.web.rest;

import jabara.general.ArgUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import jp.co.city.tear.entity.EArContentPlayLog;
import jp.co.city.tear.model.ArContentPlayLog;
import jp.co.city.tear.model.LoginUser;
import jp.co.city.tear.service.IArContentPlayLogService;
import jp.co.city.tear.web.LoginUserHolder;

/**
 * @author jabaraster
 */
@Path("log")
public class ArContentPlayLogResource {

    /**
     * 
     */
    public static final int                MAX_RESULT_COUNT    = 500;
    /**
     * 
     */
    public static final String             DEFAULT_VALUE_FIRST = "0";  //$NON-NLS-1$
    /**
     * 
     */
    public static final String             DEFAULT_VALUE_COUNT = "200"; //$NON-NLS-1$

    private final IArContentPlayLogService arContentPlayLogService;

    /**
     * @param pArContentPlayLogService -
     */
    @Inject
    public ArContentPlayLogResource(final IArContentPlayLogService pArContentPlayLogService) {
        ArgUtil.checkNull(pArContentPlayLogService, "pArContentPlayLogService"); //$NON-NLS-1$
        this.arContentPlayLogService = pArContentPlayLogService;
    }

    /**
     * @return -
     */
    @Produces({ MediaType.TEXT_PLAIN })
    @GET
    @Path("descriptor/new")
    public String crateDescriptor() {
        return this.arContentPlayLogService.createDescriptor();
    }

    /**
     * @param pFirst -
     * @param pCount -
     * @param pRequest -
     * @return -
     */
    @Produces({ MediaType.APPLICATION_JSON })
    @GET
    @Path("index")
    public List<ArContentPlayLog> getAll( //
            @QueryParam("first") @DefaultValue(DEFAULT_VALUE_FIRST) final int pFirst //
            , @QueryParam("count") @DefaultValue(DEFAULT_VALUE_COUNT) final int pCount //
            , @Context final HttpServletRequest pRequest) {

        final HttpSession session = pRequest.getSession();
        if (!LoginUserHolder.isLoggedin(session)) {
            throw new WebApplicationException(Status.NOT_ACCEPTABLE);
        }
        final LoginUser loginUser = LoginUserHolder.get(session);
        if (!loginUser.isAdministrator()) {
            throw new WebApplicationException(Status.NOT_ACCEPTABLE);
        }
        if (pCount > MAX_RESULT_COUNT) {
            throw new WebApplicationException(Status.NOT_ACCEPTABLE);
        }

        final List<ArContentPlayLog> ret = new ArrayList<>();
        for (final EArContentPlayLog log : this.arContentPlayLogService.get(pFirst, pCount)) {
            final ArContentPlayLog l = new ArContentPlayLog();
            l.setArContentId(log.getArContentId().longValue());
            l.setLatitude(log.getLatitude());
            l.setLongitude(log.getLongitude());
            l.setPlayDatetime(log.getPlayDatetime());
            l.setTrackingDescriptor(log.getTrackingDescriptor());
            ret.add(l);
        }
        return ret;
    }

    /**
     * @param pLog -
     */
    @Consumes({ MediaType.APPLICATION_JSON })
    @POST
    @Path("new")
    public void postLog(final ArContentPlayLog pLog) {
        ArgUtil.checkNull(pLog, "pLog"); //$NON-NLS-1$
        try {
            this.arContentPlayLogService.insert(pLog);
        } catch (final Exception e) {
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }
}
