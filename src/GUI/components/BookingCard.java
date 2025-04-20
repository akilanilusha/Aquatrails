package GUI.components;

import model.Package;
import javax.swing.*;
import java.awt.*;
import model.DatabaseConnection;

public class BookingCard extends JPanel {

    private JLabel bookingIdLabel;
    private JLabel visitorNameLabel;
    private JLabel packageNameLabel;
    private JLabel priceLabel;
    private JButton updateButton;
    private JButton deleteButton;

    public BookingCard(int bookingId, String visitorName, String packageName, double price) {
        setLayout(new GridLayout(1, 6, 10, 0)); // 1 row, 6 columns, 10px horizontal gap
        setPreferredSize(new Dimension(700, 50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Create columns
        bookingIdLabel = createCenteredLabel("ID: " + bookingId, labelFont);
        visitorNameLabel = createCenteredLabel(visitorName, labelFont);
        packageNameLabel = createCenteredLabel(packageName, labelFont);
        priceLabel = createCenteredLabel("$" + price, labelFont);
        updateButton = createStyledButton("Update", new Color(76, 175, 80));
        deleteButton = createStyledButton("Delete", new Color(244, 67, 54));

        // Add to panel
        add(bookingIdLabel);
        add(visitorNameLabel);
        add(packageNameLabel);
        add(priceLabel);
        add(updateButton);
        add(deleteButton);

        updateButton.addActionListener(e -> {
            JTextField nameField = new JTextField(visitorNameLabel.getText());
            JTextField priceField = new JTextField(priceLabel.getText().replace("$", ""));

            // Example package options - you can replace with dynamic options
            JComboBox<String> packageComboBox = new JComboBox<>();
            packageComboBox.setSelectedItem(packageNameLabel.getText());
            new Package().fetchPackagesFromDatabase(packageComboBox);

            // Styling
            Dimension inputSize = new Dimension(250, 30);
            nameField.setPreferredSize(inputSize);
            priceField.setPreferredSize(inputSize);
            packageComboBox.setPreferredSize(inputSize);

            Font modernFont = new Font("Segoe UI", Font.PLAIN, 16);
            nameField.setFont(modernFont);
            priceField.setFont(modernFont);
            packageComboBox.setFont(modernFont);

            nameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            priceField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            packageComboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

            JPanel updatePanel = new JPanel(new GridLayout(3, 2, 15, 15));
            updatePanel.setPreferredSize(new Dimension(500, 200));
            updatePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            updatePanel.add(new JLabel("Visitor Name:"));
            updatePanel.add(nameField);
            updatePanel.add(new JLabel("Package Name:"));
            updatePanel.add(packageComboBox);
            updatePanel.add(new JLabel("Price:"));
            updatePanel.add(priceField);

            int result = JOptionPane.showConfirmDialog(this, updatePanel,
                    "Update Booking ID: " + bookingId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText();
                String newPackage = (String) packageComboBox.getSelectedItem();
                String newPriceText = priceField.getText();

                try {
                    double newPrice = Double.parseDouble(newPriceText);

                    String updateQuery = "UPDATE booking SET visitor_name = '" + newName
                            + "', package_name = '" + newPackage
                            + "', price = " + newPrice
                            + " WHERE booking_id = " + bookingId;

                    DatabaseConnection.updateData(updateQuery);

                    visitorNameLabel.setText(newName);
                    packageNameLabel.setText(newPackage);
                    priceLabel.setText("$" + newPrice);

                    JOptionPane.showMessageDialog(this, "Booking updated successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Booking ID: " + bookingId + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Booking ID: " + bookingId + " deleted.");
                String deleteQuery = "DELETE FROM booking WHERE booking_id = " + bookingId;
                DatabaseConnection.deleteData(deleteQuery);

                Container parent = this.getParent();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                }

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
}
