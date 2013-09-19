package jp.co.city.tear.web.rest;

import jabara.jax_rs.JsonMessageBodyReaderWriter;
import jabara.jax_rs.velocity.VelocityMessageBodyWriter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 *
 */
public class RestApplication extends Application {

    /**
     * @see javax.ws.rs.core.Application#getClasses()
     */
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(new Class<?>[] { //
                JsonMessageBodyReaderWriter.class // JSONをきれいに返すにはこのクラスが必要.
                        , VelocityMessageBodyWriter.class //
                        , UserResource.class //
                        , ArContentResource.class //
                }));
    }

    /**
     * @param pArgs 起動引数.
     */
    public static void main(final String[] pArgs) {
        for (final Map.Entry<String, String> entry : System.getenv().entrySet()) {
            System.out.println(entry);
        }
    }
}
