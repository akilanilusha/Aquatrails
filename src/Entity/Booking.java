package Entity;

import java.util.Date;

/**
 *
 * @author malindu
 */
public class Booking {
    private int bookingId;
    private Date visitDate;
    private String visitorName;
    private String visitorId;
    private String packageName;
    private double price;
    private String status;

    // Constructors
    public Booking() {}

    public Booking(Date visitDate, String visitorName, String visitorId, String packageName, double price, String status) {
        this.visitDate = visitDate;
        this.visitorName = visitorName;
        this.visitorId = visitorId;
        this.packageName = packageName;
        this.price = price;
        this.status = status;
    }

    // Getters and setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public Date getVisitDate() { return visitDate; }
    public void setVisitDate(Date visitDate) { this.visitDate = visitDate; }

    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }

    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
