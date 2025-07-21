package DatabaseModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;

/**
 *
 * @author akilanilusha
 */
public class LoadPackage {

    // Load package names into a JComboBox
    public void fetchPackagesFromDatabase(JComboBox<String> packageComboBox) {
        String query = "SELECT package_name FROM packages";
        ResultSet rs = DatabaseConnection.searchData(query);

        try {
            if (rs != null) {
                while (rs.next()) {
                    String packageName = rs.getString("package_name");
                    packageComboBox.addItem(packageName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // New: Return a Map of package_name => price
    public static Map<String, Double> getPackagePriceMap() {
        Map<String, Double> packagePrices = new HashMap<>();
        String query = "SELECT package_name, price FROM packages";
        ResultSet rs = DatabaseConnection.searchData(query);

        try {
            if (rs != null) {
                while (rs.next()) {
                    String name = rs.getString("package_name");
                    double price = rs.getDouble("price");
                    packagePrices.put(name, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return packagePrices;
    }
}
