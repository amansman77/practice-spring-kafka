package com.ho.practice.kafka.reqrep;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.ho.practice.kafka.message.Greeting;

/**
 * One way messaging에 대한 설정
 * @author hhsung
 *
 */
@Configuration
public class KafkaReqrepConsumerConfig {
 
	@Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;
	
	@Bean
    public ConsumerFactory<String, Greeting> requestConsumerFactory() {
    	 Map<String, Object> props = new HashMap<>();
         props.put(
           ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
           bootstrapAddress);
         props.put(
           ConsumerConfig.GROUP_ID_CONFIG, 
           "request-consumer");
        return new DefaultKafkaConsumerFactory<>(
          props,
          new StringDeserializer(), 
          new JsonDeserializer<>(Greeting.class));
    }
    
    @Bean
    public ProducerFactory<String, Greeting> replyProducerFactory() {
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
    public KafkaTemplate<String, Greeting> replyTemplate() {
      return new KafkaTemplate<>(replyProducerFactory());
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Greeting> requestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Greeting> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestConsumerFactory());
        factory.setReplyTemplate(replyTemplate());
        return factory;
    }
    
}