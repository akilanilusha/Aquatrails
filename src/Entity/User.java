/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author akilanilusha
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String nic;
    private String email;
    private String role;
    private String status;

    public User() {}

    public User(int userId, String username, String password, String nic, String email, String role, String status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nic = nic;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public User(String username, String password, String nic, String email, String role, String status) {
        this.username = username;
        this.password = password;
        this.nic = nic;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

