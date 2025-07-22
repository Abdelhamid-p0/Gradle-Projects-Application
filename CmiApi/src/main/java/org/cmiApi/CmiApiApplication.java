package org.cmiApi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.cmiApi", "org.banque"})
public class CmiApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmiApiApplication.class, args);
    }
}