package com.ho.practice.kafka.reqrep;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.ho.practice.kafka.message.Greeting;

@Component
public class ProducerReqrep {
	
	@Autowired
    private ReplyingKafkaTemplate<String, Greeting, Greeting> replyingTemplate;
	
	@Value(value = "${kafka.topic.reqrep.request}")
	public String topicReqrepRequest;
	
	@Value(value = "${kafka.topic.reqrep.reply}")
	public String topicReqrepReply;
	
	/**
	 * 주기적으로 메세지 전송 후 동기 응답처리
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Scheduled(fixedDelayString = "${schedule.delay}")
	public void syncRequestReply() throws InterruptedException, ExecutionException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		String strDate = sdf.format(now);
		
		
		// create producer record
  	  	ProducerRecord<String, Greeting> record 
  	  		= new ProducerRecord<String, Greeting>(topicReqrepRequest, new Greeting(strDate, "Andy"));
  	  	
  	  	// set reply topic in header
  	  	record.headers().add(
  			  new RecordHeader(KafkaHeaders.REPLY_TOPIC, topicReqrepReply.getBytes())
  		);
  	  	
  	  	// post in kafka topic
  	  	RequestReplyFuture<String, Greeting, Greeting> sendAndReceive = replyingTemplate.sendAndReceive(record);
  	  	// confirm if producer produced successfully
  	  	SendResult<String, Greeting> sendResult = sendAndReceive.getSendFuture().get();
  	  	// print all headers
//  	  	sendResult.getProducerRecord().headers().forEach(
//  			  header -> System.out.println(header.key() + ":" + header.value().toString())
//  	  	);
  	  	// get consumer record
  	  	ConsumerRecord<String, Greeting> consumerRecord = sendAndReceive.get();
  	  	
  	  	// return consumer value
  	  	System.out.println("Reqrep sync reply : " + consumerRecord.value());
	}
	
	/**
	 * 주기적으로 메세지 전송 후 비동기 응답처리
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Scheduled(fixedDelayString = "${schedule.delay}")
	public void asyncRequestReply() throws InterruptedException, ExecutionException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		String strDate = sdf.format(now);
		
		// create producer record
    	ProducerRecord<String, Greeting> record = 
    			new ProducerRecord<String, Greeting>(topicReqrepRequest, new Greeting(strDate, "Andy"));
    	// set reply topic in header
    	record.headers().add(
    			new RecordHeader(KafkaHeaders.REPLY_TOPIC, topicReqrepReply.getBytes())
    			);
    	// post in kafka topic
    	RequestReplyFuture<String, Greeting, Greeting> sendAndReceive = replyingTemplate.sendAndReceive(record);
    	sendAndReceive.addCallback(new ListenableFutureCallback<ConsumerRecord<String, Greeting>>() {
    		@Override
    		public void onSuccess(ConsumerRecord<String, Greeting> result) {
    			// get consumer record value
    			Greeting requestReply = result.value();
    			System.out.println("Reqrep async reply : " + requestReply);
    		}

			@Override
			public void onFailure(Throwable ex) {
				System.out.println("Fail reply: " + ex.getMessage());
			}
    	});
	}
	
}
