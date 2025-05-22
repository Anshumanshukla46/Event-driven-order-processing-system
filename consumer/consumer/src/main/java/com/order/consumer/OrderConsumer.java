package com.order.consumer;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(topics = "orders-topic", groupId = "order-consumer-group-v2")
    public void consume(String message){
        System.out.println("Received: " + message);
        if (message.contains("fail")) {
            throw new RuntimeException("Force failure for retry!");
        }
    }
}
