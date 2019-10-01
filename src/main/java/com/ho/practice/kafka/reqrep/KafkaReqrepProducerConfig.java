package com.ho.practice.kafka.reqrep;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.ho.practice.kafka.message.Greeting;

/**
 * Request에 대한 producer설정 및 Reply에 대한 consumer설정
 * @author hhsung
 *
 */
@Configuration
public class KafkaReqrepProducerConfig {
 
	@Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;
	
	@Value(value = "${kafka.topic.reqrep.reply}")
	public String topicReqrepReply;
	
    @Bean
    public ProducerFactory<String, Greeting> requestProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
          bootstrapAddress);
        configProps.put(
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
          StringSerializer.class);
        configProps.put(
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
          JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
 
    @Bean
    public ConsumerFactory<String, Greeting> replyConsumerFactory() {
    	 Map<String, Object> props = new HashMap<>();
         props.put(
           ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
           bootstrapAddress);
         props.put(
           ConsumerConfig.GROUP_ID_CONFIG, 
           "reply-consumer");
        return new DefaultKafkaConsumerFactory<>(
          props,
          new StringDeserializer(), 
          new JsonDeserializer<>(Greeting.class));
    }
    
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Greeting>> reqrepReplyKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Greeting> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(replyConsumerFactory());
		return factory;
    }
    
    @Bean
    public KafkaMessageListenerContainer<String, Greeting> replyListenerContainer(
    		@Qualifier("replyConsumerFactory") ConsumerFactory<String, Greeting> cf) {
    	  ContainerProperties containerProperties = new ContainerProperties(topicReqrepReply);
    	  return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }
    
    @Bean
    public ReplyingKafkaTemplate<String, Greeting, Greeting> replyingTemplate(
    		@Qualifier("requestProducerFactory") ProducerFactory<String, Greeting> pf,
    		@Qualifier("replyListenerContainer") KafkaMessageListenerContainer<String, Greeting> replyListenerContainer) {
    	return new ReplyingKafkaTemplate<String, Greeting, Greeting>(pf, replyListenerContainer);
    }
    
}