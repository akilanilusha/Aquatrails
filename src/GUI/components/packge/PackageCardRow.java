package GUI.components.packge;

import model.DatabaseConnection;
import javax.swing.*;
import java.awt.*;

public class PackageCardRow extends JPanel {

    private JLabel packageIdLabel;
    private JLabel packageCodeLabel;
    private JLabel packageNameLabel;
    private JLabel descriptionLabel;
    private JLabel locationLabel;
    private JLabel priceLabel;
    private JLabel statusLabel;
    private JButton updateButton;
    private JButton deleteButton;

    public PackageCardRow(int packageId, String packageCode, String packageName,
                          String description, String location, double price, String status) {

        setLayout(new GridLayout(1, 8, 10, 0)); // 1 row, 8 columns
        setPreferredSize(new Dimension(900, 50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        packageCodeLabel = createCenteredLabel(packageCode, labelFont);
        packageNameLabel = createCenteredLabel(packageName, labelFont);
        descriptionLabel = createCenteredLabel(description, labelFont);
        locationLabel = createCenteredLabel(location, labelFont);
        priceLabel = createCenteredLabel("$" + price, labelFont);
        statusLabel = createCenteredLabel(status, labelFont);

        updateButton = createStyledButton("Update", new Color(76, 175, 80));
        deleteButton = createStyledButton("Delete", new Color(244, 67, 54));

        add(packageCodeLabel);
        add(packageNameLabel);
        add(locationLabel);
        add(priceLabel);
        add(statusLabel);
        add(updateButton);
        add(deleteButton);

        // Set initial color for status
        updateStatusLabelColor(status);

        // Update button action
        updateButton.addActionListener(e -> {
            JTextField nameField = new JTextField(packageName);
            JTextField descriptionField = new JTextField(description);
            JTextField priceField = new JTextField(String.valueOf(price));
            JTextField locationField = new JTextField(location);

            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Active", "Pending", "Closed"});
            statusComboBox.setSelectedItem(status);

            Dimension inputSize = new Dimension(250, 30);
            Font modernFont = new Font("Segoe UI", Font.PLAIN, 16);

            for (JComponent field : new JComponent[]{nameField, descriptionField, priceField, locationField, statusComboBox}) {
                field.setPreferredSize(inputSize);
                field.setFont(modernFont);
                field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            }

            JPanel updatePanel = new JPanel(new GridLayout(5, 2, 15, 15));
            updatePanel.setPreferredSize(new Dimension(500, 300));
            updatePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            updatePanel.add(new JLabel("Package Name:"));
            updatePanel.add(nameField);
            updatePanel.add(new JLabel("Description:"));
            updatePanel.add(descriptionField);
            updatePanel.add(new JLabel("Location:"));
            updatePanel.add(locationField);
            updatePanel.add(new JLabel("Price:"));
            updatePanel.add(priceField);
            updatePanel.add(new JLabel("Status:"));
            updatePanel.add(statusComboBox);

            int result = JOptionPane.showConfirmDialog(this, updatePanel,
                    "Update Package ID: " + packageId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText();
                String newDesc = descriptionField.getText();
                String newLocation = locationField.getText();
                String newStatus = (String) statusComboBox.getSelectedItem();

                try {
                    double newPrice = Double.parseDouble(priceField.getText());

                    String updateQuery = "UPDATE packages SET package_name = '" + newName
                            + "', description = '" + newDesc
                            + "', location = '" + newLocation
                            + "', price = " + newPrice
                            + ", status = '" + newStatus
                            + "' WHERE package_id = " + packageId;

                    DatabaseConnection.updateData(updateQuery);

                    // Update UI after successful update
                    packageNameLabel.setText(newName);
                    descriptionLabel.setText(newDesc);
                    locationLabel.setText(newLocation);
                    priceLabel.setText("$" + newPrice);
                    statusLabel.setText(newStatus);
                    updateStatusLabelColor(newStatus); // Update color
                    
                    

                    JOptionPane.showMessageDialog(this, "Package updated successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Package ID: " + packageId + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                String deleteQuery = "DELETE FROM packages WHERE package_id = " + packageId;
                DatabaseConnection.deleteData(deleteQuery);

                Container parent = this.getParent();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                }

                JOptionPane.showMessageDialog(this, "Package deleted successfully.");
            }
        });
    }

    // Helper: Create centered label
    private JLabel createCenteredLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        return label;
    }

    // Helper: Create styled button
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    // Helper: Update status label color
    private void updateStatusLabelColor(String status) {
        switch (status) {
            case "Active" -> {
                statusLabel.setBackground(new Color(76, 175, 80)); // Green
                statusLabel.setForeground(Color.WHITE);
            }
            case "Pending" -> {
                statusLabel.setBackground(new Color(255, 235, 59)); // Yellow
                statusLabel.setForeground(Color.BLACK);
            }
            case "Closed" -> {
                statusLabel.setBackground(new Color(244, 67, 54)); // Red
                statusLabel.setForeground(Color.WHITE);
            }
            default -> {
                statusLabel.setBackground(null);
                statusLabel.setForeground(Color.BLACK);
            }
        }
        statusLabel.setOpaque(true);
    }
}
