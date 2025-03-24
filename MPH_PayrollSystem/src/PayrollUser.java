
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import java.awt.Toolkit;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danilo
 */
public class PayrollUser extends javax.swing.JFrame {

    private String userEmployeeNumber;
    private String userLastName;
    private String userFirstName;
    
    

    public List<PayrollUserInformation> employees= new ArrayList<>();
    private static String[] userInformation;

    /**
     * Creates new form PayrollProcessing
     *
     * @throws java.io.FileNotFoundException
     */
//     public PayrollUser(String[] userPayroll) throws FileNotFoundException, IOException {
    public PayrollUser(String[] userInformation) throws FileNotFoundException, IOException {
        this.userEmployeeNumber = userInformation[0];
        this.userLastName = userInformation[1];
        this.userFirstName = userInformation[2];

        initComponents();
        String csvFile = "PayrollRecords.csv";
       csvRun(csvFile);
        populatecomboboxCoveredPeriods();
        showEmployeeInformation();
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

    private void setIconImage() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.jpg")));
    }

    public class PayrollUserInformation {

        private String employeeNo;
        private String lastName;
        private String firstName;
        private String regularHours;
        private String overtimeHours;
        private String basicSalary;
        private String hourlyRate;
        private String grossIncome;
        private String sssDeduction;
        private String philhealthDeduction;
        private String pagibigDeduction;
        private String withholdingTax;
        private String startDate;
        private String endDate;
        private String benefits;
        private String totalDeductions;
        private String takeHomePay;

        public PayrollUserInformation(
                String employeeNo, String lastName, String firstName,
                String regularHours, String overtimeHours, String basicSalary, String hourlyRate,
                String grossIncome, String sssDeduction, String philhealthDeduction,
                String pagibigDeduction, String withholdingTax, String startDate, String endDate,
                String benefits, String totalDeductions, String takeHomePay) {

            this.employeeNo = employeeNo;
            this.lastName = lastName;
            this.firstName = firstName;
            this.regularHours = regularHours;
            this.overtimeHours = overtimeHours;
            this.basicSalary = basicSalary;
            this.hourlyRate = hourlyRate;
            this.grossIncome = grossIncome;
            this.sssDeduction = sssDeduction;
            this.philhealthDeduction = philhealthDeduction;
            this.pagibigDeduction = pagibigDeduction;
            this.withholdingTax = withholdingTax;
            this.startDate = startDate;
            this.endDate = endDate;
            this.benefits = benefits;
            this.totalDeductions = totalDeductions;
            this.takeHomePay = takeHomePay;
        }

        // Getters for all fields
        public String getEmployeeNo() {
            return employeeNo;
        }

        public String getLastName() {
            return lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getRegularHours() {
            return regularHours;
        }

        public String getOvertimeHours() {
            return overtimeHours;
        }

        public String getBasicSalary() {
            return basicSalary;
        }

        public String getHourlyRate() {
            return hourlyRate;
        }

        public String getGrossIncome() {
            return grossIncome;
        }

        public String getSssDeduction() {
            return sssDeduction;
        }

        public String getPhilhealthDeduction() {
            return philhealthDeduction;
        }

        public String getPagibigDeduction() {
            return pagibigDeduction;
        }

        public String getWithholdingTax() {
            return withholdingTax;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getBenefits() {
            return benefits;
        }

        public String getTotalDeductions() {
            return totalDeductions;
        }

        public String getTakeHomePay() {
            return takeHomePay;
        }
    }

    public void parseRecords(List<String[]> records) {
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
            String philhealthDeduction = record[9];
            String pagibigDeduction = record[10];
            String withholdingTax = record[11];
            String startDate = record[12];
            String endDate = record[13];
            String benefits = record[14];
            String totalDeductions = record[15];
            String takeHomePay = record[16];

            PayrollUserInformation payrollUserInfo = new PayrollUserInformation(
                    employeeNo, lastName, firstName, regularHours, overtimeHours, basicSalary,
                    hourlyRate, grossIncome, sssDeduction, philhealthDeduction, pagibigDeduction,
                    withholdingTax, startDate, endDate, benefits, totalDeductions, takeHomePay
            );

            employees.add(payrollUserInfo);
        }
    }

    private void csvRun(String csvFile) throws FileNotFoundException, IOException {
        employees.clear();  // Clear existing entries before reading new data

        Filehandling filehandler = new Filehandling(csvFile);
        List<String[]> records = filehandler.readCSV();

        //List<EmployeePayroll> employees = parseRecords(records);
        parseRecords(records);
      
    }

