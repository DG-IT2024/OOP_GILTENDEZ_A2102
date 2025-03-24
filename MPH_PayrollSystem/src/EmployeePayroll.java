public class EmployeePayroll {
    private String employeeNo;
    private String lastName;
    private String firstName;
    private String regularHours;
    private String overtimeHours;
    private String basicSalary;
    private String hourlyRate;
    private String grossIncome;
    private String sssDeduction;
    private String philHealthDeduction;
    private String pagibigDeduction;
    private String withholdingTax;
    private String startDate;
    private String endDate;
    private String benefits;
    private String totalDeductions;
    private String takeHomePay;

    // Constructor
    public EmployeePayroll(String employeeNo, String lastName, String firstName,
                           String regularHours, String overtimeHours, String basicSalary,
                           String hourlyRate, String grossIncome, String sssDeduction,
                           String philHealthDeduction, String pagibigDeduction,
                           String withholdingTax, String startDate, String endDate,
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
        this.philHealthDeduction = philHealthDeduction;
        this.pagibigDeduction = pagibigDeduction;
        this.withholdingTax = withholdingTax;
        this.startDate = startDate;
        this.endDate = endDate;
        this.benefits = benefits;
        this.totalDeductions = totalDeductions;
        this.takeHomePay = takeHomePay;
    }

    // Getters and Setters
    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getRegularHours() {
        return regularHours;
    }

    public void setRegularHours(String regularHours) {
        this.regularHours = regularHours;
    }

    public String getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(String overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public String getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(String basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(String grossIncome) {
        this.grossIncome = grossIncome;
    }

    public String getSssDeduction() {
        return sssDeduction;
    }

    public void setSssDeduction(String sssDeduction) {
        this.sssDeduction = sssDeduction;
    }

    public String getPhilHealthDeduction() {
        return philHealthDeduction;
    }

    public void setPhilHealthDeduction(String philHealthDeduction) {
        this.philHealthDeduction = philHealthDeduction;
    }

    public String getPagibigDeduction() {
        return pagibigDeduction;
    }

    public void setPagibigDeduction(String pagibigDeduction) {
        this.pagibigDeduction = pagibigDeduction;
    }

    public String getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(String withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(String totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public String getTakeHomePay() {
        return takeHomePay;
    }

    public void setTakeHomePay(String takeHomePay) {
        this.takeHomePay = takeHomePay;
    }

    // toString() method
    @Override
    public String toString() {
        return "EmployeePayroll{" +
                "employeeNo='" + employeeNo + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", regularHours='" + regularHours + '\'' +
                ", overtimeHours='" + overtimeHours + '\'' +
                ", basicSalary='" + basicSalary + '\'' +
                ", hourlyRate='" + hourlyRate + '\'' +
                ", grossIncome='" + grossIncome + '\'' +
                ", sssDeduction='" + sssDeduction + '\'' +
                ", philHealthDeduction='" + philHealthDeduction + '\'' +
                ", pagibigDeduction='" + pagibigDeduction + '\'' +
                ", withholdingTax='" + withholdingTax + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", benefits='" + benefits + '\'' +
                ", totalDeductions='" + totalDeductions + '\'' +
                ", takeHomePay='" + takeHomePay + '\'' +
                '}';
    }
}
