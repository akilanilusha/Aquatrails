package DAO;

import Entity.Guider;
import DatabaseModel.DatabaseConnection;
import Entity.Guider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuiderDAO {

    public static boolean insertGuider(Guider guider) {
        String sql = "INSERT INTO guider (name, date_of_birth, location, package_name, is_active, image_base64) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guider.getName());
            ps.setString(2, guider.getDateOfBirth());
            ps.setString(3, guider.getLocation());
            ps.setString(4, guider.getPackageName());
            ps.setBoolean(5, guider.isActive());
            ps.setString(6, guider.getImageBase64());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Guider> getAllGuiders() {
        List<Guider> list = new ArrayList<>();
        String sql = "SELECT * FROM guider";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guider guider = new Guider(
                        rs.getInt("guider_id"),
                        rs.getString("name"),
                        rs.getString("date_of_birth"),
                        rs.getString("location"),
                        rs.getString("package_name"),
                        rs.getBoolean("is_active"),
                        rs.getString("image_base64")
                );
                list.add(guider);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteGuiderById(int id) {
        String sql = "DELETE FROM guider WHERE guider_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateGuider(Guider guider) {
        String sql = "UPDATE guider SET name=?, date_of_birth=?, location=?, package_name=?, is_active=?, image_base64=? WHERE guider_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, guider.getName());
            ps.setString(2, guider.getDateOfBirth());
            ps.setString(3, guider.getLocation());
            ps.setString(4, guider.getPackageName());
            ps.setBoolean(5, guider.isActive());
            ps.setString(6, guider.getImageBase64());
            ps.setInt(7, guider.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Guider getGuiderById(int id) {
        String sql = "SELECT * FROM guider WHERE guider_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Guider(
                        rs.getInt("guider_id"),
                        rs.getString("name"),
                        rs.getString("date_of_birth"),
                        rs.getString("location"),
                        rs.getString("package_name"),
                        rs.getBoolean("is_active"),
                        rs.getString("image_base64")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