    private void populatecomboboxCoveredPeriods() {
        Set<String> coveredPeriods = new HashSet<>();

        for (PayrollUserInformation payroll : employees) {
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

    public static boolean isPayrollRecordsCsvEmpty(String csvFile) {

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine = csvReader.readNext();

            // If nextLine is null, the file is empty
            return nextLine == null; // check if statement is true
        } catch (IOException e) {

            return true; // return boolean true if csvFile can't be found in the directory. 
        }
    }

    public Integer determineIndex() {
        String searchId = jTextFieldEmployeeNum.getText();

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

        for (int i = 0; i < employees.size(); i++) {
            PayrollUserInformation payrollUserInfo = employees.get(i);
            if (payrollUserInfo.getEmployeeNo().equals(searchId)
                    && payrollUserInfo.getStartDate().equals(startDate)
                    && payrollUserInfo.getEndDate().equals(endDate)) {

                return i;
            }
        }
        clearField();
        JOptionPane.showMessageDialog(null, "No record for the selected covered period");

        return -1; // Return -1 if no match is found
    }

    public void showEmployeeInformation() {
        jTextFieldEmployeeNum.setText(getEmployeeNumber());
        jTextFieldLastName.setText(getLastName());
        jTextFieldFirstName.setText(getFirstName());
    }

    public void showUserPayroll() {

        String basis = jTextFieldEmployeeNum.getText();
        for (PayrollUserInformation employee : employees) {
            if (employee.getEmployeeNo().equals(basis)) {
                jTextFieldRegularHours.setText(employee.getRegularHours());
                jTextFieldOvertimeHours.setText(employee.getOvertimeHours());
                jTextFieldBasicSalary.setText(employee.getBasicSalary());
                jTextFieldHourlyRate.setText(employee.getHourlyRate());
                jTextFieldGrossIncome.setText(employee.getGrossIncome());
                jTextSssDeduction.setText(employee.getSssDeduction());
                jTextFieldPhilHealthDeduction.setText(employee.getPhilhealthDeduction());
                jTextFieldPagibigDeduction.setText(employee.getPagibigDeduction());
                jTextFieldWHTax.setText(employee.getWithholdingTax());
                jTextFieldGrossIncome_S.setText(employee.getGrossIncome());
                jTextFieldBenefits.setText(employee.getBenefits());
                jTextFieldTotalDeductions.setText(employee.getTotalDeductions());
                jTextFieldTakeHomePay.setText(employee.getTakeHomePay());
            }
        }
    }

    public String[] sendInformation() {
        String[] userInformation = new String[3];;

        userInformation[0] = jTextFieldEmployeeNum.getText();
        userInformation[1] = jTextFieldLastName.getText();
        userInformation[2] = jTextFieldFirstName.getText();

        return userInformation;

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

    public void clearField() {

        jTextFieldRegularHours.setText("");
        jTextFieldOvertimeHours.setText("");
        jTextFieldBasicSalary.setText("");
        jTextFieldHourlyRate.setText("");
        jTextFieldGrossIncome.setText("");
        jTextSssDeduction.setText("");
        jTextFieldPhilHealthDeduction.setText("");
        jTextFieldPagibigDeduction.setText("");
        jTextFieldWHTax.setText("");
        jTextFieldGrossIncome_S.setText("");
        jTextFieldBenefits.setText("");
        jTextFieldTotalDeductions.setText("");
        jTextFieldTakeHomePay.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldPagibigDeduction = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldWHTax = new javax.swing.JTextField();
        jTextFieldEmployeeNum = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldLastName = new javax.swing.JTextField();
        jTextFieldGrossIncome_S = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldFirstName = new javax.swing.JTextField();
        jTextFieldBenefits = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButtonView = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldGrossIncome = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextSssDeduction = new javax.swing.JTextField();
        jTextFieldPhilHealthDeduction = new javax.swing.JTextField();
        jTextFieldTotalDeductions = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldTakeHomePay = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldHourlyRate = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldBasicSalary = new javax.swing.JTextField();
        jComboBoxCoveredPeriod = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jTextFieldRegularHours = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jTextFieldOvertimeHours = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButtonProfile = new javax.swing.JButton();
        jButtonLeaveApp = new javax.swing.JButton();
        jButtonPayroll = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jButtonPayroll1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jLabel21.setText("Month");

        jLabel22.setText("Month");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GILTENDEZ | OOP | A1102");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255, 0));
        jPanel1.setMinimumSize(new java.awt.Dimension(617, 432));
        jPanel1.setPreferredSize(new java.awt.Dimension(617, 432));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setText("PhilHealth Deduction ");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 333, -1, -1));

        jLabel11.setText("Pagibig Deduction ");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 373, -1, -1));

