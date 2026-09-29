package com.example.pipedetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Automated Public Utility Drone Pipe Leak Detection
 * and Ultrasonic Sealing System.
 *
 * Run with: mvn spring-boot:run
 * Then open: http://localhost:8080
 */
@SpringBootApplication
public class PipeDetectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PipeDetectionApplication.class, args);
    }
}
