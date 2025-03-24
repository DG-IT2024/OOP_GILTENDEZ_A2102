
import com.opencsv.CSVWriter;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

public class AttendanceAdmin extends javax.swing.JFrame {

    private final String csvFile = "Formatted_Timesheet.csv";

    public AttendanceAdmin() throws FileNotFoundException, IOException {
        initComponents();

        setIconImage();

    }

    // Method to parse records into Employee objects
    public static List<Timesheet> parseRecords(List<String[]> records) {
        List<Timesheet> timesheets = new ArrayList<>();
        for (String[] record : records) {
            String employeeNumber = record[0];
            String date = record[1];
            String timeIn = record[2];
            String timeOut = record[3];

            Timesheet timesheet = new Timesheet(employeeNumber, date, timeIn, timeOut);
            timesheets.add(timesheet);
        }

        return timesheets;
    }

    public List<Attendance> filterTimesheets(String employeeNumber, String startDateStr, String endDateStr) {
        List<Attendance> filtered = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Match CSV format

        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            Filehandling filehandler = new Filehandling(csvFile);
            List<String[]> records = filehandler.readCSV();
            List<Timesheet> allTimesheets = parseRecords(records);

            for (Timesheet t : allTimesheets) {
                Date recordDate = sdf.parse(t.getDate());

                if (t.getEmployeeNumber().equals(employeeNumber)
                        && !recordDate.before(startDate) && !recordDate.after(endDate)) {

                    // Just create Attendance object, hours are computed inside
                    Attendance attendance = new Attendance(t.getEmployeeNumber(), t.getDate(), t.getTimeIn(), t.getTimeOut());

                    filtered.add(attendance);
                }
            }

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing dates or reading file.");
        }

