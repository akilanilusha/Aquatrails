/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author akilanilusha
 */
public class SeaConditionPanel extends JPanel {

    private static final String API_KEY = "STROMGLASS_API_KEY";
    private static final String API_URL = "https://api.stormglass.io/v2/weather/point?lat=58.7984&lng=17.8081&params=waveHeight,waterTemperature";

    public SeaConditionPanel() {
        setLayout(new GridLayout(2, 1));

        List<String> times = new ArrayList<>();
        List<Double> waveHeights = new ArrayList<>();
        List<Double> waterTemps = new ArrayList<>();

        try {
            // Fetch API data
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", API_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();
            conn.disconnect();

            JSONObject json = new JSONObject(content.toString());
            JSONArray hours = json.getJSONArray("hours");

            for (int i = 0; i < hours.length(); i++) {
                JSONObject hourData = hours.getJSONObject(i);
                String time = hourData.getString("time").substring(11, 16); //  "14:00"

                if (hourData.has("waveHeight") && hourData.has("waterTemperature")) {
                    JSONObject waveHeight = hourData.getJSONObject("waveHeight");
                    JSONObject waterTemp = hourData.getJSONObject("waterTemperature");

                    if (waveHeight.has("noaa") && waterTemp.has("noaa")) {
                        times.add(time);
                        waveHeights.add(waveHeight.getDouble("noaa"));
                        waterTemps.add(waterTemp.getDouble("noaa"));
                    }
                }
            }

            // Create and add chart panels
            add(createChartPanel("Wave Height", "Height (m)", "Wave Height (m)", times, waveHeights));
            add(createChartPanel("Water Temperature", "Temperature (°C)", "Water Temp (°C)", times, waterTemps));

        } catch (Exception e) {
            e.printStackTrace();
            add(new JLabel("Failed to load sea condition data."));
        }
    }

    private ChartPanel createChartPanel(String title, String yAxisLabel, String seriesName, List<String> times, List<Double> values) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < times.size(); i++) {
            dataset.addValue(values.get(i), seriesName, times.get(i));
        }
        JFreeChart chart = ChartFactory.createLineChart(
                title, "Time", yAxisLabel, dataset
        );
        return new ChartPanel(chart);
    }
}
