/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.components.booking;

import GUI.Dashboard;
import com.toedter.calendar.JDateChooser;
import java.awt.GridLayout;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.DatabaseConnection;
import model.LoadPackage;

/**
 *
 * @author akilanilusha
 */
public class BookinButtonAction {

    public static void showBookingDialog(Dashboard dashboard) {
        JDateChooser dateChooser = new JDateChooser();
        JTextField visitorNameField = new JTextField();
        JTextField visitorIdField = new JTextField();
        JComboBox<String> packageComboBox = new JComboBox<>();
        JTextField priceField = new JTextField();
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"confirmed", "cancelled", "ongoing"});

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Select Date:"));
        panel.add(dateChooser);

        panel.add(new JLabel("Visitor Name:"));
        panel.add(visitorNameField);

        panel.add(new JLabel("Visitor ID:"));
        panel.add(visitorIdField);

        panel.add(new JLabel("Select Package:"));
        panel.add(packageComboBox);

        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

        new LoadPackage().fetchPackagesFromDatabase(packageComboBox);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(dashboard, "New Booking");
        dialog.setSize(500, 350);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            Date selectedDate = dateChooser.getDate();
            String visitorName = visitorNameField.getText();
            String visitorId = visitorIdField.getText();
            String selectedPackage = (String) packageComboBox.getSelectedItem();
            String priceText = priceField.getText();
            String status = (String) statusComboBox.getSelectedItem();

            if (selectedDate == null || visitorName.isEmpty() || visitorId.isEmpty() || selectedPackage == null || priceText.isEmpty() || status == null) {
                JOptionPane.showMessageDialog(dashboard, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double price = Double.parseDouble(priceText);

                    String insertQuery = "INSERT INTO booking (visit_date, visitor_name, visitor_id, package_name, price, status) "
                            + "VALUES ('" + new java.sql.Date(selectedDate.getTime()) + "', '"
                            + visitorName + "', '" + visitorId + "', '" + selectedPackage + "', " + price + ", '" + status + "')";

                    DatabaseConnection.insertData(insertQuery);
                    JOptionPane.showMessageDialog(dashboard, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    dashboard.loadBookingCards();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(dashboard, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
