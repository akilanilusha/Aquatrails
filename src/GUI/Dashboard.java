/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import API.SeaConditionPanel;
import GUI.components.booking.BookingCardRow;
import GUI.components.guiders.GuiderCardRow;
import GUI.components.packge.PackageCardRow;
import GUI.components.user.UserCardRow;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.DatabaseConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Base64;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author akilanilusha
 */
public final class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    private final CardLayout cardLayout;
    // Logger for error handling
    private final String apiKey = "bba3a0c3b05e5455d102a635e22d80e7"; // Important:  Replace with your actual API key here.

    public Dashboard() {
        initComponents();

        cardLayout = new CardLayout();
        jPanel6.setLayout(cardLayout);

        jPanel6.add(jPanel2, "dashboard");
        jPanel6.add(jPanel3, "booking");
        jPanel6.add(jPanel4, "packages");
        jPanel6.add(jPanel5, "guiders");
        jPanel6.add(jPanel8, "userManagement");

        loadSeaConditionChart();
        loadWeatherForecastList();
        loadBookingDetails();
        loadPackageDetails();
        loadguiderDetails();
        DisplatTime();
        loadLableValues();
        loadUserDetails();
    }

    private void loadLableValues() {

        String bookingquery = "SELECT COUNT(*) AS count FROM booking WHERE visit_date = CURDATE()";
        ResultSet rsb = DatabaseConnection.searchData(bookingquery);

        try {
            if (rsb != null && rsb.next()) {
                int count = rsb.getInt("count");
                System.out.println(count);
                todaybooking.setText(String.valueOf(count));
            } else {
                todaybooking.setText("0");
            }
        } catch (Exception e) {
            todaybooking.setText("Error");
            e.printStackTrace();
        }
        String packagequery = "SELECT COUNT(*) AS count FROM packages WHERE status = 'Active'";
        ResultSet rsp = DatabaseConnection.searchData(packagequery);

        try {
            if (rsp != null && rsp.next()) {
                int count = rsp.getInt("count");
                avilablePackage.setText(String.valueOf(count));
            } else {
                avilablePackage.setText("0");
            }
        } catch (Exception e) {
            avilablePackage.setText("Error");
            e.printStackTrace();
        }

        String guidQuery = "SELECT COUNT(*) AS count FROM guider WHERE is_active = 1";
        ResultSet grs = DatabaseConnection.searchData(guidQuery);

        try {
            if (grs != null && grs.next()) {
                int count = grs.getInt("count");
                avilableguide.setText(String.valueOf(count));
            } else {
                avilableguide.setText("0");
            }
        } catch (Exception e) {
            avilableguide.setText("Error");
            e.printStackTrace();
        }
    }

    private void DisplatTime() {
        Date.setHorizontalAlignment(SwingConstants.RIGHT);
        Time.setHorizontalAlignment(SwingConstants.RIGHT);
        Datetime.setLayout(new GridLayout(2, 1)); // Two rows: one for date, one for time
        Datetime.add(Date);
        Datetime.add(Time);

        Timer timer = new Timer(1000, e -> {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            Date.setText(dateFormat.format(now));
            Time.setText(timeFormat.format(now));
        });
        timer.start();
    }

    private void loadBookingDetails() {
        loadBookingCard.setLayout(new BoxLayout(loadBookingCard, BoxLayout.Y_AXIS));

        loadBookingCard.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadBookingCards();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    private void loadUserDetails() {
        loadUserCard.setLayout(new BoxLayout(loadUserCard, BoxLayout.Y_AXIS));

        loadUserCard.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadUserCards();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    private void loadPackageDetails() {
        loadPackageCard.setLayout(new BoxLayout(loadPackageCard, BoxLayout.Y_AXIS));

        loadPackageCard.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadPackageCards();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    private void loadguiderDetails() {
        loadGuiderCard.setLayout(new BoxLayout(loadGuiderCard, BoxLayout.Y_AXIS));

        loadGuiderCard.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadGuiderCards();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    public void loadBookingCards() {
        loadBookingCard.removeAll();

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            // Include 'status' in the query
            ResultSet rs = stmt.executeQuery(
                    "SELECT booking_id, visitor_name, package_name, price, status, visit_date "
                    + "FROM booking "
                    + "WHERE status != 'confirmed' "
                    + "ORDER BY visit_date DESC"
            );
            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                String visitorName = rs.getString("visitor_name");
                String packageName = rs.getString("package_name");
                double price = rs.getDouble("price");
                String status = rs.getString("status"); // Fetch status

                // Pass the status to the CardRow constructor
                BookingCardRow card = new BookingCardRow(bookingId, visitorName, packageName, price, status);

                loadBookingCard.add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Refresh the panel to show the newly added cards
        loadBookingCard.revalidate();
        loadBookingCard.repaint();
    }

    public void loadPackageCards() {
        loadPackageCard.removeAll();

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT package_id, package_code, package_name, description, location, price, status FROM packages");

            while (rs.next()) {
                int packageId = rs.getInt("package_id");
                String packageCode = rs.getString("package_code");
                String packageName = rs.getString("package_name");
                String description = rs.getString("description");
                String location = rs.getString("location");
                double price = rs.getDouble("price");
                String status = rs.getString("status");

                PackageCardRow card = new PackageCardRow(packageId, packageCode, packageName, description, location, price, status);

                loadPackageCard.add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadPackageCard.revalidate();
        loadPackageCard.repaint();
    }

    public void loadGuiderCards() {
        loadGuiderCard.removeAll();

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT guider_id, name, date_of_birth,  TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) AS age, location, package_name, is_active, image_base64 FROM guider");

            while (rs.next()) {
                int guiderId = rs.getInt("guider_id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String location = rs.getString("location");
                String packageName = rs.getString("package_name");
                boolean isActive = rs.getBoolean("is_active");
                String imageBase64 = rs.getString("image_base64");

           
                GuiderCardRow card = new GuiderCardRow(guiderId, name, age, location, packageName, isActive, imageBase64);
                loadGuiderCard.add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadGuiderCard.revalidate();
        loadGuiderCard.repaint();
    }

    private void loadSeaConditionChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Sample data (you can dynamically load this from database or API later)
        dataset.addValue(1.2, "Wave Height (m)", "08:00");
        dataset.addValue(1.5, "Wave Height (m)", "10:00");
        dataset.addValue(1.0, "Wave Height (m)", "12:00");
        dataset.addValue(0.8, "Wave Height (m)", "14:00");

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Sea Conditions - Wave Height & Water Temperature",
                "Time",
                "Measurement",
                dataset
        );

//        SeaConditionPanel seaConditionPanel = new SeaConditionPanel();
//
//        // Make sure to add the seaConditionPanel to the jPanel6 (which uses CardLayout)
//        seeConditionChart.setLayout(new java.awt.BorderLayout());
//        seeConditionChart.add(seaConditionPanel, BorderLayout.CENTER); // Adding the chart to the panel
//        jPanel6.add(seeConditionChart, "seaConditionChart"); // Add the seaConditionChart panel to the CardLayout
//        cardLayout.show(jPanel6, "seaConditionChart"); //
        SeaConditionPanel chartPanel = new SeaConditionPanel();
//        ChartPanel chartPanel = new ChartPanel(lineChart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(380, 160)); // Adjust to fit your panel

        seeConditionChart.removeAll(); // Clear previous chart if any
        seeConditionChart.add(chartPanel, java.awt.BorderLayout.CENTER);
        seeConditionChart.validate();
    }

    private void loadWeatherForecastList() {
        new Thread(() -> { // Perform network operation in a separate thread
            try {
                if (apiKey.equals("YOUR_API_KEY")) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Please enter a valid OpenWeatherMap API key.", "API Key Required", JOptionPane.ERROR_MESSAGE);
                    });
                    return; // Stop execution if API key is missing
                }

                String city = "Colombo";
                String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=" + apiKey;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                reader.close();

                String jsonResponse = json.toString(); // Store the raw JSON
                //LOGGER.log(Level.INFO, "Weather API Response: {0}", jsonResponse); // Log the response

                JSONObject jsonObj = new JSONObject(jsonResponse);
                JSONArray list = jsonObj.getJSONArray("list");

                JPanel forecastListPanel = new JPanel();
                forecastListPanel.setLayout(new BoxLayout(forecastListPanel, BoxLayout.Y_AXIS));

                for (int i = 0; i < list.length(); i++) {
                    JSONObject entry = list.getJSONObject(i);
                    String dateTime = entry.getString("dt_txt");
                    JSONObject main = entry.getJSONObject("main");
                    double temp = main.getDouble("temp");

                    JSONArray weatherArr = entry.getJSONArray("weather");
                    String description = weatherArr.getJSONObject(0).getString("description");

                    JLabel label = new JLabel("⏰ " + dateTime + " | 🌡️ " + temp + "°C | " + description);
                    label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    forecastListPanel.add(label);
                }

                SwingUtilities.invokeLater(() -> { // Update the UI on the EDT
                    weatherPanel.setViewportView(forecastListPanel);
                });

            } catch (Exception e) {
                // LOGGER.log(Level.SEVERE, "Error loading weather forecast: {0}", e.getMessage()); // Log the error
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Failed to load weather data. Please check your network connection and API key.", "Error", JOptionPane.ERROR_MESSAGE);
                });

            }
        }).start();
    }

    public void loadUserCards() {
        loadUserCard.removeAll();

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT user_id, username, user_role, nic, email, status FROM user");

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String role = rs.getString("user_role");
                String nic = rs.getString("nic");
                String email = rs.getString("email");
                String status = rs.getString("status");

                // Assuming UserCardRow constructor accepts these parameters
                UserCardRow card = new UserCardRow(userId, username, role, nic, email, status);
                loadUserCard.add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadUserCard.revalidate();
        loadUserCard.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        navbar = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        weatherDetails = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        weatherPanel = new javax.swing.JScrollPane();
        weatherScroller = new javax.swing.JScrollBar();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        seeConditionChart = new javax.swing.JPanel();
        TodayBookings = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        todaybooking = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        avilablePackage = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        avilableguide = new javax.swing.JLabel();
        Datetime = new javax.swing.JPanel();
        Date = new javax.swing.JLabel();
        Time = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        loadBookingCard = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        newPackage = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        loadPackageCard = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        newGuide = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        loadGuiderCard = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        newUser = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        loadUserCard = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 51));

        header.setBackground(new java.awt.Color(0, 0, 51));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Hiragino Mincho ProN", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("AQUATRAILS HOTEL MANAGEMENT SYSYTEM");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(552, 552, 552))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        navbar.setBackground(new java.awt.Color(0, 0, 51));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Packages");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Dashboard");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 51, 51));
        jButton3.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Booking");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        logoutButton.setBackground(new java.awt.Color(255, 72, 24));
        logoutButton.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("Log Out");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(51, 51, 51));
        jButton5.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Guiders");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(51, 51, 51));
        jButton7.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("User Management");
        jButton7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout navbarLayout = new javax.swing.GroupLayout(navbar);
        navbar.setLayout(navbarLayout);
        navbarLayout.setHorizontalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addContainerGap())
        );
        navbarLayout.setVerticalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jLabel2.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 153));
        jLabel2.setText("DASHBOARD");

        weatherDetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel5.setText("Weather Details");

        weatherPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        weatherPanel.setViewportView(weatherScroller);

        javax.swing.GroupLayout weatherDetailsLayout = new javax.swing.GroupLayout(weatherDetails);
        weatherDetails.setLayout(weatherDetailsLayout);
        weatherDetailsLayout.setHorizontalGroup(
            weatherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(weatherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weatherPanel)
                    .addGroup(weatherDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 213, Short.MAX_VALUE)))
                .addContainerGap())
        );
        weatherDetailsLayout.setVerticalGroup(
            weatherDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weatherDetailsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(weatherPanel)
                .addContainerGap())
        );

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabel10.setText("Sea Condition");

        seeConditionChart.setSize(new java.awt.Dimension(400, 400));
        seeConditionChart.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seeConditionChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(seeConditionChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        TodayBookings.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));
        TodayBookings.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                TodayBookingsAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        TodayBookings.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel4.setText("Today Bookings");
        TodayBookings.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        todaybooking.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N
        todaybooking.setText("44");
        todaybooking.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                todaybookingAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        TodayBookings.add(todaybooking, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, -1, -1));

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel11.setText("Avilable Packages");
        TodayBookings.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, -1, -1));

        avilablePackage.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N
        avilablePackage.setText("44");
        TodayBookings.add(avilablePackage, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, -1, -1));

        jLabel13.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel13.setText("Avilable Guides");
        TodayBookings.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        avilableguide.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N
        avilableguide.setText("44");
        TodayBookings.add(avilableguide, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, -1, -1));

        Date.setFont(new java.awt.Font("Kailasa", 1, 24)); // NOI18N
        Date.setForeground(new java.awt.Color(0, 0, 102));
        Date.setText("Time");

        Time.setFont(new java.awt.Font("Kailasa", 1, 24)); // NOI18N
        Time.setForeground(new java.awt.Color(0, 0, 102));
        Time.setText("Date");

        javax.swing.GroupLayout DatetimeLayout = new javax.swing.GroupLayout(Datetime);
        Datetime.setLayout(DatetimeLayout);
        DatetimeLayout.setHorizontalGroup(
            DatetimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DatetimeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DatetimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DatetimeLayout.createSequentialGroup()
                        .addComponent(Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(43, 43, 43))
                    .addGroup(DatetimeLayout.createSequentialGroup()
                        .addComponent(Time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        DatetimeLayout.setVerticalGroup(
            DatetimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DatetimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(TodayBookings, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Datetime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(weatherDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Datetime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TodayBookings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weatherDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jPanel3AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel3.setBackground(new java.awt.Color(0, 102, 255));
        jLabel3.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 153, 153));
        jLabel3.setText("BOOKING");

        jButton6.setBackground(new java.awt.Color(0, 102, 255));
        jButton6.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("New Booking");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        loadBookingCard.setBackground(new java.awt.Color(204, 204, 204));
        loadBookingCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        loadBookingCard.setForeground(new java.awt.Color(255, 255, 255));
        loadBookingCard.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                loadBookingCardAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        loadBookingCard.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(loadBookingCard);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1101, Short.MAX_VALUE))
                .addGap(24, 24, 24))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel6.setBackground(new java.awt.Color(0, 102, 255));
        jLabel6.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 153, 153));
        jLabel6.setText("PACKAGES");

        newPackage.setBackground(new java.awt.Color(0, 102, 255));
        newPackage.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        newPackage.setForeground(new java.awt.Color(255, 255, 255));
        newPackage.setText("New Package");
        newPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPackageActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        loadPackageCard.setBackground(new java.awt.Color(204, 204, 204));
        loadPackageCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        loadPackageCard.setForeground(new java.awt.Color(255, 255, 255));
        loadPackageCard.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                loadPackageCardAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        loadPackageCard.setLayout(new java.awt.BorderLayout());
        jScrollPane2.setViewportView(loadPackageCard);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newPackage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1104, Short.MAX_VALUE))
                .addGap(20, 20, 20))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newPackage, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel7.setBackground(new java.awt.Color(0, 102, 255));
        jLabel7.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 153, 153));
        jLabel7.setText("GUIDERS");

        newGuide.setBackground(new java.awt.Color(0, 102, 255));
        newGuide.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        newGuide.setForeground(new java.awt.Color(255, 255, 255));
        newGuide.setText("New Guider");
        newGuide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGuideActionPerformed(evt);
            }
        });

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        loadGuiderCard.setBackground(new java.awt.Color(204, 204, 204));
        loadGuiderCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        loadGuiderCard.setForeground(new java.awt.Color(255, 255, 255));
        loadGuiderCard.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                loadGuiderCardAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        loadGuiderCard.setLayout(new java.awt.BorderLayout());
        jScrollPane3.setViewportView(loadGuiderCard);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 551, Short.MAX_VALUE)
                        .addComponent(newGuide, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(7, 7, 7)
                .addComponent(newGuide, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel8.setBackground(new java.awt.Color(0, 102, 255));
        jLabel8.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 153, 153));
        jLabel8.setText("USER MANAGEMENT");

        newUser.setBackground(new java.awt.Color(0, 102, 255));
        newUser.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        newUser.setForeground(new java.awt.Color(255, 255, 255));
        newUser.setText("New User");
        newUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserActionPerformed(evt);
            }
        });

        jScrollPane4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        loadUserCard.setBackground(new java.awt.Color(204, 204, 204));
        loadUserCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        loadUserCard.setForeground(new java.awt.Color(255, 255, 255));
        loadUserCard.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                loadUserCardAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        loadUserCard.setLayout(new java.awt.BorderLayout());
        jScrollPane4.setViewportView(loadUserCard);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(newUser, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(508, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newUser, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(32, 32, 32)))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(38, 38, 38)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(24, 24, 24)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(navbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel6, "dashboard");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel6, "booking");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel6, "packages");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel3AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jPanel3AncestorAdded
        // TODO add your handling code here:


    }//GEN-LAST:event_jPanel3AncestorAdded

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        JDateChooser dateChooser = new JDateChooser();
        JTextField visitorNameField = new JTextField();
        JTextField visitorIdField = new JTextField();
        JComboBox<String> packageComboBox = new JComboBox<>();
        JTextField priceField = new JTextField();
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"confirmed", "cancelled", "ongoing"});

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2)); // Adjusted for 6 fields

        panel.add(new JLabel("Select Date:"));
        panel.add(dateChooser);

        panel.add(new JLabel("Visitor Name:"));
        panel.add(visitorNameField);

        panel.add(new JLabel("Visitor ID:"));
        panel.add(visitorIdField);

        panel.add(new JLabel("Select Package:"));
        panel.add(packageComboBox);

        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

        // Fetch packages from the database
        fetchPackagesFromDatabase(packageComboBox);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(this, "New Booking");

        dialog.setSize(500, 350);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            Date selectedDate = dateChooser.getDate();
            String visitorName = visitorNameField.getText();
            String visitorId = visitorIdField.getText();
            String selectedPackage = (String) packageComboBox.getSelectedItem();
            String priceText = priceField.getText();
            String status = (String) statusComboBox.getSelectedItem();

            if (selectedDate == null || visitorName.isEmpty() || visitorId.isEmpty() || selectedPackage == null || priceText.isEmpty() || status == null) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double price = Double.parseDouble(priceText);

                    String insertQuery = "INSERT INTO booking (visit_date, visitor_name, visitor_id, package_name, price, status) "
                            + "VALUES ('" + new java.sql.Date(selectedDate.getTime()) + "', '"
                            + visitorName + "', '" + visitorId + "', '" + selectedPackage + "', " + price + ", '" + status + "')";

                    DatabaseConnection.insertData(insertQuery);
                    JOptionPane.showMessageDialog(this, "Booking successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBookingCards();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed


    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Close the current frame
            this.dispose();  // Directly dispose of the current frame

            // Open the login frame
            new LoginUser().setVisible(true);
        }
    }//GEN-LAST:event_logoutButtonActionPerformed


    private void loadBookingCardAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_loadBookingCardAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_loadBookingCardAncestorAdded

    private void newPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPackageActionPerformed
        // TODO add your handling code here:

        JTextField packageCodeField = new JTextField();
        JTextField packageNameField = new JTextField();
        JTextArea packageDescriptionArea = new JTextArea(5, 30); // Increase rows to 5 and columns to 30
        packageDescriptionArea.setLineWrap(true);
        packageDescriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(packageDescriptionArea);
        JTextField locationField = new JTextField();
        JTextField priceField = new JTextField();

        String[] statuses = {"Active", "Pending", "Closed"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Package Code:"));
        panel.add(packageCodeField);

        panel.add(new JLabel("Package Name:"));
        panel.add(packageNameField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionScroll);

        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(this, "Add New Package");

        dialog.setSize(500, 350);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            String code = packageCodeField.getText();
            String name = packageNameField.getText();
            String description = packageDescriptionArea.getText();
            String location = locationField.getText();
            String priceText = priceField.getText();
            String status = (String) statusComboBox.getSelectedItem();

            // Validation
            if (code.isEmpty() || name.isEmpty() || description.isEmpty() || location.isEmpty() || priceText.isEmpty() || status == null) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double price = Double.parseDouble(priceText);

                    String insertQuery = "INSERT INTO packages (package_code, package_name, description, location, price, status) VALUES ('"
                            + code + "', '"
                            + name + "', '"
                            + description + "', '"
                            + location + "', "
                            + price + ", '"
                            + status + "')";

                    DatabaseConnection.insertData(insertQuery);
                    JOptionPane.showMessageDialog(this, "Package added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadPackageCards();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_newPackageActionPerformed

    private void loadPackageCardAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_loadPackageCardAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_loadPackageCardAncestorAdded

    private void newGuideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGuideActionPerformed
        // TODO add your handling code here:
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 40)); // Increase height

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        JTextField locationField = new JTextField(20);
        JComboBox<String> packageComboBox = new JComboBox<>();
        JCheckBox activeCheckbox = new JCheckBox("Is Active", true);

        // Load packages into combo box
        try {
            ResultSet rs = DatabaseConnection.getConnection().createStatement().executeQuery("SELECT package_name FROM packages");
            while (rs.next()) {
                packageComboBox.addItem(rs.getString("package_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Image upload and preview
        JLabel imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(150, 150));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        final String[] imageBase64 = {null};

        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    byte[] imageBytes = Files.readAllBytes(file.toPath());
                    imageBase64[0] = Base64.getEncoder().encodeToString(imageBytes);

                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaled));

                    JOptionPane.showMessageDialog(null, "Image uploaded successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to read image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Date of Birth:"));
        inputPanel.add(dateChooser);

        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);

        inputPanel.add(new JLabel("Package:"));
        inputPanel.add(packageComboBox);

        inputPanel.add(new JLabel("Active:"));
        inputPanel.add(activeCheckbox);

        inputPanel.add(new JLabel("Image:"));
        inputPanel.add(uploadButton);

        JPanel imagePanel = new JPanel();
        imagePanel.add(imagePreviewLabel);

        JPanel combinedPanel = new JPanel(new BorderLayout(10, 10));
        combinedPanel.add(inputPanel, BorderLayout.NORTH);
        combinedPanel.add(imagePanel, BorderLayout.CENTER);

        JOptionPane optionPane = new JOptionPane(combinedPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(this, "Add New Guider");

        dialog.setSize(600, 600);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            String name = nameField.getText();
            Date selectedDate = dateChooser.getDate();
            String location = locationField.getText();
            String packageName = (String) packageComboBox.getSelectedItem();
            boolean isActive = activeCheckbox.isSelected();

            if (name.isEmpty() || selectedDate == null || location.isEmpty() || packageName == null) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dob = sdf.format(selectedDate);

                    String insertQuery = "INSERT INTO guider (name, date_of_birth, location, package_name, is_active, image_base64) VALUES ('"
                            + name + "', '"
                            + dob + "', '"
                            + location + "', '"
                            + packageName + "', "
                            + (isActive ? "TRUE" : "FALSE") + ", "
                            + (imageBase64[0] != null ? ("'" + imageBase64[0] + "'") : "NULL") + ")";

                    DatabaseConnection.insertData(insertQuery);
                    JOptionPane.showMessageDialog(this, "Guider added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadGuiderCards();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving guider.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_newGuideActionPerformed

    private void loadGuiderCardAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_loadGuiderCardAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_loadGuiderCardAncestorAdded

    private void newUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserActionPerformed

        Font inputFont = new Font("SansSerif", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(250, 40); // Taller fields

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(fieldSize);
        usernameField.setFont(inputFont);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);
        passwordField.setFont(inputFont);

        JTextField nicField = new JTextField();
        nicField.setPreferredSize(fieldSize);
        nicField.setFont(inputFont);

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);
        emailField.setFont(inputFont);

        String[] roles = {
            "Administrator", "Receptionist", "Hotel Manager", "Guest", "Maintenance Staff"
        };
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(fieldSize);
        roleComboBox.setFont(inputFont);

        String[] statusOptions = {"Active", "Leave", "Suspended"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setPreferredSize(fieldSize);
        statusComboBox.setFont(inputFont);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("NIC:"));
        inputPanel.add(nicField);

        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(roleComboBox);

        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusComboBox);

        JOptionPane optionPane = new JOptionPane(inputPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(this, "Add New User");
        dialog.setSize(600, 400);
        dialog.setVisible(true);

        if (optionPane.getValue() != null && optionPane.getValue().equals(JOptionPane.OK_OPTION)) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String nic = nicField.getText();
            String email = emailField.getText();
            String role = (String) roleComboBox.getSelectedItem();
            String status = (String) statusComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty() || nic.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    String insertQuery = "INSERT INTO user (username, password, user_role, nic, email, status) VALUES ('"
                            + username + "', '"
                            + password + "', '"
                            + role + "', '"
                            + nic + "', '"
                            + email + "', '"
                            + status + "')";

                    DatabaseConnection.insertData(insertQuery);
                    JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUserCards();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving user.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }


    }//GEN-LAST:event_newUserActionPerformed

    private void loadUserCardAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_loadUserCardAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_loadUserCardAncestorAdded

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel6, "guiders");

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String inputKey = JOptionPane.showInputDialog(this, "Enter access key to continue:");
        String validKey = "abc";
        if (inputKey != null && inputKey.equals(validKey)) {
            cardLayout.show(jPanel6, "userManagement");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid access key!", "Access Denied", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void TodayBookingsAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_TodayBookingsAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_TodayBookingsAncestorAdded

    private void todaybookingAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_todaybookingAncestorAdded
        // TODO add your handling code here:

    }//GEN-LAST:event_todaybookingAncestorAdded

    private void fetchPackagesFromDatabase(JComboBox<String> packageComboBox) {

        String query = "SELECT package_name FROM packages";
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
            JOptionPane.showMessageDialog(this, "Error fetching packages: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Date;
    private javax.swing.JPanel Datetime;
    private javax.swing.JLabel Time;
    private javax.swing.JPanel TodayBookings;
    private javax.swing.JLabel avilablePackage;
    private javax.swing.JLabel avilableguide;
    private javax.swing.JPanel header;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel loadBookingCard;
    private javax.swing.JPanel loadGuiderCard;
    private javax.swing.JPanel loadPackageCard;
    private javax.swing.JPanel loadUserCard;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel navbar;
    private javax.swing.JButton newGuide;
    private javax.swing.JButton newPackage;
    private javax.swing.JButton newUser;
    private javax.swing.JPanel seeConditionChart;
    private javax.swing.JLabel todaybooking;
    private javax.swing.JPanel weatherDetails;
    private javax.swing.JScrollPane weatherPanel;
    private javax.swing.JScrollBar weatherScroller;
    // End of variables declaration//GEN-END:variables

    private void loadBookingsFromDatabase(DefaultTableModel model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private int calculateAge(Date dob) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
