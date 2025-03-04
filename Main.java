import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Main extends JFrame {
    private static final String CORRECT_PASSWORD = "apple"; // Hardcoded correct password
    private static final String DICTIONARY_FILE = "dictionary.txt";

    private JTextField usernameField;
    private JButton crackButton;
    private JTextArea resultArea;

    public Main() {
        setTitle("Password Cracker");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Enter Username:"));
        usernameField = new JTextField(20);
        add(usernameField);

        crackButton = new JButton("Crack Password");
        add(crackButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        crackButton.addActionListener(e -> crackPassword());

        setVisible(true);
    }

    private void crackPassword() {
        resultArea.setText("");
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            resultArea.setText("Please enter a username.");
            return;
        }

        resultArea.append("Attempting Dictionary Attack...\n");
        if (dictionaryAttack()) {
            resultArea.append("Password found using Dictionary Attack!\n");
            return;
        }

        resultArea.append("Dictionary Attack failed. Starting Brute Force Attack...\n");
        bruteForceAttack();
    }

    private boolean dictionaryAttack() {
        try (Scanner scanner = new Scanner(new File(DICTIONARY_FILE))) {
            while (scanner.hasNextLine()) {
                String password = scanner.nextLine().trim();
                if (password.equals(CORRECT_PASSWORD)) {
                    resultArea.append("Dictionary Attack successful! Password: " + password + "\n");
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            resultArea.append("Dictionary file not found.\n");
        }
        return false;
    }

    private void bruteForceAttack() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] guess = new char[5];

        if (bruteForceRecursive(chars, guess, 0)) {
            resultArea.append("Brute Force Attack successful!\n");
        } else {
            resultArea.append("Failed to crack the password.\n");
        }
    }

    private boolean bruteForceRecursive(char[] chars, char[] guess, int index) {
        if (index == 5) {
            String attempt = new String(guess);
            resultArea.append("Trying: " + attempt + "\n");
            if (attempt.equals(CORRECT_PASSWORD)) {
                resultArea.append("Password found: " + attempt + "\n");
                return true;
            }
            return false;
        }

        for (char c : chars) {
            guess[index] = c;
            if (bruteForceRecursive(chars, guess, index + 1)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}