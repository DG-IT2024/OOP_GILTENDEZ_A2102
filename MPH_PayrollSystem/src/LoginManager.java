
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

public class LoginManager {

    private List<EmployeeLogin> employeeDetails = new ArrayList<>();
    private final int MAX_ATTEMPTS = 3;
    private Map<String, Integer> userAttempts = new HashMap<>();
    private final String CSV_FILE = "login_attempts.csv";
    private String role;
    private String userEmpNum;

    public LoginManager(String csvFilePath) throws IOException {
        loadEmployeeCredentials(csvFilePath);
        loadAttemptsFromCSV();

    }

    // loading db
    private void loadEmployeeCredentials(String csvFilePath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                employeeDetails.add(new EmployeeLogin(nextLine[0], nextLine[1], nextLine[2], nextLine[3]));
            }
        }
    }

    // load attempts db
    private void loadAttemptsFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                userAttempts.put(nextLine[0], Integer.parseInt(nextLine[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // save attempts db
    private void saveAllAttemptsToCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE))) {
            for (Map.Entry<String, Integer> entry : userAttempts.entrySet()) {
                writer.writeNext(new String[]{entry.getKey(), String.valueOf(entry.getValue())});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private boolean checkPassword(String enteredPassword, String storedHashedPassword) {
        return BCrypt.checkpw(enteredPassword, storedHashedPassword);
    }

    
    private EmployeeLogin findEmployee(String username) {
        for (EmployeeLogin employee : employeeDetails) {
            if (employee.getUsername().equalsIgnoreCase(username)) {
                role = employee.getRole();
                userEmpNum = employee.getEmployeeNumber();
                return employee;
            }
            
        }
        return null;
    }

    //check if user is blocked
    public boolean isBlocked(String username) {
        return userAttempts.getOrDefault(username, 0) == MAX_ATTEMPTS;
    }

    
    public boolean logIn(String username, String password) {
        int attempts = userAttempts.getOrDefault(username, 0);

        if (isBlocked(username)) {
            return false;
        }

        EmployeeLogin employee = findEmployee(username.trim());
        if (employee == null) {
            return false;
        }

        boolean authenticated = checkPassword(password, employee.getHashPassword());

        if (!authenticated) {
            userAttempts.put(username, ++attempts);
            saveAllAttemptsToCSV();
            return false;
        }

        userAttempts.put(username, 0);
        saveAllAttemptsToCSV();
        return true;
    }

        public void redirectToDashboard() throws IOException {
        switch (role.toLowerCase()) {
            case "admin":
                new EmployeeProfileAdmin().setVisible(true);
                System.out.print("admin");
                break;
            case "hr":

                break;
            case "finance":

                break;
            case "employeeuser":
                new EmployeeProfileUser(userEmpNum).setVisible(true);
                break;
            default:

                return;
        }

    }
}