        return filtered;
    }

    public void calculateHourSummary(List<Attendance> timesheets) {
    }

    public void displayFilteredTimesheets(List<Attendance> timesheets) {
        DefaultTableModel tableModel = (DefaultTableModel) jTableTimeSheet.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        int regularHours = 0;
        int overtime = 0;

        for (Attendance timesheet : timesheets) {
            tableModel.addRow(new Object[]{
                timesheet.getDate(),
                timesheet.getTimeIn(),
                timesheet.getTimeOut(),
                timesheet.getRegularHours(),
                timesheet.getOvertimeHours()
            });

            regularHours += timesheet.getRegularHours();
            overtime += timesheet.getOvertimeHours();
        }

        jTextFieldRegularHours.setText(String.valueOf(regularHours));
        jTextFieldOvertimeHours.setText(String.valueOf(overtime));
    }

    public void handleWindowClosing() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Do you want to save before closing?",
                "Save",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {

            setVisible(false); // Close the application after saving
        } else if (option == JOptionPane.NO_OPTION) {
            setVisible(false); // Close the application without saving
        }
    }

    private static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private Date convertToDate(Object dateObj) throws ParseException {
        if (dateObj instanceof Date) {
            return (Date) dateObj;
        } else if (dateObj instanceof String) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse((String) dateObj);
        } else {
            throw new ParseException("Unparseable date: " + dateObj, 0);
        }
    }

    private boolean isValidDateRange(String startDateStr, String endDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            if (startDate.after(endDate)) {
                JOptionPane.showMessageDialog(this,
                        "Start Date must be earlier than or equal to End Date.",
                        "Invalid Date Range",
                        JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

        } catch (ParseException e) {

            return false;
        }

        return true; // ✅ Dates are valid
    }

    private void saveSummaryToCSV(String empNum, String startDate, String endDate, String regularHours, String overtimeHours) {
        String filePath = "Employee_Hours_Worked1.csv";
        File file = new File(filePath);
        boolean fileExists = file.exists();
        boolean isNewFile = false;

        try {
            // Check if the file is empty or doesn't exist
            if (!fileExists || file.length() == 0) {
                isNewFile = true;
            }

            // Create CSVWriter with append = true
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {

                // Write header if it's a new file
                if (isNewFile) {
                    String[] header = {"EmployeeNumber", "GeneratedDateTime", "StartDate", "EndDate", "RegularHours", "OvertimeHours"};
                    writer.writeNext(header);
                }

                // Get current timestamp
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = dtf.format(LocalDateTime.now());

                // Prepare the data row
                String[] row = {
                    empNum,
                    timestamp,
                    startDate,
                    endDate,
                    String.valueOf(regularHours),
                    String.valueOf(overtimeHours)
                };

                // Write the data row
                writer.writeNext(row);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save summary to CSV.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//
//    private List<String> createTableIdList() {
//        DefaultTableModel model = (DefaultTableModel) jTableTimeSheet.getModel();
//        List<String> tableIdList = new ArrayList<>();
//
//        for (int i = 0; i < model.getRowCount(); i++) {
//            String id = model.getValueAt(i, 0).toString();
//            tableIdList.add(id);
//        }
//        return tableIdList;
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButtonCalculate2 = new javax.swing.JButton();
        jButtonPublish1 = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldEmployeeNum = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldRegularHours = new javax.swing.JTextField();
        jTextFieldOvertimeHours = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTimeSheet = new javax.swing.JTable();
        jDateChooserEndDate = new com.toedter.calendar.JDateChooser();
        jDateChooserStartDate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GILTENDEZ | OOP |  A2102");
        setAutoRequestFocus(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(222, 194, 110));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonCalculate2.setText("CALCULATE");
        jButtonCalculate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCalculate2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonCalculate2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 150, -1));

        jButtonPublish1.setText("PUBLISH");
        jButtonPublish1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPublish1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonPublish1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, -1));

        jButtonClose.setText("CLOSE");
        jButtonClose.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 150, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 207, 180, 283));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(204, 0, 51));
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, -1, -1));

        jLabel4.setText("Name");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, -1, -1));

        jTextFieldName.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldName.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldName.setEnabled(false);
        jTextFieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNameActionPerformed(evt);
            }
        });
        jTextFieldName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldNameKeyTyped(evt);
            }
        });
        getContentPane().add(jTextFieldName, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, 170, 22));

        jLabel3.setText("Employee No.");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, -1, -1));

        jTextFieldEmployeeNum.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldEmployeeNum.setCaretColor(new java.awt.Color(51, 51, 51));
        jTextFieldEmployeeNum.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldEmployeeNum.setEnabled(false);
        jTextFieldEmployeeNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmployeeNumActionPerformed(evt);
            }
        });
        jTextFieldEmployeeNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldEmployeeNumKeyTyped(evt);
            }
        });
        getContentPane().add(jTextFieldEmployeeNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 170, 22));

        jLabel6.setText("Period Start Date");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, -1, -1));

        jLabel5.setText("Period End Date");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("SUMMARY");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 170, -1, -1));

        jLabel7.setText("Overtime Hours");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 250, -1, -1));

        jLabel9.setText("Regular Hours");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 210, -1, -1));

        jTextFieldRegularHours.setEditable(false);
        jTextFieldRegularHours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldRegularHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRegularHoursActionPerformed(evt);
            }
        });
        getContentPane().add(jTextFieldRegularHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 210, 50, -1));

        jTextFieldOvertimeHours.setEditable(false);
        jTextFieldOvertimeHours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(jTextFieldOvertimeHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 250, 50, -1));

        jTableTimeSheet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DATE", "TIME-IN", "TIME-OUT", "REGULAR", "OVERTIME"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableTimeSheet.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableTimeSheet.getTableHeader().setReorderingAllowed(false);
        jTableTimeSheet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableTimeSheetMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableTimeSheet);
        if (jTableTimeSheet.getColumnModel().getColumnCount() > 0) {
            jTableTimeSheet.getColumnModel().getColumn(0).setResizable(false);
            jTableTimeSheet.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTableTimeSheet.getColumnModel().getColumn(1).setResizable(false);
            jTableTimeSheet.getColumnModel().getColumn(1).setPreferredWidth(40);
            jTableTimeSheet.getColumnModel().getColumn(2).setResizable(false);
            jTableTimeSheet.getColumnModel().getColumn(2).setPreferredWidth(40);
            jTableTimeSheet.getColumnModel().getColumn(3).setResizable(false);
            jTableTimeSheet.getColumnModel().getColumn(3).setPreferredWidth(40);
            jTableTimeSheet.getColumnModel().getColumn(4).setResizable(false);
            jTableTimeSheet.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 400, 390));

        jDateChooserEndDate.setBackground(new java.awt.Color(255, 255, 255));
        jDateChooserEndDate.setToolTipText("");
        jDateChooserEndDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jDateChooserEndDateKeyTyped(evt);
            }
        });
        getContentPane().add(jDateChooserEndDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 110, 160, 30));

        jDateChooserStartDate.setBackground(new java.awt.Color(255, 255, 255));
        jDateChooserStartDate.setToolTipText("");
        jDateChooserStartDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jDateChooserStartDateKeyTyped(evt);
            }
        });
        getContentPane().add(jDateChooserStartDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, 160, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/photos/Attendance.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 620));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void jTextFieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNameActionPerformed

    private void jTextFieldNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNameKeyTyped
        // TODO add your handling code here:
        InputValidator.allowValidNameCharacters(evt);
    }//GEN-LAST:event_jTextFieldNameKeyTyped

    private void jTextFieldEmployeeNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEmployeeNumActionPerformed

    private void jTextFieldEmployeeNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNumKeyTyped
        // TODO add your handling code here:
        InputValidator.allowOnlyDigits(evt);
    }//GEN-LAST:event_jTextFieldEmployeeNumKeyTyped

    private void jTextFieldRegularHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRegularHoursActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRegularHoursActionPerformed

    private void jButtonCalculate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCalculate2ActionPerformed
        // TODO add your handling code here:

        String empNum = jTextFieldEmployeeNum.getText().trim();
        String startDate = formatDate(jDateChooserStartDate.getDate());
        String endDate = formatDate(jDateChooserEndDate.getDate());

        // Validate inputs
        if (empNum.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidDateRange(startDate, endDate)) {
            return;
        }

        // If valid, continue
        List<Attendance> filteredTimesheets = filterTimesheets(empNum, startDate, endDate);
        displayFilteredTimesheets(filteredTimesheets);


    }//GEN-LAST:event_jButtonCalculate2ActionPerformed

    private void jButtonPublish1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPublish1ActionPerformed
        // TODO add your handling code here:

        int response = JOptionPane.showConfirmDialog(null, "Do you want to proceed with saving the changes to the database?",
                "Update Database Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            String empNum = jTextFieldEmployeeNum.getText();
            String startDate = formatDate(jDateChooserStartDate.getDate());
            String endDate = formatDate(jDateChooserEndDate.getDate());
            String regularHours = jTextFieldRegularHours.getText();
            String overtimeHours = jTextFieldOvertimeHours.getText();
            saveSummaryToCSV(empNum, startDate, endDate, regularHours, overtimeHours);

        }

    }//GEN-LAST:event_jButtonPublish1ActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        // TODO add your handling code here:
        handleWindowClosing();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jTableTimeSheetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableTimeSheetMouseClicked

    }//GEN-LAST:event_jTableTimeSheetMouseClicked

    private void jDateChooserEndDateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooserEndDateKeyTyped
        // TODO add your handling code here:
        jDateChooserEndDate.getDateEditor().getUiComponent().setEnabled(false);
    }//GEN-LAST:event_jDateChooserEndDateKeyTyped

    private void jDateChooserStartDateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooserStartDateKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooserStartDateKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmployeeProfileAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeProfileAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeProfileAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeProfileAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AttendanceAdmin().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(AttendanceAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCalculate2;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonPublish1;
    private com.toedter.calendar.JDateChooser jDateChooserEndDate;
    private com.toedter.calendar.JDateChooser jDateChooserStartDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableTimeSheet;
    public javax.swing.JTextField jTextFieldEmployeeNum;
    public javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldOvertimeHours;
    private javax.swing.JTextField jTextFieldRegularHours;
    // End of variables declaration//GEN-END:variables

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.jpg")));
    }

}
