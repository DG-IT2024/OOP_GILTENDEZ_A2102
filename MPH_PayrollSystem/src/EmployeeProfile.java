
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class EmployeeProfile {
    private List<Employee> employees;
    private final String csvFile = "MotorPHEmployeeData.csv";
    private Filehandling filehandler;
    
   
    public EmployeeProfile() {
        employees = new ArrayList<>();
        filehandler = new Filehandling(csvFile);
    }
    
    
    public void loadEmployeeData() {
        try {
            List<String[]> records = filehandler.readCSV();
            employees = parseRecords(records);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  
   private static List<Employee> parseRecords(List<String[]> records) {
        List<Employee> employees = new ArrayList<>();
        for (String[] record : records) {
            String employeeNumber = record[0];
            String lastName = record[1];
            String firstName = record[2];
            String employeeBirthday = record[3];
            String address = record[4];
            String phoneNumber = record[5];
            String sssNumber = record[6];
            String philHealthNumber = record[7];
            String tinNumber = record[8];
            String pagIbigNumber = record[9];
            String status = record[10];
            String position = record[11];
            String immediateSupervisor = record[12];
            String basicSalary = record[13];
            String riceSubsidy = record[14];
            String phoneAllowance = record[15];
            String clothingAllowance = record[16];

            Employee employee = new Employee(employeeNumber, lastName, firstName, employeeBirthday, address, phoneNumber, sssNumber, philHealthNumber, tinNumber, pagIbigNumber, status, position, immediateSupervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance);
            employees.add(employee);
        }

        return employees;
    }
    
    
    public void populateTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                employee.getEmployeeNumber(), employee.getLastName(),
                employee.getFirstName(), employee.getEmployeeBirthday(),
                employee.getAddress(), employee.getPhoneNumber(),
                employee.getSssNumber(), employee.getPhilHealthNumber(),
                employee.getTinNumber(), employee.getPagIbigNumber(),
                employee.getStatus(), employee.getPosition(),
                employee.getImmediateSupervisor(), employee.getBasicSalary(),
                employee.getRiceSubsidy(), employee.getPhoneAllowance(),
                employee.getClothingAllowance()
            });
        }
    }
    
   
    public boolean addEmployee(Employee employee) {
        if (!isUniqueEmployeeId(employee.getEmployeeNumber())) {
            return false;
        }
        employees.add(employee);
        return true;
    }
    
    
    private boolean isUniqueEmployeeId(String employeeId) {
        return employees.stream().noneMatch(emp -> emp.getEmployeeNumber().equals(employeeId));
    }
    
    // Update Employee Information (Encapsulation & Polymorphism)
    public boolean updateEmployee(Employee updatedEmployee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeNumber().equals(updatedEmployee.getEmployeeNumber())) {
                employees.set(i, updatedEmployee);
                return true;
            }
        }
        return false;
    }
    
    // Delete Employee (Encapsulation)
    public boolean deleteEmployee(String employeeId) {
        return employees.removeIf(emp -> emp.getEmployeeNumber().equals(employeeId));
    }
    
    // Generate Unique ID (Encapsulation & Abstraction)
    public int generateUniqueId() {
        return employees.size() + 1;
    }
}
