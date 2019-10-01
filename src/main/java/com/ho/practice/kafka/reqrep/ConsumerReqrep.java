package com.ho.practice.kafka.reqrep;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.ho.practice.kafka.message.Greeting;

@Component
public class ConsumerReqrep {
	
	@KafkaListener(topics = "${kafka.topic.reqrep.request}", containerFactory = "requestKafkaListenerContainerFactory")
    @SendTo
    public Greeting listenRequest(Greeting request) throws InterruptedException {
    	request.setName(request.getName() + " Reply!!");
    	return request;
    }
	
}
