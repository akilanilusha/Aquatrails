package DAO;

import Entity.Package;
import DatabaseModel.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageDAO extends CommonDAO<Package> {

    private static PackageDAO instance;

    private PackageDAO() {
    }

    public static synchronized PackageDAO getInstance() {
        if (instance == null) {
            instance = new PackageDAO();
        }
        return instance;
    }

    @Override
    public boolean insert(Package pkg) {
        String query = "INSERT INTO packages (package_code, package_name, description, location, price, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, pkg.getPackageCode());
            stmt.setString(2, pkg.getPackageName());
            stmt.setString(3, pkg.getDescription());
            stmt.setString(4, pkg.getLocation());
            stmt.setDouble(5, pkg.getPrice());
            stmt.setString(6, pkg.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Package pkg) {
        String query = "UPDATE packages SET package_code = ?, package_name = ?, description = ?, location = ?, price = ?, status = ? WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, pkg.getPackageCode());
            stmt.setString(2, pkg.getPackageName());
            stmt.setString(3, pkg.getDescription());
            stmt.setString(4, pkg.getLocation());
            stmt.setDouble(5, pkg.getPrice());
            stmt.setString(6, pkg.getStatus());
            stmt.setInt(7, pkg.getPackageId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int packageId) {
        String query = "DELETE FROM packages WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, packageId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Package> getAll() {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

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

    @Override
    public Package getById(int packageId) {
        String query = "SELECT * FROM packages WHERE package_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Package pkg = new Package(
                            rs.getInt("package_id"),
                            rs.getString("package_code"),
                            rs.getString("package_name"),
                            rs.getString("description"),
                            rs.getString("location"),
                            rs.getDouble("price"),
                            rs.getString("status")
                    );
                    return pkg;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
