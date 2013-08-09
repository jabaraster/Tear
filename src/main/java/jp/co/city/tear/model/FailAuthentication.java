/**
 * 
 */
package jp.co.city.tear.model;

/**
 * @author jabaraster
 */
public class FailAuthentication extends Exception {
    private static final long  serialVersionUID = -8925867044752171279L;

    /**
     * 
     */
    @SuppressWarnings("synthetic-access")
    public static final Global INSTANCE         = new Global();

    /**
     * 
     */
    public static final class Global extends FailAuthentication {
        private static final long serialVersionUID = 3551288687693896812L;

        private Global() {
            //
        }
    }
}
