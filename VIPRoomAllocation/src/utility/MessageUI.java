package utility;

/**
 * Utility class containing shared, static console-message helpers used by
 * boundary classes. Contains only static methods/variables, as required for
 * utility classes under the ECB pattern.
 *
 * @author Your Name
 */
public class MessageUI {

    private MessageUI() {
        // Prevent instantiation; this is a static-only utility class.
    }

    public static void printHeader(String title) {
        System.out.println("\n===== " + title + " =====");
    }

    public static void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void printDivider() {
        System.out.println("-----------------------------------------------------------");
    }
}
