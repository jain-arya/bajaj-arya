package com.example.webhookapp.service;

import com.example.webhookapp.model.WebhookResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void processWebhook() {
        String generateWebhookUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "arya jain");
        requestBody.put("regNo", "0827CS221052");
        requestBody.put("email", "aryajain221087@acropolis.in");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(generateWebhookUrl, entity, WebhookResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            WebhookResponse webhookResponse = response.getBody();
            System.out.println("Webhook URL: " + webhookResponse.getWebhook());
            System.out.println("AccessToken: " + webhookResponse.getAccessToken());

            String finalSQL = "SELECT p.AMOUNT AS SALARY, " +
                    "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
                    "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                    "d.DEPARTMENT_NAME " +
                    "FROM PAYMENTS p " +
                    "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                    "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                    "WHERE DAY(p.PAYMENT_TIME) != 1 " +
                    "ORDER BY p.AMOUNT DESC " +
                    "LIMIT 1;";

            submitFinalQuery(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), finalSQL);
        } else {
            System.err.println("Failed to generate webhook");
        }
    }

    private void submitFinalQuery(String webhookUrl, String accessToken, String finalQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); 

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Successfully submitted final query!");
        } else {
            System.err.println("Failed to submit final query. Response: " + response.getBody());
        }
    }
}
