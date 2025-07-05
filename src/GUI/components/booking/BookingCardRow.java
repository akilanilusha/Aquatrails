package GUI.components.booking;

import Entity.Booking;
import DAO.BookingDAO;
import com.toedter.calendar.JDateChooser;
import DatabaseModel.LoadPackage;


import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;


public class BookingCardRow extends JPanel {

    private JLabel visitorNameLabel;
    private JLabel packageNameLabel;
    private JLabel priceLabel;
    private JLabel statusLabel;
    private JLabel bookingDateLabel;
    private JButton updateButton;
    private JButton deleteButton;

    public BookingCardRow(int bookingId, String visitorName, String packageName, double price, String status, String bookingDate) {
        setLayout(new GridLayout(1, 7, 10, 0));
        setPreferredSize(new Dimension(700, 50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        visitorNameLabel = createCenteredLabel(visitorName, labelFont);
        packageNameLabel = createCenteredLabel(packageName, labelFont);
        priceLabel = createCenteredLabel("$" + price, labelFont);
        statusLabel = createCenteredLabel(status, labelFont);
        bookingDateLabel = createCenteredLabel(bookingDate, labelFont);

        updateButton = createStyledButton("Update", new Color(76, 175, 80));
        deleteButton = createStyledButton("Delete", new Color(244, 67, 54));

        add(visitorNameLabel);
        add(packageNameLabel);
        add(bookingDateLabel);
        add(priceLabel);
        add(statusLabel);
        add(updateButton);
        add(deleteButton);

        updateStatusLabelColor(status);

        updateButton.addActionListener(e -> openUpdateDialog(bookingId));
        deleteButton.addActionListener(e -> deleteBooking(bookingId));
    }

    private void openUpdateDialog(int bookingId) {
        JTextField nameField = new JTextField(visitorNameLabel.getText());
        JTextField priceField = new JTextField(priceLabel.getText().replace("$", ""));

        JComboBox<String> packageComboBox = new JComboBox<>();
        new LoadPackage().fetchPackagesFromDatabase(packageComboBox);
        packageComboBox.setSelectedItem(packageNameLabel.getText());

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"confirmed", "cancelled", "ongoing"});
        statusComboBox.setSelectedItem(statusLabel.getText());

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        try {
            java.util.Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(bookingDateLabel.getText());
            dateChooser.setDate(parsedDate);
        } catch (Exception ex) {
            dateChooser.setDate(new java.util.Date());
        }

        JPanel updatePanel = new JPanel(new GridLayout(5, 2, 15, 15));
        updatePanel.setPreferredSize(new Dimension(500, 300));
        updatePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        updatePanel.add(new JLabel("Visitor Name:"));
        updatePanel.add(nameField);
        updatePanel.add(new JLabel("Package Name:"));
        updatePanel.add(packageComboBox);
        updatePanel.add(new JLabel("Booking Date:"));
        updatePanel.add(dateChooser);
        updatePanel.add(new JLabel("Price:"));
        updatePanel.add(priceField);
        updatePanel.add(new JLabel("Status:"));
        updatePanel.add(statusComboBox);

        int result = JOptionPane.showConfirmDialog(this, updatePanel,
                "Update Booking ID: " + bookingId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText().trim();
                String newPackage = (String) packageComboBox.getSelectedItem();
                double newPrice = Double.parseDouble(priceField.getText().trim());
                String newStatus = (String) statusComboBox.getSelectedItem();

                java.util.Date selectedDate = dateChooser.getDate();
                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(this, "Please select a valid date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

                // Create new Booking object
                Booking updatedBooking = new Booking(selectedDate, newName, "N/A", newPackage, newPrice, newStatus);
                updatedBooking.setBookingId(bookingId);
//                updatedBooking.setVisitDate(formattedDate);
                updatedBooking.setVisitDate(selectedDate);

                if (BookingDAO.updateBooking(updatedBooking)) {
                    visitorNameLabel.setText(newName);
                    packageNameLabel.setText(newPackage);
                    priceLabel.setText("$" + newPrice);
                    statusLabel.setText(newStatus);
                    bookingDateLabel.setText(formattedDate);
                    updateStatusLabelColor(newStatus);

                    JOptionPane.showMessageDialog(this, "Booking updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update booking.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price entered!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Booking ID: " + bookingId + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            if (BookingDAO.deleteBooking(bookingId)) {
                JOptionPane.showMessageDialog(this, "Booking ID: " + bookingId + " deleted.");
                Container parent = this.getParent();
                if (parent != null) {
                    parent.remove(this);
                    parent.revalidate();
                    parent.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    //chnge lble color
    private void updateStatusLabelColor(String status) {
        switch (status) {
            case "confirmed" -> {
                statusLabel.setBackground(new Color(76, 175, 80)); // Green
                statusLabel.setForeground(Color.WHITE);
            }
            case "ongoing" -> {
                statusLabel.setBackground(new Color(255, 235, 59)); // Yellow
                statusLabel.setForeground(Color.BLACK);
            }
            case "cancelled" -> {
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
