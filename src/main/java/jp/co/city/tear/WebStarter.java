package jp.co.city.tear;

import jabara.jetty_memcached.MemcachedSessionServerStarter;

import javax.naming.NamingException;

import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcDataSource;

/**
 * 
 */
public class WebStarter {
    /**
     * @throws NamingException -
     */
    @SuppressWarnings({ "unused", "nls" })
    public static void initializeDataSource() throws NamingException {
        // H2Databaseの場合
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:target/db/db");
        dataSource.setUser("sa");

        // PostgreSQLの場合
        // final PGPoolingDataSource dataSource = new PGPoolingDataSource();
        // dataSource.setDatabaseName("Aac");
        // dataSource.setUser("postgres");
        // dataSource.setPassword("postgres");

        new Resource("jdbc/" + Environment.getApplicationName(), dataSource);
    }

    /**
     * @param pArgs 起動引数.
     * @throws NamingException -
     */
    public static void main(final String[] pArgs) throws NamingException {
        initializeDataSource();
        // final ServerStarter server = new ServerStarter();
        // server.start();
        // MemcachedをHttpSessionのストアとして使う場合.
        // 主にHerokuでの運用を意識.
        new MemcachedSessionServerStarter().start();
    }
}
