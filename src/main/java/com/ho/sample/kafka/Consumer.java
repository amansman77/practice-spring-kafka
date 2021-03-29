package com.ho.sample.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	
	@KafkaListener(topics = "model-result", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
    	System.out.println("[String] msg : " + message);
    }
	
}
