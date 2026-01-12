package com.f1.carservice; // <--- Matches your existing folder structure

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class CarTelemetryProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final Random random = new Random();
    private final String CAR_ID = "VER-33";

    public CarTelemetryProducer(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public record TelemetryData(String carId, double speedKmh, double rpm, double tireTempC, long timestamp) {}

    @Scheduled(fixedRate = 100)
    public void streamSensorData() {
        double speed = 200 + random.nextDouble() * 140;
        double rpm = 10000 + random.nextDouble() * 5000;
        double tireTemp = 90 + random.nextDouble() * 30;

        TelemetryData data = new TelemetryData(CAR_ID, speed, rpm, tireTemp, System.currentTimeMillis());

        System.out.println("SENDING: " + data);

        kafkaTemplate.send("f1-telemetry", CAR_ID, data);
    }
}