//Author: Trisha Quismundo
//28/05/2025 1:11am

package Config;
import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class ApplicationThemeUtility {
     /**
     * Sets the default FlatLaf theme for the application.
     * This method should be called before any Swing components are initialized.
     */
    
    //THEMES
    public static void setIntelliJ()
    /* A light theme inspired by the IntelliJ IDEA default light theme. 
    *  It has subtle differences from FlatLightLaf in elements like checkboxes, radio buttons, and focus borders.
    */ 
    {
        try {
            FlatIntelliJLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to set Theme to FlatIntelliJLaf" + e);
        }
    }
    
    public static void setFlatDark(){
        //The default dark theme, providing a dark color scheme.
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to set Theme to FlatDarkLaf" + e);
        }
    }
    
    public static void setFlatLight(){
        //The default light theme, offering a clean and modern flat design.
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to set Theme to FlatLightLaf" + e);
        }
    }
    
    public static void setFlatDarcula(){
        // A dark theme inspired by the IntelliJ IDEA Darcula theme. Similar to FlatIntelliJLaf, it has subtle differences from FlatDarkLaf.
        try {
            FlatDarculaLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to set Theme to FlatDarculaLaf" + e);
        }
    }
    
    public static void setMacDark() {
        try {
            FlatMacDarkLaf.setup();
        } catch (Exception e) {
            System.err.println("Failed to set Theme to FlatMacDark");
        }
    }
}
