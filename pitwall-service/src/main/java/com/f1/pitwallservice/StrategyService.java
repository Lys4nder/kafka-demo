package com.f1.pitwallservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class StrategyService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public record TelemetryData(String carId, double speedKmh, double rpm, double tireTempC, long timestamp) {}

    @KafkaListener(topics = "f1-telemetry", groupId = "strategy-group-v1")
    public void analyze(String message) {
        try {
            TelemetryData data = objectMapper.readValue(message, TelemetryData.class);

            if (data.tireTempC() > 115.0) {
                System.err.println("ALARM: " + data.carId() + " TIRES OVERHEATING! " + String.format("%.2f", data.tireTempC()) + "°C");
            } else {
                System.out.println(data.carId() + " Status Nominal. Speed: " + (int)data.speedKmh() + " km/h");
            }

        } catch (Exception e) {
            System.err.println("Error parsing telemetry: " + e.getMessage());
        }
    }
}