/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.components.user;

import GUI.Dashboard;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import DatabaseModel.DatabaseConnection;

/**
 *
 * @author akilanilusha
 */
public class AddUserButton {

    public static void showUserDialog(Dashboard dashboard) {
        Font inputFont = new Font("SansSerif", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(250, 40); // Taller fields

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(fieldSize);
        usernameField.setFont(inputFont);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        passwordField.setFont(inputFont);

        JTextField nicField = new JTextField();
        nicField.setPreferredSize(fieldSize);
        nicField.setFont(inputFont);

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);
        emailField.setFont(inputFont);

        String[] roles = {
            "Administrator", "Receptionist", "Hotel Manager", "Guest", "Maintenance Staff"
        };
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(fieldSize);
        roleComboBox.setFont(inputFont);

        String[] statusOptions = {"Active", "Leave", "Suspended"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setPreferredSize(fieldSize);
        statusComboBox.setFont(inputFont);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("NIC:"));
        inputPanel.add(nicField);

        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(roleComboBox);

        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusComboBox);

        JOptionPane optionPane = new JOptionPane(inputPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(dashboard, "Add New User");
        dialog.setSize(600, 400);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String nic = nicField.getText();
            String email = emailField.getText();
            String role = (String) roleComboBox.getSelectedItem();
            String status = (String) statusComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty() || nic.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dashboard, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    String insertQuery = "INSERT INTO user (username, password, user_role, nic, email, status) VALUES ('"
                            + username + "', '"
                            + password + "', '"
                            + role + "', '"
                            + nic + "', '"
                            + email + "', '"
                            + status + "')";

                    DatabaseConnection.insertData(insertQuery);
                    JOptionPane.showMessageDialog(dashboard, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dashboard.loadUserCards();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dashboard, "Error saving user.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

}
