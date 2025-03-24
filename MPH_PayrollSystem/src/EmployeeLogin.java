public class EmployeeLogin {
    // Attributes
    private String username;
    private String hashPassword;
    private String employeeNumber;
    private String role;
    

    // Constructor
    public EmployeeLogin(String employeeNumber, String username, String hashPassword, String role) {
        this.employeeNumber = employeeNumber;
        this.username = username;
        this.hashPassword = hashPassword;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }
   
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    
         public String getRole() {
        return role;
    }
          
     public String getHashPassword() {
     return hashPassword;
    }

   
}
