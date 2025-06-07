package DatabaseModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;

/**
 *
 * @author akilanilusha
 */

//load packages form the database packages table
public class LoadPackage {
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
}
