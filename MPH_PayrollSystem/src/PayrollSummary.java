/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author danilo
 */
public class PayrollSummary extends javax.swing.JFrame {

    public static List<EmployeePayroll> employees = new ArrayList<>();

    /**
     * Creates new form PayrollSummary2
     */
    public PayrollSummary() throws FileNotFoundException, IOException {
        initComponents();

        String csvFile = "PayrollRecords.csv";

        csvRun(csvFile);

        populatecomboboxCoveredPeriods();
        populateByEmployeeNum();

        setIconImage();
    }

    private void csvRun(String csvFile) throws FileNotFoundException, IOException {
        employees.clear();  // Clear existing entries before reading new data

        Filehandling filehandler = new Filehandling(csvFile);
        List<String[]> records = filehandler.readCSV();

        //List<EmployeePayroll> employees = parseRecords(records);
        parseRecords(records);
        informationTable(employees);
    }

    //public static List<EmployeePayroll> parseRecords(List<String[]> records) {
    public static void parseRecords(List<String[]> records) {
        for (String[] record : records) {
            String employeeNo = record[0];
            String lastName = record[1];
            String firstName = record[2];
            String regularHours = record[3];
            String overtimeHours = record[4];
            String basicSalary = record[5];
            String hourlyRate = record[6];
            String grossIncome = record[7];
            String sssDeduction = record[8];
            String philHealthDeduction = record[9];
            String pagibigDeduction = record[10];
            String withholdingTax = record[11];
            String startDate = record[12];
            String endDate = record[13];
            String benefits = record[14];
            String totalDeductions = record[15];
            String takeHomePay = record[16];

            EmployeePayroll payroll = new EmployeePayroll(
                    employeeNo,
                    lastName,
                    firstName,
                    regularHours,
                    overtimeHours,
                    basicSalary,
                    hourlyRate,
                    grossIncome,
                    sssDeduction,
                    philHealthDeduction,
                    pagibigDeduction,
                    withholdingTax,
                    startDate,
                    endDate,
                    benefits,
                    totalDeductions,
                    takeHomePay
            );
            employees.add(payroll);
        }

        //return employees;
    }

    private void informationTable(List<EmployeePayroll> employees) {
        DefaultTableModel tableModel = (DefaultTableModel) jTablePayrollSummary.getModel();
        tableModel.setRowCount(0); // Clear existing rows
        for (EmployeePayroll payroll : employees) {
            tableModel.addRow(new Object[]{
                payroll.getEmployeeNo(),
                payroll.getLastName(),
                payroll.getFirstName(),
                payroll.getRegularHours(),
                payroll.getOvertimeHours(),
                payroll.getBasicSalary(),
                payroll.getHourlyRate(),
                payroll.getGrossIncome(),
                payroll.getSssDeduction(),
                payroll.getPhilHealthDeduction(),
                payroll.getPagibigDeduction(),
                payroll.getWithholdingTax(),
                payroll.getStartDate(),
                payroll.getEndDate(),
                payroll.getBenefits(),
                payroll.getTotalDeductions(),
                payroll.getTakeHomePay()
            });
        }
    }

//    private String[] populateByMonth() {
//        String[] months = {"",
//            "January", "February", "March", "April", "May", "June",
//            "July", "August", "September", "October", "November", "December"
//        };
//
//        return months;
//    }
//
//    private List<String> populateByYear() {
//        List<String> years = new ArrayList<>();
//        years.add(""); // Adding empty string as the first element
//        int currentYear = Year.now().getValue();
//        for (int i = currentYear - 4; i <= currentYear; i++) {  //assuming company store data for 5 years
//            years.add(String.valueOf(i));
//        }
//        return years;
//    }
//    private void populatecomboboxCoveredPeriods() {
//
//        String[] months = populateByMonth();
//        for (String month : months) {
//            jComboBoxCoveredPeriod.addItem(month);
//        }
//
//        List<String> years = populateByYear();
//        for (int i = 0; i < years.size(); i++) {
//            jComboBoxCoveredYear.addItem(years.get(i));
//        }
//    }
    private void populateByEmployeeNum() {
        Set<String> employeeSet = new HashSet<>();

        for (EmployeePayroll payroll : employees) {
            String id = payroll.getEmployeeNo();
            String firstName = payroll.getFirstName();
            String lastName = payroll.getLastName();
            String employeeInfo = id + "- " + lastName + ", " + firstName;
            employeeSet.add(employeeInfo);
        }

        List<String> employeeList = new ArrayList<>(employeeSet);
        Collections.sort(employeeList);

        jComboBoxEmployeeNumber.addItem("");
        for (String employeeInfo : employeeList) {
            jComboBoxEmployeeNumber.addItem(employeeInfo);
        }
    }

