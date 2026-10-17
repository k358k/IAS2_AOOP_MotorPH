
package Util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class AuditLogger {

    private static final Path LOG_FILE =
            Path.of("logs", "motorph-audit.log");

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditLogger() {
        // Utility class; do not instantiate.
    }

    public static synchronized void logActivity(
            String action, String employeeId) {

        String timestamp = LocalDateTime.now()
                .format(TIMESTAMP_FORMAT);

        String safeAction = sanitize(action);
        String safeEmployeeId = sanitize(employeeId);

        String entry = timestamp
                + " | Action: " + safeAction
                + " | Employee ID: " + safeEmployeeId
                + System.lineSeparator();

        try {
            Files.createDirectories(LOG_FILE.getParent());

            Files.writeString(
                    LOG_FILE,
                    entry,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println(
                    "Unable to write audit log: " + e.getMessage());
        }
    }

    private static String sanitize(String value) {
        if (value == null) {
            return "N/A";
        }

        return value.replace('\n', '_')
                    .replace('\r', '_')
                    .replace('|', '_');
    }
}
