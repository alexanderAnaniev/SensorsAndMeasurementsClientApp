package com.example.project3client;

import com.example.project3client.dto.MeasurementsDTO;
import com.example.project3client.dto.MeasurementsResponse;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class getMeasurementAndDrawChart {
    public static void main(String[] args) {
        List<Double> temperatures = getTemperaturesFromServer();
        drawChart(temperatures);
    }

    private static List<Double> getTemperaturesFromServer(){
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements";

        MeasurementsResponse jsonResponse = restTemplate.getForObject(url,MeasurementsResponse.class);

        if (jsonResponse == null || jsonResponse.getMeasurements() == null)
            return Collections.emptyList();

        return jsonResponse.getMeasurements().stream().map(MeasurementsDTO::getValue)
                .collect(Collectors.toList());
    }
    private static void drawChart(List<Double> temperatures){
        double[] xData = IntStream.range(0,temperatures.size()).asDoubleStream().toArray();
        double[] yData = temperatures.stream().mapToDouble(x ->x).toArray();

        XYChart chart = QuickChart.getChart("Temperatures","X","Y","temperatures",
                xData,yData);

        new SwingWrapper(chart).displayChart();
    }
}
