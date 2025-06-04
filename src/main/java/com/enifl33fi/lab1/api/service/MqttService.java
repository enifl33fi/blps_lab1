package com.enifl33fi.lab1.api.service;

import com.enifl33fi.lab1.api.dto.request.EmailRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MqttService {
    private final ObjectMapper objectMapper;

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    private MqttClient mqttClient;

    @PostConstruct
    public void init() {
        try {
            mqttClient = new MqttClient(brokerUrl, "lab-sender");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            mqttClient.connect(options);
        } catch (MqttException e) {
            log.error("MQTT connection failed", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                log.warn("Error disconnecting MQTT client", e);
            }
        }
    }

    public void sendEmailOtpRequest(String email, String otp) {
        sendEmailRequest(email, otp, "Confirmation code");
    }

    public void sendEmailExpiredRequest(String email, String otp) {
        sendEmailRequest(email, otp, "Your subscription has expired");
    }

    public void sendEmailRequest(String email, String text, String subject) {
        if (mqttClient == null || !mqttClient.isConnected()) {
            log.warn("MQTT client not available");
            return;
        }

        try {
            EmailRequestDto dto = new EmailRequestDto(email, text, subject);
            MqttMessage message = new MqttMessage();
            message.setPayload(objectMapper.writeValueAsBytes(dto));
            message.setQos(1);

            mqttClient.publish("email.requests.topic", message);
        } catch (JsonProcessingException e) {
            log.error("JSON serialization failed", e);
        } catch (MqttException e) {
            log.error("MQTT publish failed", e);
        }
    }
}