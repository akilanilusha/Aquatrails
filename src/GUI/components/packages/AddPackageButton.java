package GUI.components.packages;

import DAO.PackageDAO;
import Entity.Package;
import GUI.Dashboard;

import javax.swing.*;
import java.awt.*;

public class AddPackageButton {

    public static void showPackageDialog(Dashboard dashboard) {
        JTextField packageCodeField = new JTextField();
        JTextField packageNameField = new JTextField();
        JTextArea packageDescriptionArea = new JTextArea(5, 30);
        packageDescriptionArea.setLineWrap(true);
        packageDescriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(packageDescriptionArea);
        JTextField locationField = new JTextField();
        JTextField priceField = new JTextField();

        String[] statuses = {"Active", "Pending", "Closed"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Package Code:"));
        panel.add(packageCodeField);

        panel.add(new JLabel("Package Name:"));
        panel.add(packageNameField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionScroll);

        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(dashboard, "Add New Package");
        dialog.setSize(500, 350);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            String code = packageCodeField.getText().trim();
            String name = packageNameField.getText().trim();
            String description = packageDescriptionArea.getText().trim();
            String location = locationField.getText().trim();
            String priceText = priceField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (code.isEmpty() || name.isEmpty() || description.isEmpty() ||
                location.isEmpty() || priceText.isEmpty() || status == null) {
                JOptionPane.showMessageDialog(dashboard, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double price = Double.parseDouble(priceText);

                    Package newPackage = new Package(
                            code, name, description, location, price, status
                    );

                    PackageDAO.insertPackage(newPackage);
                    JOptionPane.showMessageDialog(dashboard, "Package added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dashboard.loadPackageCards();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(dashboard, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
