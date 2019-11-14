package com.ho.practice.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ho.practice.kafka.message.Greeting;

public class ObjectMapperTest {

	@Test(expected=JsonParseException.class)
	public void NaN_입력시_JsonParseException_발생() throws JsonParseException, JsonMappingException, IOException {
		String jsonData= "{\"id\":\"mem-001\","
				+ "\"name\":NaN}";
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.readValue(jsonData, Greeting.class);
	}
	
	@Test
	public void NaN도_deserialize_하기() throws JsonParseException, JsonMappingException, IOException {
		String jsonData= "{\"id\":\"mem-001\","
				+ "\"name\":" + Double.NaN + "}";
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
		Greeting greeting = objectMapper.readValue(jsonData, Greeting.class);
		
		assertThat(greeting).isNotNull();
		assertThat(greeting.getName()).isNotNull();
		assertThat(greeting.getName()).isEqualTo("NaN");
	}
	
}
