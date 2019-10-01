package com.ho.practice.kafka.pubsub;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ho.practice.kafka.message.Greeting;

@Component
public class Producer {
	
	@Autowired
	private KafkaTemplate<String, Greeting> kafkaTemplatePubsub;
	
	@Value(value = "${kafka.topic.pubsub}")
	public String kafkaTopic;
	
	/**
	 * 주기적으로 메세지 전송
	 */
	@Scheduled(fixedDelayString = "${schedule.delay}")
	public void sendMessage() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		String strDate = sdf.format(now);
		kafkaTemplatePubsub.send(kafkaTopic, new Greeting(strDate, "Andy"));
	}
	
}
