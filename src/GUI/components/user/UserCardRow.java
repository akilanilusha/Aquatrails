package GUI.components.user;

import model.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class UserCardRow extends JPanel {

    private JLabel usernameLabel;
    private JLabel nicLabel;
    private JLabel emailLabel;
    private JLabel roleLabel;
    private JLabel statusLabel;
    private JButton updateButton;
    private JButton deleteButton;

    private int userId;

    public UserCardRow(int userId, String username, String userRole, String nic, String email, String status) {
        this.userId = userId;

        setLayout(new GridLayout(1, 7, 10, 0));
        setPreferredSize(new Dimension(1000, 50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        usernameLabel = createCenteredLabel(username, labelFont);
        nicLabel = createCenteredLabel(nic, labelFont);
        emailLabel = createCenteredLabel(email, labelFont);
        roleLabel = createCenteredLabel(userRole, labelFont);
        statusLabel = createCenteredLabel(status, labelFont);
        updateStatusLabelColor(status);

        updateButton = createStyledButton("Update", new Color(76, 175, 80));
        deleteButton = createStyledButton("Delete", new Color(244, 67, 54));

        add(usernameLabel);
        add(nicLabel);
        add(emailLabel);
        add(roleLabel);
        add(statusLabel);
        add(updateButton);
        add(deleteButton);

        // --- Update Button Action ---
        updateButton.addActionListener(e -> {
            JTextField usernameField = new JTextField(usernameLabel.getText());
            JPasswordField passwordField = new JPasswordField(); // Empty default
            JTextField nicField = new JTextField(nicLabel.getText());
            JTextField emailField = new JTextField(emailLabel.getText());
            JComboBox<String> roleCombo = new JComboBox<>(new String[]{
                "Administrator", "Receptionist", "Hotel Manager", "Guest", "Maintenance Staff"
            });
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Leave", "Suspended"});

            roleCombo.setSelectedItem(roleLabel.getText());
            statusCombo.setSelectedItem(statusLabel.getText());

            JPanel updatePanel = new JPanel(new GridLayout(6, 2, 10, 10));
            updatePanel.setPreferredSize(new Dimension(450, 300));
            updatePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            updatePanel.add(new JLabel("Username:"));
            updatePanel.add(usernameField);
            updatePanel.add(new JLabel("New Password (leave blank to keep current):"));
            updatePanel.add(passwordField);
            updatePanel.add(new JLabel("NIC:"));
            updatePanel.add(nicField);
            updatePanel.add(new JLabel("Email:"));
            updatePanel.add(emailField);
            updatePanel.add(new JLabel("User Role:"));
            updatePanel.add(roleCombo);
            updatePanel.add(new JLabel("Status:"));
            updatePanel.add(statusCombo);

            int result = JOptionPane.showConfirmDialog(this, updatePanel,
                    "Update User ID: " + userId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newUsername = usernameField.getText().trim();
                String newPassword = new String(passwordField.getPassword()).trim();
                String newNIC = nicField.getText().trim();
                String newEmail = emailField.getText().trim();
                String newRole = (String) roleCombo.getSelectedItem();
                String newStatus = (String) statusCombo.getSelectedItem();

                String updateQuery;
                if (!newPassword.isEmpty()) {
                    updateQuery = "UPDATE user SET "
                            + "username = '" + newUsername + "', "
                            + "password = '" + newPassword + "', "
                            + "nic = '" + newNIC + "', "
                            + "email = '" + newEmail + "', "
                            + "user_role = '" + newRole + "', "
                            + "status = '" + newStatus + "' "
                            + "WHERE user_id = " + userId;
                } else {
                    updateQuery = "UPDATE user SET "
                            + "username = '" + newUsername + "', "
                            + "nic = '" + newNIC + "', "
                            + "email = '" + newEmail + "', "
                            + "user_role = '" + newRole + "', "
                            + "status = '" + newStatus + "' "
                            + "WHERE user_id = " + userId;
                }

                DatabaseConnection.updateData(updateQuery);

                // Update UI
                usernameLabel.setText(newUsername);
                nicLabel.setText(newNIC);
                emailLabel.setText(newEmail);
                roleLabel.setText(newRole);
                statusLabel.setText(newStatus);
                updateStatusLabelColor(newStatus);

                JOptionPane.showMessageDialog(this, "User updated successfully!");
            }
        });

        // --- Delete Button Action ---
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete User ID: " + userId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String deleteQuery = "DELETE FROM user WHERE user_id = " + userId;
                DatabaseConnection.deleteData(deleteQuery);

                Container parent = this.getParent();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                }

                JOptionPane.showMessageDialog(this, "User deleted successfully.");
            }
        });
    }

    private JLabel createCenteredLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void updateStatusLabelColor(String status) {
        statusLabel.setOpaque(true);
        switch (status) {
            case "Active" -> {
                statusLabel.setBackground(new Color(76, 175, 80));
                statusLabel.setForeground(Color.WHITE);
            }
            case "Leave" -> {
                statusLabel.setBackground(new Color(255, 193, 7));
                statusLabel.setForeground(Color.BLACK);
            }
            case "Suspended" -> {
                statusLabel.setBackground(Color.GRAY);
                statusLabel.setForeground(Color.WHITE);
            }
            default -> {
                statusLabel.setBackground(null);
                statusLabel.setForeground(Color.BLACK);
            }
        }
    }
}
