
public class Timesheet {

    private String employeeNumber;
    private String date;
    private String timeIn;
    private String timeOut;

    public Timesheet(String employeeNumber, String date, String timeIn, String timeOut) {
        this.employeeNumber = employeeNumber;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
 
    }
   
    // Getter and Setter for timeIN
     public String getEmployeeNumber() {
        return employeeNumber;
    }
    
      public String getDate() {
        return date;
    }
     
    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    // Getter and Setter for timeOUT
    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }
}
