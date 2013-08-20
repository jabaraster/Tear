package jabara.servlet;

import jabara.general.ArgUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jabaraster
 */
public abstract class RouterBase implements IRouter {
    /**
     * 
     */
    protected final HttpServletRequest  request;

    /**
     * 
     */
    protected final HttpServletResponse response;

    /**
     * @param pRequest -
     * @param pResponse -
     */
    public RouterBase(final HttpServletRequest pRequest, final HttpServletResponse pResponse) {
        ArgUtil.checkNull(pRequest, "pRequest"); //$NON-NLS-1$
        ArgUtil.checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        this.request = pRequest;
        this.response = pResponse;
    }

    /**
     * @throws Exception -
     */
    @Override
    public void routing() throws Exception {
        redirectIfUrlContainsSessionId(getTopPagePath());
        routingCore();
    }

    /**
     * @param pPathWithoutContextPath -
     * @return -
     */
    protected boolean equalsPath(final String pPathWithoutContextPath) {
        return ServletUtil.omitContextPathFromRequestUri(this.request).equals(pPathWithoutContextPath);
    }

    /**
     * @param pRequestPath -
     * @param pRedirectPath -
     * @throws Stop -
     * @throws IOException -
     * @throws ServletException -
     */
    protected void forwardIfMatch(final String pRequestPath, final String pRedirectPath) throws Stop, IOException, ServletException {
        if (equalsPath(pRequestPath)) {
            forward(pRedirectPath, this.request, this.response);
        }
    }

    /**
     * {@link #redirectIfUrlContainsSessionId(String)}にてリダイレクトする先のURL.
     * 
     * @return -
     */
    protected abstract String getTopPagePath();

    /**
     * @param pRequestPath -
     * @param pRedirectPath -
     * @throws Stop -
     * @throws IOException -
     */
    protected void redirectIfMatch(final String pRequestPath, final String pRedirectPath) throws Stop, IOException {
        if (equalsPath(pRequestPath)) {
            redirect(pRedirectPath, this.request, this.response);
        }
    }

    /**
     * セッションIDを含むURLをクライアントのアドレス欄に晒さないようにするため、リダイレクトする.
     * 
     * @param pRedirectPath -
     * @throws IOException -
     * @throws Stop -
     */
    protected void redirectIfUrlContainsSessionId(final String pRedirectPath) throws IOException, Stop {
        final String sessionId = this.request.getRequestedSessionId();
        if (sessionId == null) {
            return;
        }

        if (this.request.getRequestURI().contains(sessionId)) {
            redirect(pRedirectPath, this.request, this.response);
        }
    }

    /**
     * @throws Exception -
     */
    protected abstract void routingCore() throws Exception;

    private static void forward( //
            final String pPath //
            , final HttpServletRequest pRequest //
            , final HttpServletResponse pResponse //
    ) throws IOException, Stop, ServletException {
        pRequest.getRequestDispatcher(pPath).forward(pRequest, pResponse);
        throw Stop.INSTANCE;
    }

    private static void redirect( //
            final String pPath //
            , final HttpServletRequest pRequest //
            , final HttpServletResponse pResponse //
    ) throws IOException, Stop {
        pResponse.sendRedirect(pRequest.getContextPath() + pPath);
        throw Stop.INSTANCE;
    }
}