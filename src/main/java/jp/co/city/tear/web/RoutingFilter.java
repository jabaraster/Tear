package jp.co.city.tear.web;

import jabara.servlet.ServletUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class RoutingFilter implements Filter {

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // 処理なし
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest pRequest, final ServletResponse pResponse, final FilterChain pChain) throws IOException,
            ServletException {
        try {
            final HttpServletRequest request = (HttpServletRequest) pRequest;
            final HttpServletResponse response = (HttpServletResponse) pResponse;

            routing(request, response);

            pChain.doFilter(pRequest, pResponse);

        } catch (final Stop e) {
            return;
        }
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(@SuppressWarnings("unused") final FilterConfig pFilterConfig) {
        // 処理なし
    }

    private static boolean equalsPath(final HttpServletRequest pRequest, final String pPathWithoutContextPath) {
        return ServletUtil.omitContextPathFromRequestUri(pRequest).equals(pPathWithoutContextPath);
    }

    @SuppressWarnings("synthetic-access")
    private static void redirect(final String pPath, final HttpServletRequest pRequest, final HttpServletResponse pResponse) throws IOException,
            Stop {
        pResponse.sendRedirect(pRequest.getContextPath() + pPath);
        throw Stop.INSTANCE;
    }

    /**
     * セッションIDを含むURLをクライアントのアドレス欄に晒さないようにするため、リダイレクトする.
     */
    private static void redirectIfUrlContainsSessionId(final HttpServletRequest pRequest, final HttpServletResponse pResponse) throws IOException,
            Stop {
        final String sessionId = pRequest.getRequestedSessionId();
        if (sessionId == null) {
            return;
        }

        if (pRequest.getRequestURI().contains(sessionId)) {
            redirect(WebInitializer.PATH_UI, pRequest, pResponse);
        }
    }

    private static void routing(final HttpServletRequest request, final HttpServletResponse response) throws IOException, Stop {

        redirectIfUrlContainsSessionId(request, response);

        if (equalsPath(request, "/")) { //$NON-NLS-1$
            redirect(WebInitializer.PATH_UI, request, response);
        }
    }

    private static class Stop extends Exception {
        private static final long     serialVersionUID = 7136696838961800917L;

        private static final Stop INSTANCE         = new Stop();
    }

}
