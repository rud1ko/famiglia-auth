package com.famiglia.famiglia_auth.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "taskManager", groupId = "my_consumer")
    public void listen(String message) {
        System.out.println("Recieved message = " + message);
    }
}
