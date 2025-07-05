package GUI.components.packages;

import DAO.PackageDAO;
import Entity.Package;

import javax.swing.*;
import java.awt.*;

/**
 * @author akilanilusha
 */
public class PackageCardRow extends JPanel {

    private JLabel packageCodeLabel;
    private JLabel packageNameLabel;
    private JLabel descriptionLabel;
    private JLabel locationLabel;
    private JLabel priceLabel;
    private JLabel statusLabel;
    private JButton updateButton;
    private JButton deleteButton;

    public PackageCardRow(Package pkg) {
        setLayout(new GridLayout(1, 8, 10, 0));
        setPreferredSize(new Dimension(900, 50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        packageCodeLabel = createCenteredLabel(pkg.getPackageCode(), labelFont);
        packageNameLabel = createCenteredLabel(pkg.getPackageName(), labelFont);
        descriptionLabel = createCenteredLabel(pkg.getDescription(), labelFont);
        locationLabel = createCenteredLabel(pkg.getLocation(), labelFont);
        priceLabel = createCenteredLabel("$" + pkg.getPrice(), labelFont);
        statusLabel = createCenteredLabel(pkg.getStatus(), labelFont);

        updateButton = createStyledButton("Update", new Color(76, 175, 80));
        deleteButton = createStyledButton("Delete", new Color(244, 67, 54));

        add(packageCodeLabel);
        add(packageNameLabel);
        add(locationLabel);
        add(priceLabel);
        add(statusLabel);
        add(updateButton);
        add(deleteButton);

        updateStatusLabelColor(pkg.getStatus());

        updateButton.addActionListener(e -> {
            JTextField codeField = new JTextField(pkg.getPackageCode());
            JTextField nameField = new JTextField(pkg.getPackageName());
            JTextField descriptionField = new JTextField(pkg.getDescription());
            JTextField locationField = new JTextField(pkg.getLocation());
            JTextField priceField = new JTextField(String.valueOf(pkg.getPrice()));

            JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Active", "Pending", "Closed"});
            statusComboBox.setSelectedItem(pkg.getStatus());

            JPanel updatePanel = new JPanel(new GridLayout(6, 2, 10, 10));
            updatePanel.setPreferredSize(new Dimension(500, 300));
            updatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            updatePanel.add(new JLabel("Package Code:"));
            updatePanel.add(codeField);
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
                    "Update Package ID: " + pkg.getPackageId(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newCode = codeField.getText();
                    String newName = nameField.getText();
                    String newDesc = descriptionField.getText();
                    String newLocation = locationField.getText();
                    double newPrice = Double.parseDouble(priceField.getText());
                    String newStatus = (String) statusComboBox.getSelectedItem();

                    Package updatedPkg = new Package(pkg.getPackageId(), newCode, newName, newDesc, newLocation, newPrice, newStatus);
                    PackageDAO.updatePackage(updatedPkg);

                    // UI Updates
                    packageCodeLabel.setText(newCode);
                    packageNameLabel.setText(newName);
                    descriptionLabel.setText(newDesc);
                    locationLabel.setText(newLocation);
                    priceLabel.setText("$" + newPrice);
                    statusLabel.setText(newStatus);
                    updateStatusLabelColor(newStatus);

                    JOptionPane.showMessageDialog(this, "Package updated successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this package?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                PackageDAO.deletePackage(pkg.getPackageId());

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
        switch (status) {
            case "Active" -> {
                statusLabel.setBackground(new Color(76, 175, 80));
                statusLabel.setForeground(Color.WHITE);
            }
            case "Pending" -> {
                statusLabel.setBackground(new Color(255, 235, 59));
                statusLabel.setForeground(Color.BLACK);
            }
            case "Closed" -> {
                statusLabel.setBackground(new Color(244, 67, 54));
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
