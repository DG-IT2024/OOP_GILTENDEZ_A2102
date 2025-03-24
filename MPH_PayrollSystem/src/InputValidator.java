
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danilo
 */
// Utility Class
public class InputValidator {
    
    public static void allowOnlyDigits(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }

    // Allow only digits with max length
    public static void allowOnlyDigits(KeyEvent evt, JTextField textField, int maxLength) {
        char c = evt.getKeyChar();
        int currentLength = textField.getText().length();

        // Allow backspace (8) and delete (127)
        if (!Character.isDigit(c) && c != '\b' && c != '\u007F') {
            evt.consume();
        }

        // Block input if max length is reached
        if (currentLength >= maxLength && c != '\b' && c != '\u007F') {
            evt.consume();
        }
    }

    public static void allowOnlyDate(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '/' && c != '-' && c != '.') {
            evt.consume();
        }
    }

    public static void allowOnlyDigitsSpecial(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '-') {
            evt.consume();
        }
    }

// Allow only digits and special characters (-) with max length
    public static void allowOnlyDigitsSpecial(KeyEvent evt, JTextField textField, int maxLength) {
        char c = evt.getKeyChar();
        if ((!Character.isDigit(c) && c != '-') || textField.getText().length() >= maxLength) {
            evt.consume(); // Prevent input
        }
    }

    // Allow only alphabets (A-Z, a-z)
    public void allowOnlyAlphabets(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c)) {
            evt.consume();
        }
    }

    public static void allowValidNameCharacters(KeyEvent evt) {
        char c = evt.getKeyChar();

        // Allow letters, space, hyphen, apostrophe, backspace, and delete
        if (!Character.isLetter(c) && c != ' ' && c != '-' && c != '\'' && c != '\b' && c != '\u007F') {
            evt.consume(); // Block invalid input
        }
    }

    // Allow only alphabets (A-Z, a-z)
    public void allowOnlyAlphabets(KeyEvent evt, JTextField textField, int maxLength) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) || textField.getText().length() >= maxLength) {
            evt.consume(); 
        }
    }
}
