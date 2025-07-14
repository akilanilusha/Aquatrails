package API;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeaConditionPanel extends JPanel {

private static final String API_KEY = "3631d5c4-1966-11f0-a906-0242ac130003-3631d61e-1966-11f0-a906-0242ac130003";
    private static final String API_URL = "https://api.stormglass.io/v2/weather/point?lat=58.7984&lng=17.8081&params=waveHeight,waterTemperature";

    public SeaConditionPanel() {
        setLayout(new GridLayout(2, 1, 10, 10)); // Two rows, spacing
        setBackground(new Color(245, 245, 245)); // Light background

        List<String> times = new ArrayList<>();
        List<Double> waveHeights = new ArrayList<>();
        List<Double> waterTemps = new ArrayList<>();

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
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
                String time = hourData.getString("time").substring(11, 16); // HH:mm format

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

            add(createModernChartPanel("🌊 Wave Height", "Time", "Height (m)", "Wave Height (m)", times, waveHeights));
            add(createModernChartPanel("🌡 Water Temperature", "Time", "Temperature (°C)", "Water Temp (°C)", times, waterTemps));

        } catch (Exception e) {
            e.printStackTrace();
            add(new JLabel("❌ Failed to load sea condition data."));
        }
    }

    private ChartPanel createModernChartPanel(String title, String xLabel, String yLabel,
                                              String seriesName, List<String> times, List<Double> values) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Reduce clutter: add every 3rd point
        for (int i = 0; i < times.size(); i += 3) {
            dataset.addValue(values.get(i), seriesName, times.get(i));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                title, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, true, true, false
        );

        // Chart title & background
        chart.setBackgroundPaint(new Color(245, 245, 245));
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));

        // Plot customization
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(180, 180, 180));
        plot.setDomainGridlinePaint(new Color(220, 220, 220));

        // Renderer - smooth red line
        LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
        renderer.setSeriesPaint(0, new Color(255, 80, 80));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        // X-axis labels font and rotation
        plot.getDomainAxis().setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        plot.getDomainAxis().setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4) // 45° angle
        );

        // Y-axis labels
        plot.getRangeAxis().setTickLabelFont(new Font("SansSerif", Font.PLAIN, 10));

        ChartPanel panel = new ChartPanel(chart);
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setPreferredSize(new Dimension(700, 300));

        return panel;
    }
}
