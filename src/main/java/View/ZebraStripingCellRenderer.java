package View;

import java.awt.Component;

import java.awt.Color;

import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;

public class ZebraStripingCellRenderer extends DefaultTableCellRenderer {

    private static final Color ODD_ROW_COLOR = new Color(240, 240, 240); // Light gray

    private static final Color EVEN_ROW_COLOR = Color.WHITE;

    private static final Color SELECTION_COLOR = new Color(123, 152, 199);

    @Override

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        // Get the default renderer component for the cell
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Apply alternating row colors if not selected
        if (!isSelected) {

            if (row % 2 == 0) { // Even row (0, 2, 4...)

                c.setBackground(EVEN_ROW_COLOR);

            } else { // Odd row (1, 3, 5...)

                c.setBackground(ODD_ROW_COLOR);

            }

        } else {

            // If the row is selected, use the default selection color
            c.setBackground(SELECTION_COLOR);

            c.setForeground(table.getSelectionForeground());

        }

        // Restore default foreground if not selected
        if (!isSelected) {

            c.setForeground(table.getForeground());

        }

        return c;

    }

}
