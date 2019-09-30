package com.ho.practice.kafka.pubsub;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ho.practice.kafka.message.Greeting;

@Component
public class Consumer {
	
	@KafkaListener(topics = KafkaProducerConfig.KAFKA_TOPICS, groupId = "foo")
    public void listen(Greeting message) {
    	System.out.println("Pubsub receive : " + message);
    }
	
}
