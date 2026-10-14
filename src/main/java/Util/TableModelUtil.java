package Util;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.function.Function;

public class TableModelUtil{

    /**
     * Generic method to create and populate a table model
     * @param data List of objects to populate
     * @param columnNames Array of column names
     * @param rowMapper Function to convert object to row data array
     * @return Populated DefaultTableModel
     */
    public static <T> DefaultTableModel createTableModel(
            List<T> data, 
            String[] columnNames, 
            Function<T, Object[]> rowMapper) {

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (T item : data) {
            Object[] rowData = rowMapper.apply(item);
            tableModel.addRow(rowData);
        }

        return tableModel;
    }

    /**
     * Create a non-editable table model
     */
    public static <T> DefaultTableModel createReadOnlyTableModel(
        List<T> data,
        String[] columnNames,
        Function<T, Object[]> rowMapper) {

        // Create an anonymous inner class extending DefaultTableModel
        return new DefaultTableModel(columnNames, 0) { // Initialize with columns, 0 rows
            { // This is an instance initializer block, runs after constructor
                if (data != null) {
                    for (T item : data) {
                        Object[] rowData = rowMapper.apply(item);
                        addRow(rowData); // Add row to THIS model instance
                    }
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
    }
}