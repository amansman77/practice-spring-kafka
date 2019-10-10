package com.ho.practice.kafka.pubsub;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ho.practice.kafka.message.Greeting;

@Component
public class Consumer {
	
	@KafkaListener(topics = "${kafka.topic.pubsub}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
    	System.out.println("[String] Pubsub receive : " + message);
    }
	
	@KafkaListener(topics = "${kafka.topic.reqrep.request}", containerFactory = "jsonKafkaListenerContainerFactory")
	public void listen2(Greeting message) {
    	System.out.println("[Json] Pubsub receive : " + message);
    }
	
}
