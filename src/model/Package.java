/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;

/**
 *
 * @author akilanilusha
 */
public class Package {
        public void fetchPackagesFromDatabase(JComboBox<String> packageComboBox) {
        // Database connection (Replace with your actual database credentials)

        String query = "SELECT package_name FROM package"; // Adjust table and column names if necessary
        ResultSet rs = DatabaseConnection.searchData(query);

        try {
            if (rs != null) {
                while (rs.next()) {
                    String packageName = rs.getString("package_name");
                    packageComboBox.addItem(packageName);
//                    System.out.println(packageName);
                }
            }
        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Error fetching packages: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
