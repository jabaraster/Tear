/**
 * 
 */
package jp.co.city.tear.model;

/**
 * @author jabaraster
 */
public class UnmatchPassword extends Exception {
    private static final long  serialVersionUID = 6961839040728641843L;

    /**
     * 
     */
    @SuppressWarnings("synthetic-access")
    public static final Global INSTANCE         = new Global();

    /**
     * 
     */
    public static final class Global extends UnmatchPassword {
        private static final long serialVersionUID = 3551288687693896812L;

        private Global() {
            //
        }
    }

}
