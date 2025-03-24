
import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;

public class AttendanceUser extends javax.swing.JFrame {

    private final String csvFile = "Formatted_Timesheet.csv";
    private String userEmployeeNumber;
    private String userLastName;
    private String userFirstName;
    private static String[] userInformation;

    public AttendanceUser(String[] userInformation) throws FileNotFoundException, IOException {
        initComponents();

        this.userEmployeeNumber = userInformation[0];
        this.userLastName = userInformation[1];
        this.userFirstName = userInformation[2];

        jTextFieldEmployeeNum.setText(userEmployeeNumber);
        jTextFieldName.setText(getuserFullName());
        setIconImage();

    }

    public String getEmployeeNumber() {
        return userEmployeeNumber;
    }

    public String getLastName() {
        return userLastName;
    }

    public String getFirstName() {
        return userFirstName;
    }

    public String getuserFullName() {
        String userFullName = getLastName() + "," + getFirstName();
        return userFullName;
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

    public void openUserProfile() {

        try {
            setVisible(false);
            EmployeeProfileUser profileUser = new EmployeeProfileUser(getEmployeeNumber());
            profileUser.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(LeaveApplicationUser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void openUserAttendance() {

        try {
            setVisible(false);
            String[] employeeInformation = sendInformation();
            AttendanceUser AttendanceUser = new AttendanceUser(employeeInformation);
            AttendanceUser.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(LeaveApplicationUser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void openUserLeave() {
        try {
            // TODO add your handling code here:
            String[] employeeInformation = sendInformation();
            LeaveApplicationUser leaveUser = new LeaveApplicationUser(employeeInformation);
            setVisible(false);
            leaveUser.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeProfileUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openUserPayroll() {
        try {

            setVisible(false);
            String[] employeeInformation = sendInformation();
            PayrollUser payrollUser = new PayrollUser(employeeInformation);
            payrollUser.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(EmployeeProfileUser.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public void displayFilteredTimesheets(List<Attendance> timesheets) {
        DefaultTableModel tableModel = (DefaultTableModel) jTableTimeSheet.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        for (Attendance timesheet : timesheets) {
            tableModel.addRow(new Object[]{
                timesheet.getDate(),
                timesheet.getTimeIn(),
                timesheet.getTimeOut(),
                timesheet.getRegularHours(), // Now directly fetched
                timesheet.getOvertimeHours() // Now directly fetched
            });
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

    public String[] sendInformation() {
        String[] userInformation = new String[3];;

        userInformation[0] = getEmployeeNumber();
        userInformation[1] = getLastName();
        userInformation[2] = getFirstName();

        return userInformation;

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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTableTimeSheet = new javax.swing.JTable();
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
        jPanel4 = new javax.swing.JPanel();
        jButtonProfile = new javax.swing.JButton();
        jButtonLeaveApp = new javax.swing.JButton();
        jButtonPayroll = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jButtonPayroll1 = new javax.swing.JButton();
        jDateChooserStartDate = new com.toedter.calendar.JDateChooser();
        jDateChooserEndDate = new com.toedter.calendar.JDateChooser();
        jButtonView = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GILTENDEZ | OOP |  A2102");
        setAutoRequestFocus(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255, 0));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        // Now set column widths AFTER the table is initialized
        jTableTimeSheet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Time-In", "Time-Out", "Regular Worked Hours", "Overtime"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableTimeSheet.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableTimeSheet.setAutoscrolls(false);
        jTableTimeSheet.setEnabled(false);
        jTableTimeSheet.setMinimumSize(new java.awt.Dimension(380, 0));
        jTableTimeSheet.getTableHeader().setReorderingAllowed(false);
        jTableTimeSheet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableTimeSheetMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableTimeSheet);
        if (jTableTimeSheet.getColumnModel().getColumnCount() > 0) {
            jTableTimeSheet.getColumnModel().getColumn(0).setMinWidth(50);
            jTableTimeSheet.getColumnModel().getColumn(0).setPreferredWidth(3);
            jTableTimeSheet.getColumnModel().getColumn(0).setHeaderValue("Date");
            jTableTimeSheet.getColumnModel().getColumn(1).setHeaderValue("Time-In");
            jTableTimeSheet.getColumnModel().getColumn(2).setHeaderValue("Time-Out");
            jTableTimeSheet.getColumnModel().getColumn(3).setMinWidth(100);
            jTableTimeSheet.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTableTimeSheet.getColumnModel().getColumn(3).setHeaderValue("Regular Worked Hours");
            jTableTimeSheet.getColumnModel().getColumn(4).setHeaderValue("Overtime");
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, 390, 430));

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
        getContentPane().add(jTextFieldName, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 170, 22));

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
        getContentPane().add(jTextFieldEmployeeNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 170, 22));

        jLabel6.setText("Period Start Date");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, -1, -1));

        jLabel5.setText("Period End Date");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("SUMMARY");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 200, -1, -1));

        jLabel7.setText("Overtime Hours");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 270, -1, -1));

        jLabel9.setText("Regular Hours");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 240, -1, -1));

        jTextFieldRegularHours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldRegularHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRegularHoursActionPerformed(evt);
            }
        });
        getContentPane().add(jTextFieldRegularHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 230, 50, -1));

        jTextFieldOvertimeHours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(jTextFieldOvertimeHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 260, 50, -1));

        jPanel4.setBackground(new java.awt.Color(222, 194, 110));

        jButtonProfile.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButtonProfile.setText("PROFILE");
        jButtonProfile.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProfileActionPerformed(evt);
            }
        });

        jButtonLeaveApp.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButtonLeaveApp.setText("LEAVE ");
        jButtonLeaveApp.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonLeaveApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLeaveAppActionPerformed(evt);
            }
        });

        jButtonPayroll.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButtonPayroll.setText("ATTENDANCE");
        jButtonPayroll.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonPayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPayrolljButtonAttendanceActionPerformed(evt);
            }
        });

        jButtonExit.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButtonExit.setText("Exit");
        jButtonExit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jButtonPayroll1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jButtonPayroll1.setText("PAYROLL");
        jButtonPayroll1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonPayroll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPayroll1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonPayroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonPayroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonProfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonLeaveApp, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButtonProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jButtonPayroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonLeaveApp, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButtonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 150, 220));

        jDateChooserStartDate.setBackground(new java.awt.Color(255, 255, 255));
        jDateChooserStartDate.setToolTipText("");
        jDateChooserStartDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jDateChooserStartDateKeyTyped(evt);
            }
        });
        getContentPane().add(jDateChooserStartDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, 160, 30));

        jDateChooserEndDate.setBackground(new java.awt.Color(255, 255, 255));
        jDateChooserEndDate.setToolTipText("");
        jDateChooserEndDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jDateChooserEndDateKeyTyped(evt);
            }
        });
        getContentPane().add(jDateChooserEndDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 110, 160, 30));

        jButtonView.setText("View");
        jButtonView.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.white));
        jButtonView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonView, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 150, 79, 23));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/photos/Attendance.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 620));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void jTableTimeSheetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableTimeSheetMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_jTableTimeSheetMouseClicked


    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane2MouseClicked

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

    private void jButtonProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProfileActionPerformed
        // TODO add your handling code here:
        openUserProfile();
    }//GEN-LAST:event_jButtonProfileActionPerformed

    private void jButtonLeaveAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLeaveAppActionPerformed
        openUserLeave();
    }//GEN-LAST:event_jButtonLeaveAppActionPerformed

    private void jButtonPayrolljButtonAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPayrolljButtonAttendanceActionPerformed

    }//GEN-LAST:event_jButtonPayrolljButtonAttendanceActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        // TODO add your handling code here:

        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // Check the user's response
        if (response == JOptionPane.YES_OPTION) {
            try {
                // Hide the current window
                setVisible(false);

                // Show the login manager window
                new LoginManagerForm().setVisible(true);
            } catch (IOException ex) {
                // Log the exception if there is an IOException
                Logger.getLogger(EmployeeProfile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jButtonPayroll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPayroll1ActionPerformed
        openUserPayroll();
    }//GEN-LAST:event_jButtonPayroll1ActionPerformed

    private void jDateChooserStartDateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooserStartDateKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooserStartDateKeyTyped

    private void jDateChooserEndDateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooserEndDateKeyTyped
        // TODO add your handling code here:
        jDateChooserEndDate.getDateEditor().getUiComponent().setEnabled(false);
    }//GEN-LAST:event_jDateChooserEndDateKeyTyped

    private void jButtonViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewActionPerformed

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


    }//GEN-LAST:event_jButtonViewActionPerformed

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
                    new AttendanceUser(userInformation).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(AttendanceAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonLeaveApp;
    private javax.swing.JButton jButtonPayroll;
    private javax.swing.JButton jButtonPayroll1;
    private javax.swing.JButton jButtonProfile;
    private javax.swing.JButton jButtonView;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableTimeSheet;
    private javax.swing.JTextField jTextFieldEmployeeNum;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldOvertimeHours;
    private javax.swing.JTextField jTextFieldRegularHours;
    // End of variables declaration//GEN-END:variables

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.jpg")));
    }

}
