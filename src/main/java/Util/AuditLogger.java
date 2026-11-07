package Util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Control 2: Comprehensive Audit Logging System
 * Incorporates User Identity, Source IP Tracking, CRUD Action Context,
 * File-Level Permission Lockdowns, and SHA-256 Tamper Detection.
 */
public final class AuditLogger {

    // Target log file path inside the project logs folder
    private static final Path LOG_FILE = Path.of("logs", "motorph-audit.log");

    // Standard date-time format for log entries
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditLogger() {
        // Utility class; do not instantiate.
    }

    /**
     * Backward-compatible log method (Action + Target Employee ID)
     */
    public static synchronized void logActivity(String action, String targetId) {
        logActivity("SYSTEM_USER", action, targetId, "N/A");
    }

    /**
     * Enhanced Audit Logging Method meeting MS2 and mentor requirements.
     * Logs: Timestamp | User ID | Source IP | Action | Target ID | Details | SHA-256 Checksum
     */
    public static synchronized void logActivity(String userId, String action, String targetId, String details) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String ipAddress = getLocalIpAddress();

        // Clean input values to prevent log injection/tampering
        String safeUser = sanitize(userId != null && !userId.isBlank() ? userId : "ANONYMOUS");
        String safeAction = sanitize(action);
        String safeTargetId = sanitize(targetId);
        String safeDetails = sanitize(details);

        // Pre-hash payload text
        String basePayload = String.format("%s | User: %s | IP: %s | Action: %s | Target ID: %s | Details: %s",
                timestamp, safeUser, ipAddress, safeAction, safeTargetId, safeDetails);

        // Tamper-Detection: Compute SHA-256 hash of the line
        String checksum = computeSHA256(basePayload);

        // Complete log entry line with appended checksum
        String entry = basePayload + " | Hash: " + checksum + System.lineSeparator();

        try {
            // 1. Ensure logs/ folder exists
            if (LOG_FILE.getParent() != null) {
                Files.createDirectories(LOG_FILE.getParent());
            }

            // 2. Temporarily enable write permissions if file already exists
            File logFileObj = LOG_FILE.toFile();
            if (logFileObj.exists()) {
                logFileObj.setWritable(true, false);
            }

            // 3. Append log entry using UTF-8
            Files.writeString(
                    LOG_FILE,
                    entry,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            );

            // 4. File Access Control: Set to Read-Only so standard users cannot edit/delete
            logFileObj.setWritable(false, false);

        } catch (IOException e) {
            System.err.println("Unable to write audit log: " + e.getMessage());
        }
    }

    /**
     * Dynamically captures the Source IP address of the local system machine.
     */
    private static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    /**
     * Generates a SHA-256 checksum for tamper verification.
     */
    private static String computeSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().substring(0, 12); // First 12 chars for clean formatting
        } catch (NoSuchAlgorithmException e) {
            return "HASH_ERROR";
        }
    }

    /**
     * Sanitizes inputs by removing newlines and pipe characters.
     */
    private static String sanitize(String value) {
        if (value == null) {
            return "N/A";
        }
        return value.replace('\n', '_')
                    .replace('\r', '_')
                    .replace('|', '_');
    }
}