        jTextFieldPagibigDeduction.setEditable(false);
        jTextFieldPagibigDeduction.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldPagibigDeduction.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldPagibigDeduction.setEnabled(false);
        jTextFieldPagibigDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPagibigDeductionActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldPagibigDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 370, 170, 22));

        jLabel12.setText("Withholding Tax");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 413, -1, -1));

        jLabel3.setText("Employee No.");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 13, -1, -1));

        jTextFieldWHTax.setEditable(false);
        jTextFieldWHTax.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldWHTax.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldWHTax.setEnabled(false);
        jTextFieldWHTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldWHTaxActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldWHTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 410, 170, 22));

        jTextFieldEmployeeNum.setEditable(false);
        jTextFieldEmployeeNum.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
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
        jPanel1.add(jTextFieldEmployeeNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 170, 22));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel13.setText("Summary");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 213, -1, -1));

        jLabel4.setText("Last Name");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 53, -1, -1));

        jLabel14.setText("Gross Income");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 253, -1, -1));

        jTextFieldLastName.setEditable(false);
        jTextFieldLastName.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldLastName.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldLastName.setEnabled(false);
        jTextFieldLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLastNameActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 170, 22));

        jTextFieldGrossIncome_S.setEditable(false);
        jTextFieldGrossIncome_S.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldGrossIncome_S.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldGrossIncome_S.setEnabled(false);
        jTextFieldGrossIncome_S.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldGrossIncome_SActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldGrossIncome_S, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 250, 112, 22));

        jLabel5.setText("First Name");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 93, -1, -1));

        jLabel15.setText("Benefits");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 293, -1, -1));

        jTextFieldFirstName.setEditable(false);
        jTextFieldFirstName.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldFirstName.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldFirstName.setEnabled(false);
        jTextFieldFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFirstNameActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 170, 22));

        jTextFieldBenefits.setEditable(false);
        jTextFieldBenefits.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldBenefits.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldBenefits.setEnabled(false);
        jTextFieldBenefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBenefitsActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldBenefits, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 290, 112, 22));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel6.setText("Covered Period");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 10, -1, -1));

        jLabel7.setText("Worked Hours");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 133, -1, -1));

        jButtonView.setText("View");
        jButtonView.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.white));
        jButtonView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonView, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 70, 79, 23));

        jLabel8.setText("Gross Income");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 253, -1, -1));

        jTextFieldGrossIncome.setEditable(false);
        jTextFieldGrossIncome.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldGrossIncome.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldGrossIncome.setEnabled(false);
        jTextFieldGrossIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldGrossIncomeActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldGrossIncome, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 170, 22));

        jLabel9.setText("SSS Deduction ");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 293, -1, -1));

        jTextSssDeduction.setEditable(false);
        jTextSssDeduction.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextSssDeduction.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextSssDeduction.setEnabled(false);
        jTextSssDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextSssDeductionActionPerformed(evt);
            }
        });
        jPanel1.add(jTextSssDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 170, 22));

        jTextFieldPhilHealthDeduction.setEditable(false);
        jTextFieldPhilHealthDeduction.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldPhilHealthDeduction.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldPhilHealthDeduction.setEnabled(false);
        jTextFieldPhilHealthDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPhilHealthDeductionActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldPhilHealthDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 330, 170, 22));

        jTextFieldTotalDeductions.setEditable(false);
        jTextFieldTotalDeductions.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldTotalDeductions.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldTotalDeductions.setEnabled(false);
        jTextFieldTotalDeductions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTotalDeductionsActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldTotalDeductions, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 330, 112, 22));

        jLabel16.setText("Total Deductions");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 333, -1, -1));

        jTextFieldTakeHomePay.setEditable(false);
        jTextFieldTakeHomePay.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldTakeHomePay.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldTakeHomePay.setEnabled(false);
        jTextFieldTakeHomePay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTakeHomePayActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldTakeHomePay, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 370, 112, 22));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("TAKE-HOME PAY");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 373, -1, -1));

        jTextFieldHourlyRate.setEditable(false);
        jTextFieldHourlyRate.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldHourlyRate.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldHourlyRate.setEnabled(false);
        jTextFieldHourlyRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldHourlyRateActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldHourlyRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 170, 22));

        jLabel18.setText("Basic Salary");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 173, -1, -1));

        jLabel19.setText("Hourly Rate");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 213, -1, -1));

        jTextFieldBasicSalary.setEditable(false);
        jTextFieldBasicSalary.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldBasicSalary.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldBasicSalary.setEnabled(false);
        jTextFieldBasicSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBasicSalaryActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldBasicSalary, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 170, 22));

        jComboBoxCoveredPeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.white));
        jComboBoxCoveredPeriod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxCoveredPeriodMouseClicked(evt);
            }
        });
        jComboBoxCoveredPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCoveredPeriodActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBoxCoveredPeriod, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, 220, -1));

        jLabel26.setText("Regular");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, -1, -1));

        jTextFieldRegularHours.setEditable(false);
        jTextFieldRegularHours.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldRegularHours.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldRegularHours.setEnabled(false);
        jTextFieldRegularHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRegularHoursActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldRegularHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 70, 22));

        jLabel25.setText("Overtime");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 120, -1, -1));

        jTextFieldOvertimeHours.setEditable(false);
        jTextFieldOvertimeHours.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, java.awt.Color.gray));
        jTextFieldOvertimeHours.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        jTextFieldOvertimeHours.setEnabled(false);
        jTextFieldOvertimeHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldOvertimeHoursActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldOvertimeHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 70, 22));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 630, 450));

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

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 150, 220));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/photos/Payroll.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 530));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldEmployeeNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEmployeeNumActionPerformed

    private void jTextFieldEmployeeNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEmployeeNumKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextFieldEmployeeNumKeyTyped

    private void jTextFieldLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLastNameActionPerformed

    private void jTextFieldFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFirstNameActionPerformed

    private void jButtonViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewActionPerformed
        // TODO add your handling code here:
        int index = determineIndex();
        if (index != -1) {
            showUserPayroll();
        }


    }//GEN-LAST:event_jButtonViewActionPerformed

    private void jTextFieldGrossIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldGrossIncomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldGrossIncomeActionPerformed

    private void jTextSssDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextSssDeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextSssDeductionActionPerformed

    private void jTextFieldPhilHealthDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPhilHealthDeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPhilHealthDeductionActionPerformed

    private void jTextFieldPagibigDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPagibigDeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPagibigDeductionActionPerformed

    private void jTextFieldWHTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldWHTaxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldWHTaxActionPerformed

    private void jTextFieldGrossIncome_SActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldGrossIncome_SActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldGrossIncome_SActionPerformed

    private void jTextFieldBenefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBenefitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBenefitsActionPerformed

    private void jTextFieldTotalDeductionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTotalDeductionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTotalDeductionsActionPerformed

    private void jTextFieldTakeHomePayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTakeHomePayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTakeHomePayActionPerformed

    private void jTextFieldHourlyRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldHourlyRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldHourlyRateActionPerformed

    private void jTextFieldBasicSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBasicSalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBasicSalaryActionPerformed

    private void jComboBoxCoveredPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCoveredPeriodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxCoveredPeriodActionPerformed

    private void jComboBoxCoveredPeriodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxCoveredPeriodMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jComboBoxCoveredPeriodMouseClicked

    private void jButtonProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProfileActionPerformed
        // TODO add your handling code here:
        openUserProfile();
    }//GEN-LAST:event_jButtonProfileActionPerformed

    private void jButtonLeaveAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLeaveAppActionPerformed
        openUserLeave();
    }//GEN-LAST:event_jButtonLeaveAppActionPerformed

    private void jButtonPayrolljButtonAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPayrolljButtonAttendanceActionPerformed
        openUserAttendance();
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

    }//GEN-LAST:event_jButtonPayroll1ActionPerformed

    private void jTextFieldRegularHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRegularHoursActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextFieldRegularHoursActionPerformed

    private void jTextFieldOvertimeHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldOvertimeHoursActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldOvertimeHoursActionPerformed

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
            java.util.logging.Logger.getLogger(PayrollUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayrollUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayrollUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayrollUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PayrollUser(userInformation).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(PayrollUser.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JComboBox<String> jComboBoxCoveredPeriod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    public javax.swing.JTextField jTextFieldBasicSalary;
    public javax.swing.JTextField jTextFieldBenefits;
    private javax.swing.JTextField jTextFieldEmployeeNum;
    public javax.swing.JTextField jTextFieldFirstName;
    public javax.swing.JTextField jTextFieldGrossIncome;
    public javax.swing.JTextField jTextFieldGrossIncome_S;
    public javax.swing.JTextField jTextFieldHourlyRate;
    public javax.swing.JTextField jTextFieldLastName;
    public javax.swing.JTextField jTextFieldOvertimeHours;
    public javax.swing.JTextField jTextFieldPagibigDeduction;
    public javax.swing.JTextField jTextFieldPhilHealthDeduction;
    public javax.swing.JTextField jTextFieldRegularHours;
    public javax.swing.JTextField jTextFieldTakeHomePay;
    public javax.swing.JTextField jTextFieldTotalDeductions;
    public javax.swing.JTextField jTextFieldWHTax;
    public javax.swing.JTextField jTextSssDeduction;
    // End of variables declaration//GEN-END:variables
}
