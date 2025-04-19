import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class WeatherApp {

    private static JTextArea weatherTextArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        weatherTextArea = new JTextArea();
        weatherTextArea.setEditable(false);
        weatherTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane weatherScroller = new JScrollPane(weatherTextArea);
        frame.add(weatherScroller);

        frame.setVisible(true);

        // Load weather after UI is visible
        loadWeatherForecast();
    }

    private static void loadWeatherForecast() {
        try {
            String API_KEY = "bba3a0c3b05e5455d102a635e22d80e7"; // Replace with your real API key
            String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=Colombo&units=metric&appid=" + API_KEY;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            parseAndDisplayWeather(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            weatherTextArea.setText("Failed to load weather data.\n" + e.getMessage());
        }
    }

    private static void parseAndDisplayWeather(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray list = jsonObject.getJSONArray("list");

            StringBuilder output = new StringBuilder();
            for (int i = 0; i < list.length(); i += 8) { // Every 8th item = next day (24 hrs / 3hr steps)
                JSONObject item = list.getJSONObject(i);
                String date = item.getString("dt_txt");
                double temp = item.getJSONObject("main").getDouble("temp");
                String description = item.getJSONArray("weather").getJSONObject(0).getString("description");

                output.append(String.format("Date: %s\nTemp: %.1f °C\nDescription: %s\n\n", date, temp, description));
            }

            weatherTextArea.setText(output.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            weatherTextArea.setText("Error parsing weather data.\n" + e.getMessage());
        }
    }
}
