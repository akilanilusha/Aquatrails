package DAO;
import Entity.Booking;
import Entity.Booking;
import DatabaseModel.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingDAO {

    public static boolean insertBooking(Booking booking) {
        String query = "INSERT INTO booking (visit_date, visitor_name, visitor_id, package_name, price, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(booking.getVisitDate().getTime()));
            stmt.setString(2, booking.getVisitorName());
            stmt.setString(3, booking.getVisitorId());
            stmt.setString(4, booking.getPackageName());
            stmt.setDouble(5, booking.getPrice());
            stmt.setString(6, booking.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Or use a logger
            return false;
        }
    }

    public static boolean updateBooking(Booking booking) {
        String query = "UPDATE booking SET visitor_name = ?, package_name = ?, price = ?, status = ?, visit_date = ? WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, booking.getVisitorName());
            stmt.setString(2, booking.getPackageName());
            stmt.setDouble(3, booking.getPrice());
            stmt.setString(4, booking.getStatus());
            stmt.setDate(5, new java.sql.Date(booking.getVisitDate().getTime()));
            stmt.setInt(6, booking.getBookingId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteBooking(int bookingId) {
        String query = "DELETE FROM booking WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
