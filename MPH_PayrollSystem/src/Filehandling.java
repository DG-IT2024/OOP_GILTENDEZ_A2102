
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Filehandling {

    private String csvFile;
    

    // Constructor
    public Filehandling(String csvFile) {
        this.csvFile = csvFile;
    }

    // Setter for file path (allows flexibility)
    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    // Method to read CSV file and return records
    public List<String[]> readCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> records = reader.readAll();
            if (!records.isEmpty()) {
                records.remove(0); // Assuming the first row is a header
            }
            return records;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found: " + csvFile, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading the CSV file: " + csvFile, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Method to export JTable to CSV
    public void exportTableToCSV(JTable table) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Write column headers
            int columnCount = model.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = model.getColumnName(i);
            }
            writer.writeNext(columnNames);

            // Write rows
            int rowCount = model.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                String[] rowData = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    rowData[j] = model.getValueAt(i, j).toString();
                }
                writer.writeNext(rowData);
            }

            JOptionPane.showMessageDialog(null, "Database updated successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to export data to CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
       

}
