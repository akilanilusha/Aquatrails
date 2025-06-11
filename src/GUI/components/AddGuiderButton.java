package GUI.components;

import GUI.Dashboard;
import com.toedter.calendar.JDateChooser;
import DatabaseModel.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author akilanilusha
 */

public class AddGuiderButton {

    public static void showGuiderDialog(Dashboard dashboard) {
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 40));

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        JTextField locationField = new JTextField(20);
        JComboBox<String> packageComboBox = new JComboBox<>();
        JCheckBox activeCheckbox = new JCheckBox("Is Active", true);

        try {
            var rs = DatabaseConnection.getConnection().createStatement().executeQuery("SELECT package_name FROM packages");
            while (rs.next()) {
                packageComboBox.addItem(rs.getString("package_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Image upload and preview
        JLabel imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(150, 150));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        final String[] imageBase64 = {null};

        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    byte[] imageBytes = Files.readAllBytes(file.toPath());
                    imageBase64[0] = Base64.getEncoder().encodeToString(imageBytes); //encode image using base 64 encoder

                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaled));

                    JOptionPane.showMessageDialog(null, "Image uploaded successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to read image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Date of Birth:"));
        inputPanel.add(dateChooser);

        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);

        inputPanel.add(new JLabel("Package:"));
        inputPanel.add(packageComboBox);

        inputPanel.add(new JLabel("Active:"));
        inputPanel.add(activeCheckbox);

        inputPanel.add(new JLabel("Image:"));
        inputPanel.add(uploadButton);

        JPanel imagePanel = new JPanel();
        imagePanel.add(imagePreviewLabel);

        JPanel combinedPanel = new JPanel(new BorderLayout(10, 10));
        combinedPanel.add(inputPanel, BorderLayout.NORTH);
        combinedPanel.add(imagePanel, BorderLayout.CENTER);

        JOptionPane optionPane = new JOptionPane(combinedPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(dashboard, "Add New Guider");

        dialog.setSize(600, 600);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            String name = nameField.getText().trim();
            Date selectedDate = dateChooser.getDate();
            String location = locationField.getText().trim();
            String packageName = (String) packageComboBox.getSelectedItem();
            boolean isActive = activeCheckbox.isSelected();

            if (name.isEmpty() || selectedDate == null || location.isEmpty() || packageName == null) {
                JOptionPane.showMessageDialog(dashboard, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dob = sdf.format(selectedDate);

                    // Use PreparedStatement for safe query execution
                    String insertQuery = "INSERT INTO guider (name, date_of_birth, location, package_name, is_active, image_base64) VALUES (?, ?, ?, ?, ?, ?)";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement ps = conn.prepareStatement(insertQuery)) {

                        ps.setString(1, name);
                        ps.setString(2, dob);
                        ps.setString(3, location);
                        ps.setString(4, packageName);
                        ps.setBoolean(5, isActive);
                        ps.setString(6, imageBase64[0] != null ? imageBase64[0] : null);

                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(dashboard, "Guider added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                        dashboard.loadGuiderCards();

                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dashboard, "Error saving guider.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }
}
