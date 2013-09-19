/**
 * 
 */
package jp.co.city.tear.web;

import jabara.servlet.ServletUtil;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jabaraster
 */
public class MultipartFilter implements Filter {

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
        core((HttpServletRequest) pRequest);
        pChain.doFilter(pRequest, pResponse);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(@SuppressWarnings("unused") final FilterConfig pFilterConfig) {
        // 処理なし
    }

    private static void core(final HttpServletRequest pRequest) {
        if (ServletUtil.isMultipartRequest(pRequest)) {
            // s(pRequest);
        }
    }

    private static void s(final HttpServletRequest pRequest) {
        try (FileOutputStream fileOut = new FileOutputStream("files/mr.txt"); // //$NON-NLS-1$
                BufferedOutputStream out = new BufferedOutputStream(fileOut); //
                final InputStream in = pRequest.getInputStream();) {

            final byte[] buf = new byte[4096];
            for (int d = in.read(buf); d != -1; d = in.read(buf)) {
                out.write(buf, 0, d);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
