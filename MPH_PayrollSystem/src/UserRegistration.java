
import org.mindrot.jbcrypt.BCrypt;

public class UserRegistration {

    // Hash the password before storing in the database
    public static String hashPassword(String password) {
        int workload = 12; // Cost factor for bcrypt (higher = more secure)
        return BCrypt.hashpw(password, BCrypt.gensalt(workload));
    }

    public static boolean authenticateUser(String enteredPassword, String storedHashedPassword) {
        return BCrypt.checkpw(enteredPassword, storedHashedPassword);
    }

    public static void main(String[] args) {
        String enteredPassword = "A4CeHGFsz"; // User's chosen password
        String storedHashedPassword = "$2a$12$aoBFRjRUkyhoqY2H68f7oOnZGo6AWD/.rTTvN4R6NILWwaB4qDwYG";

        if (BCrypt.checkpw(enteredPassword, storedHashedPassword)) {
            System.out.print("matched");
        }
        

    }
}
