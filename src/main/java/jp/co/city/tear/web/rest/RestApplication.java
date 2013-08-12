package jp.co.city.tear.web.rest;

import jabara.jax_rs.JsonMessageBodyReaderWriter;

import java.util.Arrays;
import java.util.HashSet;
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
                        , UserResource.class //
                        , ArContentResource.class //
                }));
    }
}
