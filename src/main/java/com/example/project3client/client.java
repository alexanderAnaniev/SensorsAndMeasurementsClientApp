package com.example.project3client;

import com.github.javafaker.Faker;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class client {
    public static void main(String[] args) {
        Random random = new Random();
        Faker faker = new Faker();
        final String sensorName = faker.name().firstName();
        registerSensor(sensorName);

        int min = -100;
        int max = 100;
        int diapason = max + Math.abs(min) + 1;
        for (int i = 0; i < 1000; i++) {
            System.out.println("Измерение успешно отправлено " + i);
            sendMeasurements((Math.random() * diapason) - max, random.nextBoolean(), sensorName);
        }
    }
        private static void registerSensor(String sensorName){
            final String url = "http://localhost:8080/sensors/registration";
            Map<String,Object> jsonData = new HashMap<>();
            jsonData.put("name", sensorName);

            makePostRequestWithJSONData(url,jsonData);
        }

        private static void sendMeasurements ( double value, boolean raining, String sensorName){
            final String url = "http://localhost:8080/measurements/add";
            Map<String,Object> jsonData = new HashMap<>();
            jsonData.put("value", value);
            jsonData.put("raining", raining);
            jsonData.put("sensor",Map.of("name",sensorName));

            makePostRequestWithJSONData(url,jsonData);
        }
        private static void makePostRequestWithJSONData(String url,Map<String,Object> jsonData) {
            final RestTemplate restTemplate = new RestTemplate();

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);
            try {
                restTemplate.postForObject(url, request, String.class);

            } catch (HttpClientErrorException e) {
                System.out.println("ERROR!");
                System.out.println(e.getMessage());
            }
        }

    }




