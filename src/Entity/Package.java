package Entity;

/**
 *
 * @author kesha
 */
public class Package {
    private int packageId;
    private String packageCode;
    private String packageName;
    private String description;
    private String location;
    private double price;
    private String status;

    public Package(int packageId, String packageCode, String packageName, String description, String location, double price, String status) {
        this.packageId = packageId;
        this.packageCode = packageCode;
        this.packageName = packageName;
        this.description = description;
        this.location = location;
        this.price = price;
        this.status = status;
    }

    public Package(String packageCode, String packageName, String description, String location, double price, String status) {
        this(-1, packageCode, packageName, description, location, price, status);
    }

    // Getters and setters
    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }

    public String getPackageCode() { return packageCode; }
    public void setPackageCode(String packageCode) { this.packageCode = packageCode; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
