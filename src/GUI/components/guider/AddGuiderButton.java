package GUI.components.guider;

import DAO.BookingDAO;
import GUI.Dashboard;
import DAO.GuiderDAO;
import Entity.Guider;
import com.toedter.calendar.JDateChooser;
import DatabaseModel.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author hp
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

        try (var stmt = DatabaseConnection.getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT package_name FROM packages")) {
            while (rs.next()) {
                packageComboBox.addItem(rs.getString("package_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Image preview
        JLabel imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(150, 150));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        final String[] imageBase64 = {null};

        // Upload Button
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    byte[] imageBytes = Files.readAllBytes(file.toPath());
                    imageBase64[0] = Base64.getEncoder().encodeToString(imageBytes);

                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaled));

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to read image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Input Panel
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

                    Guider guider = new Guider(name, dob, location, packageName, isActive, imageBase64[0]);
                    
                    GuiderDAO dao = GuiderDAO.getInstance();
                    boolean success = dao.insert(guider);

                    if (success) {
                        JOptionPane.showMessageDialog(dashboard, "Guider added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dashboard.loadGuiderCards();

                    } else {
                        JOptionPane.showMessageDialog(dashboard, "Failed to add guider.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dashboard, "An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }
}
