package jp.co.city.tear.web;

import jabara.servlet.routing.IRouter;
import jabara.servlet.routing.RouterBase;
import jabara.servlet.routing.RoutingFilterBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class RoutingFilter extends RoutingFilterBase {

    /**
     * @see jabara.servlet.routing.RoutingFilterBase#createRouter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected IRouter createRouter(final HttpServletRequest pRequest, final HttpServletResponse pResponse) {
        return new Router(pRequest, pResponse);
    }

    private static class Router extends RouterBase {

        Router(final HttpServletRequest pRequest, final HttpServletResponse pResponse) {
            super(pRequest, pResponse);
        }

        @Override
        protected String getTopPagePath() {
            return WebInitializer.PATH_UI;
        }

        @SuppressWarnings("nls")
        @Override
        protected void routingCore() throws Exception {
            redirectIfMatch("/", WebInitializer.PATH_UI);
        }
    }
}
