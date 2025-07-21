package DAO;

import DatabaseModel.DatabaseConnection;
import Entity.Guider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp
 */
public class GuiderDAO extends CommonDAO<Guider> {

    private static GuiderDAO instance;

    private GuiderDAO() {
    }

    public static synchronized GuiderDAO getInstance() {
        if (instance == null) {
            instance = new GuiderDAO();
        }
        return instance;
    }

    @Override
    public boolean insert(Guider guider) {
        String sql = "INSERT INTO guider (name, date_of_birth, location, package_name, is_active, image_base64) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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

    @Override
    public List<Guider> getAll() {
        List<Guider> list = new ArrayList<>();
        String sql = "SELECT * FROM guider";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

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

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM guider WHERE guider_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Guider guider) {
        String sql = "UPDATE guider SET name=?, date_of_birth=?, location=?, package_name=?, is_active=?, image_base64=? WHERE guider_id=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    @Override
    public Guider getById(int id) {
        String sql = "SELECT * FROM guider WHERE guider_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
