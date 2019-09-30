package com.ho.practice.kafka.pubsub;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ho.practice.kafka.message.Greeting;

@Component
public class Producer {
	
	@Autowired
	private KafkaTemplate<String, Greeting> kafkaTemplatePubsub;
	
	/**
	 * 1초에 한번씩 메세지 전송
	 */
	@Scheduled(fixedDelay = 1000)
	public void cronJobSch() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		String strDate = sdf.format(now);
//		System.out.println("Java cron job expression:: " + strDate);
		kafkaTemplatePubsub.send(KafkaProducerConfig.KAFKA_TOPICS, new Greeting(strDate, "Andy"));
	}
	
}