    private void populatecomboboxCoveredPeriods() {
        Set<String> coveredPeriods = new HashSet<>();

        for (EmployeePayroll payroll : employees) {
            String period = payroll.getStartDate() + " to " + payroll.getEndDate();
            coveredPeriods.add(period); // Avoid duplicates
        }

        List<String> sortedPeriods = new ArrayList<>(coveredPeriods);
        Collections.sort(sortedPeriods, (p1, p2) -> {
            LocalDate d1 = LocalDate.parse(p1.split(" to ")[0]);
            LocalDate d2 = LocalDate.parse(p2.split(" to ")[0]);
            return d2.compareTo(d1); // Sort from latest to oldest
        });

        jComboBoxCoveredPeriod.addItem("");
        for (String period : sortedPeriods) {
            jComboBoxCoveredPeriod.addItem(period);
        }
    }

//    
    public void filterByCategory(String coveredPeriod, String employeeNumber) {
    DefaultTableModel tableModel = (DefaultTableModel) jTablePayrollSummary.getModel();
    tableModel.setRowCount(0); // Clear existing rows

    String selectedPeriod = jComboBoxCoveredPeriod.getSelectedItem() != null
        ? jComboBoxCoveredPeriod.getSelectedItem().toString()
        : "";

    String startDate = "";
    String endDate = "";

    if (!selectedPeriod.isEmpty() && selectedPeriod.contains(" to ")) {
        String[] parts = selectedPeriod.split(" to ");
        if (parts.length == 2) {
            startDate = parts[0].trim();
            endDate = parts[1].trim();
        }
    }

    List<EmployeePayroll> filteredList = new ArrayList<>();

    for (EmployeePayroll payroll : employees) {
        boolean matches = true;

        // Only filter by employee number if provided
        if (employeeNumber != null && !employeeNumber.trim().isEmpty()) {
            matches = matches && payroll.getEmployeeNo().equalsIgnoreCase(employeeNumber);
        }

        // Only filter by date range if provided
        if (!startDate.isEmpty()) {
            matches = matches && payroll.getStartDate().equalsIgnoreCase(startDate);
        }

        if (!endDate.isEmpty()) {
            matches = matches && payroll.getEndDate().equalsIgnoreCase(endDate);
        }

        if (matches) {
            filteredList.add(payroll);
        }
    }

    // If no filters were applied, show all data
    if (selectedPeriod.isEmpty() && (employeeNumber == null || employeeNumber.trim().isEmpty())) {
        filteredList = employees;
    }

    // Populate the table
    for (EmployeePayroll payroll : filteredList) {
        tableModel.addRow(new Object[]{
            payroll.getEmployeeNo(),
            payroll.getLastName(),
            payroll.getFirstName(),
            payroll.getRegularHours(),
            payroll.getOvertimeHours(),
            payroll.getBasicSalary(),
            payroll.getHourlyRate(),
            payroll.getGrossIncome(),
            payroll.getSssDeduction(),
            payroll.getPhilHealthDeduction(),
            payroll.getPagibigDeduction(),
            payroll.getWithholdingTax(),
            payroll.getStartDate(),
            payroll.getEndDate(),
            payroll.getBenefits(),
            payroll.getTotalDeductions(),
            payroll.getTakeHomePay()
        });
    }
}

    public void onFilterAction() {
        String coveredPeriod = jComboBoxCoveredPeriod.getSelectedItem() != null ? jComboBoxCoveredPeriod.getSelectedItem().toString() : "";
        String employeeInfo = jComboBoxEmployeeNumber.getSelectedItem() != null ? jComboBoxEmployeeNumber.getSelectedItem().toString() : "";

        String[] employeeParts = employeeInfo.split("-");
        String employeeNumber = employeeParts.length > 0 ? employeeParts[0] : "";

        filterByCategory(coveredPeriod, employeeNumber);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePayrollSummary = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jComboBoxCoveredPeriod = new javax.swing.JComboBox<>();
        jComboBoxEmployeeNumber = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GILTENDEZ | OOP | A1102");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255, 0));

        jTablePayrollSummary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee No", "Last Name", "First Name", "Regular Hours", "Overtime Hours", "Basic Salary", "Hourly Rate", "Gross Income", "SSS Deduction", "Philthealth Deduction", "Pagibig Deduction", "Withholding Tax", "Start Date", "End Date", "Benefits", "Total Deductions", "TakeHome-Pay"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePayrollSummary.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(jTablePayrollSummary);
        if (jTablePayrollSummary.getColumnModel().getColumnCount() > 0) {
            jTablePayrollSummary.getColumnModel().getColumn(0).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(0).setPreferredWidth(70);
            jTablePayrollSummary.getColumnModel().getColumn(3).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(4).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(5).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(6).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(7).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(8).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(9).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(10).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(11).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(12).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(12).setPreferredWidth(100);
            jTablePayrollSummary.getColumnModel().getColumn(13).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(13).setPreferredWidth(100);
            jTablePayrollSummary.getColumnModel().getColumn(14).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(15).setResizable(false);
            jTablePayrollSummary.getColumnModel().getColumn(16).setResizable(false);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 600, 240));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBoxCoveredPeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.white));
        jComboBoxCoveredPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCoveredPeriodActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxCoveredPeriod, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 200, -1));

        jComboBoxEmployeeNumber.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.white));
        jComboBoxEmployeeNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEmployeeNumberActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxEmployeeNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 200, -1));

        jLabel1.setText("Covered Period");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        jLabel3.setText("Employee");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 13, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel6.setText("Filter by :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButtonClose.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonClose.setText("CLOSE");
        jButtonClose.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.white));
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 60, 70, 23));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 600, 90));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/photos/Payroll Summary.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxCoveredPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCoveredPeriodActionPerformed
        // TODO add your handling code here:
        onFilterAction();
    }//GEN-LAST:event_jComboBoxCoveredPeriodActionPerformed

    private void jComboBoxEmployeeNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEmployeeNumberActionPerformed
        // TODO add your handling code here:
        onFilterAction();
    }//GEN-LAST:event_jComboBoxEmployeeNumberActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        // TODO add your handling code here:
        setVisible(false);

    }//GEN-LAST:event_jButtonCloseActionPerformed

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
            java.util.logging.Logger.getLogger(PayrollSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayrollSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayrollSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayrollSummary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PayrollSummary().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(PayrollSummary.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JComboBox<String> jComboBoxCoveredPeriod;
    private javax.swing.JComboBox<String> jComboBoxEmployeeNumber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePayrollSummary;
    // End of variables declaration//GEN-END:variables

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.jpg")));
    }
}
