package Entity;

/**
 *
 * @author hp
 */

public class Guider {
    private int id;
    private String name;
    private String dateOfBirth; // Consider using java.time.LocalDate
    private String location;
    private String packageName;
    private boolean isActive;
    private String imageBase64;

    // Constructors
    public Guider() {}

    public Guider(int id, String name, String dateOfBirth, String location, String packageName, boolean isActive, String imageBase64) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.location = location;
        this.packageName = packageName;
        this.isActive = isActive;
        this.imageBase64 = imageBase64;
    }

    public Guider(String name, String dateOfBirth, String location, String packageName, boolean isActive, String imageBase64) {
        this(-1, name, dateOfBirth, location, packageName, isActive, imageBase64);
    }

    // Getters and Setters

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getPackageName() { return packageName; }

    public void setPackageName(String packageName) { this.packageName = packageName; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    public String getImageBase64() { return imageBase64; }

    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
