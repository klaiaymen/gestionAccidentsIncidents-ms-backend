package com.transtu.reclamationservice.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class VonageSmsService {
    @Value("${vonage.api.key}")
    private String apiKey;

    @Value("${vonage.api.secret}")
    private String apiSecret;

    @Value("${vonage.sms.sender}")
    private String sender;

    public void sendSmsToMultipleRecipients(String[] recipients, String message) {
        VonageClient vonageClient = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();

        Arrays.stream(recipients).forEach(recipient -> {
            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(
                    new com.vonage.client.sms.messages.TextMessage(
                            sender,
                            recipient,
                            message)
            );

            System.out.println("Message envoyé à " + recipient + ". Status : " + response.getMessages().get(0).getStatus());
        });
    }
}
