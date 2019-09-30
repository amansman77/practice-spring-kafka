package com.ho.practice.kafka.pubsub;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

import com.ho.practice.kafka.message.Greeting;

@Component
@EnableBinding(Sink.class)
public class StreamReceiver {
	
    @StreamListener(Sink.INPUT)
    public void handle(Greeting greeting) {
    	if (Long.valueOf(greeting.getId()) % 2 == 0) {
    		System.out.println("Pubsub stream receive : " + greeting.getId() + " : 짝수입니다");
    	} else {
    		System.out.println("Pubsub stream receive : " + greeting.getId() + " : 홀수입니다");
    	}
    }
	
}
