package Test;

import DAO.AttendanceMgmtImpl;
import Model.ClockHour;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AttendanceMgmtDAOTest {
    /*
    @BeforeAll - This is you setup before running all the unit tests.
    @Test - You can create many Test annotations for all your unit test.
    @AfterAll - This is the code that will be executed after all the unit tests.
    */
    
    private static AttendanceMgmtImpl attendanceDAO;
    
    @BeforeAll
    public static void setup() throws SQLException{
        System.out.println("Running Before All...");
        attendanceDAO = new AttendanceMgmtImpl();
    }
    
    @Test
    public void testFindAllClockHourRecords() throws SQLException{
        System.out.println("Running testFindAllClockHourRecords...");
        
        List<ClockHour> attendanceRecords = attendanceDAO.findAllClockHourRecords();
        assertNotNull(attendanceRecords, "Attendance Records List should not be null");
        assertTrue(!attendanceRecords.isEmpty(), "Attendance Records list should contain at least one clock hour record.");
    }
    
    @AfterAll
    public static void teardown() throws SQLException{
    }
}

