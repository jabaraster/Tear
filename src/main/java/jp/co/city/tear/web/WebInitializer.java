package jp.co.city.tear.web;

import jabara.jpa.util.SystemPropertyToPostgreJpaPropertiesParser;
import jabara.jpa_guice.SinglePersistenceUnitJpaModule;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import jp.co.city.tear.Environment;
import jp.co.city.tear.service.IDataStore;
import jp.co.city.tear.service.IUserService;
import jp.co.city.tear.service.impl.FileDataStore;
import jp.co.city.tear.service.impl.LobDataStore;
import jp.co.city.tear.service.impl.S3DataStore;
import jp.co.city.tear.web.rest.RestApplication;
import jp.co.city.tear.web.ui.WicketApplication;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.util.IProvider;
import org.eclipse.jetty.servlets.GzipFilter;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * 
 */
@WebListener
public class WebInitializer extends GuiceServletContextListener {
    /**
     * 
     */
    public static final String PATH_UI   = "/ui/";  //$NON-NLS-1$

    /**
     * 
     */
    public static final String PATH_REST = "/rest/"; //$NON-NLS-1$
    /**
     * 
     */
    public static final String PATH_ROOT = "/";     //$NON-NLS-1$
    /**
     * 
     */
    public static final char   WILD_CARD = '*';

    private Injector           injector;

    /**
     * @see com.google.inject.servlet.GuiceServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent pServletContextEvent) {
        super.contextInitialized(pServletContextEvent);

        final ServletContext servletContext = pServletContextEvent.getServletContext();

        addFilter(servletContext, GuiceFilter.class) //
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, PATH_ROOT + WILD_CARD);

        addGzipFilter(servletContext);

        addFilter(servletContext, RoutingFilter.class) //
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, PATH_ROOT + WILD_CARD);

        // addFilter(servletContext, MultipartFilter.class) //
        // .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, PATH_ROOT + WILD_CARD);
        // addFilter(servletContext, RequestDumpFilter.class) //
        // .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, PATH_ROOT + WILD_CARD);
        // addFilter(servletContext, ResponseDumpFilter.class) //
        // .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, PATH_ROOT + WILD_CARD);
    }

    /**
     * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
     */
    @Override
    protected Injector getInjector() {
        if (this.injector == null) {
            this.injector = createInjector();
            initializeDatabase();
        }
        return this.injector;
    }

    private Injector createInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @SuppressWarnings("synthetic-access")
            @Override
            protected void configureServlets() {
                install(new SinglePersistenceUnitJpaModule( //
                        getPersistenceUnitName() //
                        , new SystemPropertyToPostgreJpaPropertiesParser() //
                ));
                initializeJersey();
                initializeWicket();
                initializeServices();
            }

            private void initializeJersey() {
                final Map<String, String> params = new HashMap<>();
                params.put(ServletContainer.APPLICATION_CONFIG_CLASS, RestApplication.class.getName());
                params.put("com.sun.jersey.config.property.JSPTemplatesBasePath", "/WEB-INF/jsp"); //$NON-NLS-1$//$NON-NLS-2$
                serve(PATH_REST + WILD_CARD).with(GuiceContainer.class, params);
            }

            @SuppressWarnings("synthetic-access")
            private void initializeServices() {
                this.bind(IDataStore.class).toProvider(new DataStoreProvider());
            }

            private void initializeWicket() {
                final String path = PATH_UI + WILD_CARD;
                final Map<String, String> params = new HashMap<>();
                params.put(WicketFilter.FILTER_MAPPING_PARAM, path);

                // 一般的には"applicationClassName"というキーに対してアプリケーションクラス名を登録するのですが
                // "applicationClassName"という値を持つ定数が、どうもWicketからは提供されていないようです.
                // しかしWicketFilter.APP_FACT_PARAMならば提供されているので、
                // アプリケーションクラスを返すファクトリクラスを作って、それを指定するようにしています.
                params.put(WicketFilter.APP_FACT_PARAM, F.class.getName());

                filter(path).through(new E(new IProvider<Injector>() {

                    @SuppressWarnings("synthetic-access")
                    @Override
                    public Injector get() {
                        return WebInitializer.this.injector;
                    }
                }), params);
            }
        });
    }

    private void initializeDatabase() {
        this.injector.getInstance(IUserService.class).insertAdministratorIfNotExists();
    }

    private static Dynamic addFilter(final ServletContext pServletContext, final Class<? extends Filter> pFilterType) {
        return pServletContext.addFilter(pFilterType.getName(), pFilterType);
    }

    @SuppressWarnings("nls")
    private static void addGzipFilter(final ServletContext pServletContext) {
        final Dynamic filter = addFilter(pServletContext, GzipFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, PATH_REST + WILD_CARD);
        // filter.setInitParameter("minGzipSize", Integer.toString(40));
        filter.setInitParameter("mimeTypes" //
                , "text/html" //
                        + ",text/plain" //
                        + ",text/xml" //
                        + ",text/css" //
                        + ",application/json" //
                        + ",application/xhtml+xml" //
                        + ",application/javascript" //
                        + ",application/x-javascript" //
                        + ",image/svg+xml" //
        );
    }

    private static String getPersistenceUnitName() {
        return isDatabaseUrlSet() ? Environment.getApplicationName() : Environment.getApplicationName() + "_WithDataSource"; //$NON-NLS-1$
    }

    private static boolean isDatabaseUrlSet() {
        final String p = System.getProperty(SystemPropertyToPostgreJpaPropertiesParser.KEY_DATABASE_URL);
        return p != null && p.length() > 0;
    }

    /**
     * 
     */
    public static class F implements IWebApplicationFactory {

        /**
         * @see org.apache.wicket.protocol.http.IWebApplicationFactory#createApplication(org.apache.wicket.protocol.http.WicketFilter)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public WebApplication createApplication(final WicketFilter pFilter) {
            return new WicketApplication(((E) pFilter).injectorProvider);
        }

        /**
         * @see org.apache.wicket.protocol.http.IWebApplicationFactory#destroy(org.apache.wicket.protocol.http.WicketFilter)
         */
        @Override
        public void destroy(@SuppressWarnings("unused") final WicketFilter pFilter) {
            // 処理なし
        }
    }

    private static class DataStoreProvider implements Provider<IDataStore> {
        private EntityManagerFactory entityManagerFactory;

        @Override
        public IDataStore get() {
            switch (Environment.getDataStoreMode()) {
            case FILE:
                return new FileDataStore();
            case LOB:
                return new LobDataStore(this.entityManagerFactory);
            case S3:
                return new S3DataStore();
            default:
                throw new IllegalStateException();
            }
        }

        @Inject
        void set(final EntityManagerFactory pEntityManagerFactory) {
            this.entityManagerFactory = pEntityManagerFactory;
        }

    }

    /**
     * 
     */
    @Singleton
    private static class E extends WicketFilter {
        private final IProvider<Injector> injectorProvider;

        E(final IProvider<Injector> pInjectorProvider) {
            this.injectorProvider = pInjectorProvider;
        }
    }
}
