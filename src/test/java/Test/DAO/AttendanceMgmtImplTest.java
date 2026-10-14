package Test.DAO;

import DAO.AttendanceMgmtImpl;
import Model.ClockHour;
import Model.PayPeriod;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceMgmtImplTest {

    private static AttendanceMgmtImpl dao;

    @BeforeAll
    static void setup() {
        dao = new AttendanceMgmtImpl(); // Initializes the DAO and DB connection
        System.out.println("DAO initialized.");
    }

    @Test
    void testFindAllClockHourRecords() throws SQLException {  
        List<ClockHour> records = dao.findAllClockHourRecords();
        System.out.println("All Clock Hour Records:");
        records.forEach(System.out::println);

        assertNotNull(records);
    }

    @Test
    void testFindClockHoursByEmployee() throws SQLException { 
        List<ClockHour> records = dao.findClockHoursByEmployee(10001);
        System.out.println("Clock Hours for Employee ID 10001:");
        records.forEach(System.out::println);

        assertNotNull(records);
    }

    @Test
    void testFindUnclosedClockHourRecordByEmployee() throws SQLException {
        ClockHour clockHour = dao.findUnclosedClockHourRecordByEmployee(10001);
        if (clockHour != null) {
            System.out.println("Unclosed Clock Hour Record for Employee 10001:");
            System.out.println(clockHour);
        } else {
            System.out.println("No unclosed clock hour record found for Employee 10001.");
        }

        assertTrue(clockHour == null || clockHour.getEmpId() == 10001);
    }

    @Test
    void testFindClockHoursByEmployeeIdAndPayPeriod() throws SQLException {
        // Fixed: Create PayPeriod with constructor arguments
        PayPeriod payPeriod = new PayPeriod(
            LocalDate.of(2025, 1, 1), 
            LocalDate.of(2025, 12, 31)
        );

        List<ClockHour> records = dao.findClockHoursByEmployeeIdAndPayPeriod(10001, payPeriod);
        System.out.println("Clock Hours for Employee 10001 within Pay Period 2025:");
        records.forEach(System.out::println);
        assertNotNull(records);
    }

    @Test
    void testCreateClockHourRecord() throws SQLException {
        ClockHour clockHour = new ClockHour();
        clockHour.setEmpId(10001);
        clockHour.setClockInDateTime(LocalDateTime.now().withHour(9).withMinute(0));
        clockHour.setClockOutDateTime(LocalDateTime.now().withHour(17).withMinute(0));

        boolean result = dao.createClockHourRecord(clockHour);
        System.out.println("Created Clock Hour Record:");
        System.out.println(clockHour);

        assertTrue(result);
        assertTrue(clockHour.getClockHourId() > 0);
    }

    @Test
    void testCloseClockHourRecord() throws SQLException {
        ClockHour openRecord = dao.findUnclosedClockHourRecordByEmployee(10001);
        if (openRecord != null) {
            openRecord.setClockOutDateTime(LocalDateTime.now());
            boolean closed = dao.closeClockHourRecord(openRecord);
            System.out.println("Closed Clock Hour Record:");
            System.out.println(openRecord);

            assertTrue(closed);
            assertNotNull(openRecord.getClockOutDateTime());
        } else {
            System.out.println("No open record found to close for Employee 10001.");
        }
    }

    @Test
    void testDeleteClockHourRecord() throws SQLException {
        // Create dummy record to test deletion
        ClockHour clockHour = new ClockHour();
        clockHour.setEmpId(10001);
        clockHour.setClockInDateTime(LocalDateTime.now().withHour(10));
        clockHour.setClockOutDateTime(LocalDateTime.now().withHour(11));
        boolean created = dao.createClockHourRecord(clockHour);
        assertTrue(created);

        System.out.println("Attempting to delete record:");
        System.out.println(clockHour);

        // Now delete it
        boolean deleted = dao.deleteClockHourRecord(clockHour.getClockHourId());
        System.out.println("Deletion status: " + deleted);

        assertTrue(deleted);
    }
}

