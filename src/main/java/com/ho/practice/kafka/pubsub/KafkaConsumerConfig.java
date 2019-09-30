package com.ho.practice.kafka.pubsub;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.ho.practice.kafka.message.Greeting;

/**
 * One way messaging에 대한 설정
 * @author hhsung
 *
 */
@Configuration
public class KafkaConsumerConfig {
 
	@Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;
	
    @Bean
    public ConsumerFactory<String, Greeting> greetingConsumerFactory() {
    	 Map<String, Object> props = new HashMap<>();
         props.put(
           ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
           bootstrapAddress);
         props.put(
           ConsumerConfig.GROUP_ID_CONFIG, 
           "foo");
        return new DefaultKafkaConsumerFactory<>(
          props,
          new StringDeserializer(), 
          new JsonDeserializer<>(Greeting.class));
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Greeting> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Greeting> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(greetingConsumerFactory());
        return factory;
    }
    
}