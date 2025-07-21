package DAO;

import Entity.Booking;
import DatabaseModel.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author malindu
 */

public class BookingDAO extends CommonDAO<Booking> {

    private static BookingDAO instance;

    private BookingDAO() {
    }
    
    public static synchronized BookingDAO getInstance() {
        if (instance == null) {
            instance = new BookingDAO();
        }
        return instance;
    }

    @Override
    public boolean insert(Booking booking) {
        String query = "INSERT INTO booking (visit_date, visitor_name, visitor_id, package_name, price, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(booking.getVisitDate().getTime()));
            stmt.setString(2, booking.getVisitorName());
            stmt.setString(3, booking.getVisitorId());
            stmt.setString(4, booking.getPackageName());
            stmt.setDouble(5, booking.getPrice());
            stmt.setString(6, booking.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Booking booking) {
        String query = "UPDATE booking SET visitor_name = ?, package_name = ?, price = ?, status = ?, visit_date = ? WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

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

    @Override
    public boolean delete(int bookingId) {
        String query = "DELETE FROM booking WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Booking> getAll() {
        String query = "SELECT * FROM booking";
        List<Booking> bookings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking(); // use default constructor
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setVisitDate(rs.getDate("visit_date"));
                booking.setVisitorName(rs.getString("visitor_name"));
                booking.setVisitorId(rs.getString("visitor_id"));
                booking.setPackageName(rs.getString("package_name"));
                booking.setPrice(rs.getDouble("price"));
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    @Override
    public Booking getById(int bookingId) {
        String query = "SELECT * FROM booking WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking(); // use default constructor
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setVisitDate(rs.getDate("visit_date"));
                    booking.setVisitorName(rs.getString("visitor_name"));
                    booking.setVisitorId(rs.getString("visitor_id"));
                    booking.setPackageName(rs.getString("package_name"));
                    booking.setPrice(rs.getDouble("price"));
                    booking.setStatus(rs.getString("status"));
                    return booking;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
