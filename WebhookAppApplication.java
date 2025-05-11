package com.example.webhookapp;

import com.example.webhookapp.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebhookAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookAppApplication.class, args);
    }

    @Bean
    CommandLineRunner run(WebhookService webhookService) {
        return args -> {
            webhookService.processWebhook();
        };
    }
}
