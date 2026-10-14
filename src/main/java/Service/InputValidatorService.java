package Service;

public class InputValidatorService {

    /**
     * Validates date format.
     *
     * @param date the date string to validate
     * @return true if valid format (yyyy-mm-dd), false otherwise
     */
    public static boolean validateDateFormat(String date) {
        // Check if the date matches the yyyy-mm-dd format
        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return false;
        }

        // Split the date into components
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        // Check if month is valid (1-12)
        if (month < 1 || month > 12) {
            return false;
        }

        // Check if day is valid based on the month
        if (day < 1 || day > 31) {
            return false;
        }

        // Check for months with specific day limits
        switch (month) {
            case 2: // February
                // Check for leap year
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    if (day > 29) {
                        return false; // Leap year
                    }
                } else {
                    if (day > 28) {
                        return false; // Non-leap year
                    }
                }
                break;
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                if (day > 30) {
                    return false; // Months with 30 days
                }
                break;
        }

        return true; // Valid date
    }

    public static boolean validateTimeFormat(String timeInput) {
        return timeInput.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$"); // HH:MM:SS format
    }

    /**
     * Validates SSS number format.
     *
     * @param sssNumberInput the SSS number string to validate
     * @return true if valid format (##-#######-#), false otherwise
     */
    public static boolean validateSssFormat(String sssNumberInput) {
        return sssNumberInput.matches("^\\d{2}-\\d{7}-\\d{1}$");  // XX-XXXXXXX-X format
    }

    /**
     * Validates PhilHealth number format.
     *
     * @param philhealthNumberInput the PhilHealth number string to validate
     * @return true if valid format (############), false otherwise
     */
    public static boolean validatePhilhealthFormat(String philhealthNumberInput) {
        return philhealthNumberInput.matches("^\\d{12}$");  // 12 digits
    }

    /**
     * Validates Pag-IBIG number format.
     *
     * @param pagibigNumberInput the Pag-IBIG number string to validate
     * @return true if valid format (#############), false otherwise
     */
    public static boolean validatePagibigFormat(String pagibigNumberInput) {
        return pagibigNumberInput.matches("^\\d{12}$");  // 12 digits
    }

    /**
     * Validates TIN number format.
     *
     * @param tinInput the TIN number string to validate
     * @return true if valid format (###-###-###-###), false otherwise
     */
    public static boolean validateTinFormat(String tinInput) {
        return tinInput.matches("^\\d{3}-\\d{3}-\\d{3}-\\d{3}$");  // XXX-XXX-XXX-XXX format
    }

    /**
     * Validates phone number format.
     *
     * @param phoneNumberInput the phone number string to validate
     * @return true if valid format (###-###-###), false otherwise
     */
    public static boolean validatePhoneNumberFormat(String phoneNumberInput) {
        return phoneNumberInput.matches("^\\d{3}-\\d{3}-\\d{3}$");  // XXX-XXX-XXX format
    }
}
