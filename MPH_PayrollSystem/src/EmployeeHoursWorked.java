
public class EmployeeHoursWorked {

    // New Attributes
    private String empNum;
    private String generatedDateTime;
    private String startDate;
    private String endDate;
    private String regularHours;
    private String overtimeHours;

    // New Constructor
    public EmployeeHoursWorked(String empNum, String generatedDateTime, String startDate, String endDate, String regularHours, String overtimeHours) {
        this.empNum = empNum;
        this.startDate = generatedDateTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regularHours = regularHours;
        this.overtimeHours = overtimeHours;
    }

    // Getters and Setters
    public String getEmployeeNumber() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
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

    @Override
    public String toString() {
        return "EmployeeHoursWorked{"
                + "empNum='" + empNum + '\''
                + ",generatedDateTime'" + generatedDateTime + '\''
                + ", startDate='" + startDate + '\''
                + ", endDate='" + endDate + '\''
                + ", regularHours='" + regularHours + '\''
                + ", overtimeHours='" + overtimeHours + '\''
                + '}';
    }
}
