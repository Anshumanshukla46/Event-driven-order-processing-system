package com.serverless.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final String TOPIC_NAME = "orders-topic";

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    @PostMapping
    public String placeOrder(@RequestBody Order order){
        System.out.println("ORDER RECEIVED AS: "+order);

        kafkaTemplate.send(TOPIC_NAME,order);
        return "✅ Order sent to Kafka!";
    }

}
