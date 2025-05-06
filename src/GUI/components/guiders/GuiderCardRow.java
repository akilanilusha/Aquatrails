package GUI.components.guiders;

import model.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.util.Base64;

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

    public GuiderCardRow(int guiderId, String name, int age, String location,
            String packageName, boolean isActive, String imageBase64) {

        this.guiderId = guiderId;
        this.location = location;
        this.imageBase64 = imageBase64;

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

        viewButton.addActionListener(e -> {
            JTextArea textArea = new JTextArea(
                    "Guider ID: " + guiderId
                    + "\nName: " + nameLabel.getText()
                    + "\nAge: " + ageLabel.getText()
                    + "\nLocation: " + location
                    + "\nPackage: " + packageLabel.getText()
                    + "\nStatus: " + activeStatusLabel.getText()
            );
            textArea.setEditable(false);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            textArea.setBackground(new Color(250, 250, 250));
            textArea.setMargin(new Insets(10, 10, 10, 10));
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
            mainPanel.setBackground(Color.WHITE);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            if (imageBase64 != null && !imageBase64.isEmpty()) {
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
                    ImageIcon imageIcon = new ImageIcon(imageBytes);
                    Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(image);

                    JLabel imageLabel = new JLabel(imageIcon);
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));
                    imageLabel.setPreferredSize(new Dimension(150, 150));

                    mainPanel.add(imageLabel, BorderLayout.WEST);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid image format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            scrollPane.setPreferredSize(new Dimension(300, 200));
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            JLabel title = new JLabel("Guider Details", JLabel.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            title.setForeground(new Color(33, 150, 243));
            title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

            JPanel contentWrapper = new JPanel(new BorderLayout());
            contentWrapper.setBackground(Color.WHITE);
            contentWrapper.add(title, BorderLayout.NORTH);
            contentWrapper.add(mainPanel, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(this, contentWrapper, "Guider Info",
                    JOptionPane.PLAIN_MESSAGE);
        });

        updateButton.addActionListener(e -> showUpdateDialog(name, age, location, packageName, isActive));

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Guider ID: " + guiderId + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                String deleteQuery = "DELETE FROM guider WHERE guider_id = " + guiderId;
                DatabaseConnection.deleteData(deleteQuery);

                Container parent = this.getParent();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                }

                JOptionPane.showMessageDialog(this, "Guider deleted successfully.");
            }
        });
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

    private void showUpdateDialog(String name, int age, String location, String packageName, boolean isActive) {
        JTextField nameField = new JTextField(name);
        JTextField ageField = new JTextField(String.valueOf(age));
        JTextField locationField = new JTextField(location);
        JComboBox<String> packageComboBox = new JComboBox<>();
        new model.Package().fetchPackagesFromDatabase(packageComboBox);
        packageComboBox.setSelectedItem(packageName); // Ensure correct item selected

        JCheckBox activeBox = new JCheckBox("Active", isActive);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);  // ✅ Added missing field

        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        panel.add(new JLabel("Package:"));
        panel.add(packageComboBox);  // ✅ Correct usage

        panel.add(new JLabel("Status:"));
        panel.add(activeBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Guider", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText();
                int newAge = Integer.parseInt(ageField.getText());
                String newLocation = locationField.getText();
                String newPackage = (String) packageComboBox.getSelectedItem(); // ✅ FIXED
                boolean newStatus = activeBox.isSelected();

                String sql = "UPDATE guider SET name='" + newName + "', location='" + newLocation
                        + "', package_name='" + newPackage + "', is_active=" + newStatus
                        + " WHERE guider_id=" + guiderId;
                DatabaseConnection.updateData(sql);

                // UI Update
                nameLabel.setText(newName);
                ageLabel.setText(newAge + " yrs");
                packageLabel.setText(newPackage);
                activeStatusLabel.setText(newStatus ? "Active" : "Inactive");
                activeStatusLabel.setForeground(newStatus ? Color.GREEN.darker() : Color.RED);

                JOptionPane.showMessageDialog(this, "Guider updated successfully!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
