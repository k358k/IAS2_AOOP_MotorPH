package Main;

import Config.ApplicationThemeUtility;
import DAO.PayrollMgmtImpl;
import Util.DatabaseConnectionUtil;
import View.LoginView;
import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        ApplicationThemeUtility.setIntelliJ();
        
        DatabaseConnectionUtil.getConnection();
        EventQueue.invokeLater(() -> {
            try {
                LoginView frame = new LoginView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
