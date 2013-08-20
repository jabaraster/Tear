package jabara.servlet;

/**
 * @author jabaraster
 */
public final class Stop extends Exception {
    private static final long serialVersionUID = 7136696838961800917L;

    /**
     * 
     */
    public static final Stop  INSTANCE         = new Stop();

    private Stop() {
        // 処理なし
    }
}