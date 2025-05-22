package com.order.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class DeadLetterConsumer {

    @KafkaListener(topics = "orders-topic.DLT", groupId = "order-consumer-group-v2")
    public void listenDLT(@Payload String message){
        System.out.println("Received in DLT: "+message);
    }

}
