package DAO;

import Entity.Package;
import DatabaseModel.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageDAO {

    public static void insertPackage(Package pkg) {
        String query = "INSERT INTO packages (package_code, package_name, description, location, price, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, pkg.getPackageCode());
            stmt.setString(2, pkg.getPackageName());
            stmt.setString(3, pkg.getDescription());
            stmt.setString(4, pkg.getLocation());
            stmt.setDouble(5, pkg.getPrice());
            stmt.setString(6, pkg.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Package> getAllPackages() {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Package pkg = new Package(
                        rs.getInt("package_id"),
                        rs.getString("package_code"),
                        rs.getString("package_name"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getDouble("price"),
                        rs.getString("status")
                );
                packages.add(pkg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return packages;
    }

    public static void updatePackage(Package pkg) {
        String query = "UPDATE packages SET package_name = ?, description = ?, location = ?, price = ?, status = ? WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, pkg.getPackageName());
            stmt.setString(2, pkg.getDescription());
            stmt.setString(3, pkg.getLocation());
            stmt.setDouble(4, pkg.getPrice());
            stmt.setString(5, pkg.getStatus());
            stmt.setInt(6, pkg.getPackageId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePackage(int packageId) {
        String query = "DELETE FROM packages WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, packageId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
