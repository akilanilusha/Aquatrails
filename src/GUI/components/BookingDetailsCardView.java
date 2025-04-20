package GUI.components;


import GUI.components.BookingCard;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import model.DatabaseConnection;


public class BookingDetailsCardView {
    private JPanel mainPanel;
    private JScrollPane scrollPane;

    public BookingDetailsCardView() {
        // Create main panel (vertical stack)
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE); // Optional: for better contrast

        // Add padding and spacing between cards
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Put mainPanel into a scroll pane
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling
        scrollPane.setPreferredSize(new Dimension(700, 400)); // or adjust to fit your layout
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void loadBookingDetails() {
        mainPanel.removeAll(); // Clear previous

        try {
             Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT booking_id, visitor_name, package_name, price FROM booking");

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;

                int bookingId = rs.getInt("booking_id");
                String visitorName = rs.getString("visitor_name");
                String packageName = rs.getString("package_name");
                double price = rs.getDouble("price");

                BookingCard card = new BookingCard(bookingId, visitorName, packageName, price);
                
                // Set preferred width and fixed height for the card
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                card.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Add spacing below each card
                mainPanel.add(card);
                mainPanel.add(Box.createVerticalStrut(10));
            }

            if (!hasData) {
                JLabel noDataLabel = new JLabel("No bookings found.");
                noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(noDataLabel);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
