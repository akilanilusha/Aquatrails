package GUI.components.booking;

import DAO.BookingDAO;
import DatabaseModel.LoadPackage;
import Entity.Booking;
import GUI.Dashboard;
import GUI.components.booking.DocumentListner;
import com.toedter.calendar.JDateChooser;
import java.awt.GridLayout;
import java.util.Date;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author malindu
 */

public class BookinButtonAction {

    public static void showBookingDialog(Dashboard dashboard) {
        JDateChooser dateChooser = new JDateChooser();
        JTextField visitorNameField = new JTextField();
        JTextField visitorIdField = new JTextField();
        JComboBox<String> packageComboBox = new JComboBox<>();
        JTextField memberCountField = new JTextField("1"); // default 1 member
        JTextField discountField = new JTextField("0");     // default 0%
        JTextField priceField = new JTextField();
        priceField.setEditable(false); // disabled field
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"confirmed", "cancelled", "pending", "complete"});

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2));

        panel.add(new JLabel("Select Date:"));
        panel.add(dateChooser);

        panel.add(new JLabel("Visitor Name:"));
        panel.add(visitorNameField);

        panel.add(new JLabel("Visitor ID:"));
        panel.add(visitorIdField);

        panel.add(new JLabel("Select Package:"));
        panel.add(packageComboBox);

        panel.add(new JLabel("No. of Members:"));
        panel.add(memberCountField);

        panel.add(new JLabel("Discount (%):"));
        panel.add(discountField);

        panel.add(new JLabel("Total Price:"));
        panel.add(priceField);

        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

        // Load package data
        new LoadPackage().fetchPackagesFromDatabase(packageComboBox);

        // Hold package price mapping
        Map<String, Double> packagePrices = LoadPackage.getPackagePriceMap();

        // Recalculate price when selection/fields change
        Runnable updatePrice = () -> {
            String selectedPackage = (String) packageComboBox.getSelectedItem();
            String membersText = memberCountField.getText();
            String discountText = discountField.getText();

            try {
                int members = Integer.parseInt(membersText);
                double discount = Double.parseDouble(discountText);
                double basePrice = packagePrices.getOrDefault(selectedPackage, 0.0);
                double total = basePrice * members;

                if (discount > 0 && discount <= 100) {
                    total = total - (total * (discount / 100.0));
                }

                priceField.setText(String.format("%.2f", total));
            } catch (NumberFormatException e) {
                priceField.setText("Invalid input");
            }
        };

        // Add listeners
        packageComboBox.addActionListener(e -> updatePrice.run());
        memberCountField.getDocument().addDocumentListener(new DocumentListner(updatePrice) {
        });
        discountField.getDocument().addDocumentListener(new DocumentListner(updatePrice) {
        });

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(dashboard, "New Booking");
        dialog.setSize(600, 400);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            Date selectedDate = dateChooser.getDate();
            String visitorName = visitorNameField.getText();
            String visitorId = visitorIdField.getText();
            String selectedPackage = (String) packageComboBox.getSelectedItem();
            String priceText = priceField.getText();
            String status = (String) statusComboBox.getSelectedItem();

            if (selectedDate == null || visitorName.isEmpty() || visitorId.isEmpty()
                    || selectedPackage == null || priceText.isEmpty() || status == null || priceText.equals("Invalid input")) {
                JOptionPane.showMessageDialog(dashboard, "Please fill all fields correctly.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double price = Double.parseDouble(priceText);
                    Booking booking = new Booking(selectedDate, visitorName, visitorId, selectedPackage, price, status);
                    
                    BookingDAO dao = BookingDAO.getInstance();

                    boolean success = dao.insert(booking);

                    if (success) {
                        JOptionPane.showMessageDialog(dashboard, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dashboard.loadBookingCards();

                    } else {
                        JOptionPane.showMessageDialog(dashboard, "Failed to insert booking.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(dashboard, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
