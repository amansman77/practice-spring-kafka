package com.ho.practice.kafka.pubsub;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ho.practice.kafka.message.Greeting;

@Component
public class Consumer {
	
	@KafkaListener(topics = "${kafka.topic.pubsub}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(Greeting message) {
    	System.out.println("Pubsub receive : " + message);
    }
	
}
