package GUI.components.guider;

import DAO.GuiderDAO;
import Entity.Guider;
import DatabaseModel.DatabaseConnection;
import GUI.Dashboard;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class GuiderCardRow extends JPanel {

    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel packageLabel;
    private JLabel activeStatusLabel;
    private JButton viewButton;
    private JButton updateButton;
    private JButton deleteButton;

    private int guiderId;
    private String location;
    private String imageBase64;
    private String dateOfBirth;

    public GuiderCardRow(int guiderId, String name, int age, String location,
            String packageName, boolean isActive, String imageBase64, String dateOfBirth) {

        this.guiderId = guiderId;
        this.location = location;
        this.imageBase64 = imageBase64;
        this.dateOfBirth = dateOfBirth;

        setLayout(new GridLayout(1, 7, 10, 0));
        setPreferredSize(new Dimension(900, 50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        nameLabel = createCenteredLabel(name, labelFont);
        ageLabel = createCenteredLabel(age + " yrs", labelFont);
        packageLabel = createCenteredLabel(packageName, labelFont);
        activeStatusLabel = createCenteredLabel(isActive ? "Active" : "Inactive", labelFont);
        activeStatusLabel.setForeground(isActive ? Color.GREEN.darker() : Color.RED);

        viewButton = createStyledButton("View", new Color(33, 150, 243));
        updateButton = createStyledButton("Update", new Color(76, 175, 80));
        deleteButton = createStyledButton("Delete", new Color(244, 67, 54));

        add(nameLabel);
        add(ageLabel);
        add(packageLabel);
        add(activeStatusLabel);
        add(viewButton);
        add(updateButton);
        add(deleteButton);

        viewButton.addActionListener(e -> showViewDialog(name, age, packageName, isActive));
        updateButton.addActionListener(e -> showUpdateDialog(name, location, packageName, isActive));
        deleteButton.addActionListener(e -> handleDelete());
    }

    private JLabel createCenteredLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
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

    private void showViewDialog(String name, int age, String packageName, boolean isActive) {
        JTextArea textArea = new JTextArea(
                "Guider ID: " + guiderId
                + "\nName: " + name
                + "\nAge: " + age + " yrs"
                + "\nDOB: " + dateOfBirth
                + "\nLocation: " + location
                + "\nPackage: " + packageName
                + "\nStatus: " + (isActive ? "Active" : "Inactive")
        );
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textArea.setBackground(new Color(250, 250, 250));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Color.WHITE);

        if (imageBase64 != null && !imageBase64.isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
                ImageIcon imageIcon = new ImageIcon(imageBytes);
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));
                imageLabel.setPreferredSize(new Dimension(150, 150));
                mainPanel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid image format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, mainPanel, "Guider Details", JOptionPane.PLAIN_MESSAGE);
    }

    private void showUpdateDialog(String name, String location, String packageName, boolean isActive) {
        JTextField nameField = new JTextField(name);
        JTextField locationField = new JTextField(location);

        // DOB date chooser
        JDateChooser dobChooser = new JDateChooser();
        dobChooser.setDateFormatString("yyyy-MM-dd");
        try {
            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
                dobChooser.setDate(parsed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JComboBox<String> packageComboBox = new JComboBox<>();
        new DatabaseModel.LoadPackage().fetchPackagesFromDatabase(packageComboBox);
        packageComboBox.setSelectedItem(packageName);

        JCheckBox activeBox = new JCheckBox("Active", isActive);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date of Birth:"));
        panel.add(dobChooser);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Package:"));
        panel.add(packageComboBox);
        panel.add(new JLabel("Status:"));
        panel.add(activeBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Guider", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText();
                Date dobDate = dobChooser.getDate();
                if (dobDate == null) {
                    JOptionPane.showMessageDialog(this, "Please select a valid DOB.");
                    return;
                }
                String newDob = new SimpleDateFormat("yyyy-MM-dd").format(dobDate);
                String newLocation = locationField.getText();
                String newPackage = (String) packageComboBox.getSelectedItem();
                boolean newStatus = activeBox.isSelected();

                Guider updatedGuider = new Guider(guiderId, newName, newDob, newLocation, newPackage, newStatus, imageBase64);
                boolean updated = GuiderDAO.updateGuider(updatedGuider);

                if (updated) {
                    nameLabel.setText(newName);
                    int newAge = calculateAge(dobDate);
                    ageLabel.setText(newAge + " yrs");
                    packageLabel.setText(newPackage);
                    activeStatusLabel.setText(newStatus ? "Active" : "Inactive");
                    activeStatusLabel.setForeground(newStatus ? Color.GREEN.darker() : Color.RED);
                    this.dateOfBirth = newDob;
                    JOptionPane.showMessageDialog(this, "Guider updated successfully!");
                    new Dashboard().loadLableValues();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update guider.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDelete() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete Guider ID: " + guiderId + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = GuiderDAO.deleteGuiderById(guiderId);
            if (deleted) {
                Container parent = this.getParent();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                }
                JOptionPane.showMessageDialog(this, "Guider deleted successfully.");

            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete guider.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int calculateAge(Date dob) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(dob);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }
}
