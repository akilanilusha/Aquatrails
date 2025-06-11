/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.User;
import DatabaseModel.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author akilanilusha
 */
public class UserDAO {

    public static boolean insertUser(User user) {
        String query = "INSERT INTO user (username, password, user_role, nic, email, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getNic());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(User user) {
        String query = "UPDATE user SET username=?, password=?, user_role=?, nic=?, email=?, status=? WHERE user_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getNic());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getStatus());
            stmt.setInt(7, user.getUserId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUser(int userId) {
        String query = "DELETE FROM user WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static List<User> getAllUsers() {
//        List<User> users = new ArrayList<>();
//        String query = "SELECT * FROM user";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                User user = new User(
//                        rs.getInt("user_id"),
//                        rs.getString("username"),
//                        rs.getString("password"),
//                        rs.getString("nic"),
//                        rs.getString("email"),
//                        rs.getString("user_role"),
//                        rs.getString("status")
//                );
//                users.add(user);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return users;
//    }
//
//    public static User getUserById(int userId) {
//        String query = "SELECT * FROM user WHERE user_id = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setInt(1, userId);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                return new User(
//                        rs.getInt("user_id"),
//                        rs.getString("username"),
//                        rs.getString("password"),
//                        rs.getString("nic"),
//                        rs.getString("email"),
//                        rs.getString("user_role"),
//                        rs.getString("status")
//                );
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
}