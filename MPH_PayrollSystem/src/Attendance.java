
import java.time.LocalTime;

/*
The program will calculate the payroll based on the number of hours worked by an employee. You will be asked to provide the employee number, and time IN and time Out. 

**Input Notes**
- Separate time entries with comma
- Write Time-In followed by Time-Out.
- Time should follow HH:mm format.
- Input time in 24-hour format

**Limitations**
- 12:00-13:00 (BreakTime). Not counted in computed worked hours
- Maximum regular paid hours is 8 hours
- Maximum worked days is 20days for one month.
- Hourly rate is the quotient of BasicSalary divided by the product of maximum regulars hours (8) and maximum worked days(20). 
- Gross Income (Pay for hours worked) is used to determine the SSS, PhilHealth, Pag-ibig deductions
- Overtime is only computed if employee works for more than 8 hours
- Overtime pay consideration.
Options:
  1. Don't credit overtime (rate set to 0)
  2. Set overtime pay rate(e.g. 1.25)
- Program computes for one month payroll.
- Work starts at 8:00AM
- Grace period of 10 mins. Considered late if Time-in 8:11.
- Only consider worked hours per day. Any fraction thereof is not considered in the payroll computation

**Features**
- Clear Console (limited to platforms that allow ANSI. Generates newline after ANSI)
- Continuous processing of payroll
- TimeSheet is generated after encoding all the Time-In/Time-Out for the month
- Computed regular and overtime worked hours are shown after each Time-In/Time-Out entry
- Allows setting of overtime rate per day
- Option to disregard overtime if it is not approved
- Calculate overtime hours
- Calculate regular hours
- Calculate SSS, Pag-ibig, Philhealth, and Withholding tax
- Calculate net salary based on worked hours, deductions, and benefits
- Generate payslip after getting the employee number and timesheet
 */
public class Attendance extends Timesheet {

    private int regularHours;
    private int overtimeHours;

    public Attendance(String employeeNumber,
            String date,
            String timeIn,
            String timeOut) {
        super(employeeNumber, date, timeIn, timeOut);
        calculateHours(); // Auto-compute hours when Attendance object is created
    }

    private void calculateHours() {
        int workedHours = calculateWorkedHours(getTimeIn(), getTimeOut());
        this.regularHours = Math.min(workedHours, 8); // Regular hours max at 8
        this.overtimeHours = Math.max(workedHours - 8, 0); // Overtime is beyond 8
    }

    public int getRegularHours() {
        return regularHours;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public static int calculateWorkedHours(String timeIn, String timeOut) {
        LocalTime parsedTimeIn = LocalTime.parse(timeIn);
        LocalTime parsedTimeOut = LocalTime.parse(timeOut);

        int gracePeriod = 10;
        LocalTime officeStart = LocalTime.of(8, 0);
        LocalTime targetTime = LocalTime.of(8, gracePeriod + 1);

        if (parsedTimeIn.isBefore(targetTime) && parsedTimeIn.getHour() == officeStart.getHour()) {
            parsedTimeIn = officeStart;
        }

        LocalTime breakStart = LocalTime.of(12, 0);
        LocalTime breakEnd = LocalTime.of(13, 0);

        if (parsedTimeIn.isBefore(breakStart) && parsedTimeOut.isBefore(LocalTime.of(12, 59))) {
            parsedTimeOut = breakStart;
        }
        if (parsedTimeIn.isAfter(LocalTime.of(11, 59)) && parsedTimeOut.isAfter(breakEnd)) {
            parsedTimeIn = breakEnd;
        }

        int workedMinutes = (parsedTimeOut.getHour() * 60 + parsedTimeOut.getMinute())
                - (parsedTimeIn.getHour() * 60 + parsedTimeIn.getMinute());

        int workedHour = workedMinutes / 60;

        int breakTime = 0;
        if (parsedTimeIn.isBefore(breakStart) && parsedTimeOut.isAfter(LocalTime.of(12, 59))
                || (parsedTimeIn.equals(breakStart) && parsedTimeOut.equals(breakEnd))) {
            breakTime = 1;
        }

        return workedHour - breakTime;
    }

}
