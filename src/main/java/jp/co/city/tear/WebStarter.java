package jp.co.city.tear;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.jetty_memcached.MemcachedSessionServerStarter;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcDataSource;

/**
 * 
 */
public class WebStarter {
    /**
     * 
     */
    public static void initializeDataSource() {
        initializeDataSource(Mode.WEB_APPLICATION);
    }

    /**
     * @param pMode -
     */
    @SuppressWarnings({ "nls", "unused" })
    public static void initializeDataSource(final Mode pMode) {
        ArgUtil.checkNull(pMode, "pMode"); //$NON-NLS-1$

        try {
            // H2Databaseの場合
            final JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:target/db/db_" + pMode);
            dataSource.setUser("sa");

            // PostgreSQLの場合
            // final PGPoolingDataSource dataSource = new PGPoolingDataSource();
            // dataSource.setDatabaseName("Aac");
            // dataSource.setUser("postgres");
            // dataSource.setPassword("postgres");

            new Resource("jdbc/" + Environment.getApplicationName(), dataSource);

        } catch (final NamingException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @param pArgs 起動引数.
     */
    public static void main(final String[] pArgs) {
        initializeDataSource(Mode.WEB_APPLICATION);
        // final ServerStarter server = new ServerStarter();
        // server.start();
        // MemcachedをHttpSessionのストアとして使う場合.
        // 主にHerokuでの運用を意識.
        final MemcachedSessionServerStarter server = new MemcachedSessionServerStarter();
        server.setWebPort(8082);
        server.start();
    }

    /**
     * @author jabaraster
     */
    public enum Mode {
        /**
         * 
         */
        WEB_APPLICATION,
        /**
         * 
         */
        UNIT_TEST, ;
    }
}
