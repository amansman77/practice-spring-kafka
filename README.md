# Spring을 활용한 Kafka연계

## 목표
Spring을 기반으로 kafka를 연계한다.

## 개발 프레임워크
 - IDE : STS-4.2.2.RELEASE
 - Java : openjdk 12.0.1
 - Spring Boot : 2.1.8
 - Gradle : 5.6.2

## Kafka 라이브러리
- Kafka
    - Apache 그룹에서 제공
- Spring-kafka
    - Spring에서 제공
- Spring-cloud-starter-stream-kafka
    - Spring Cloud Project에서 제공
    - Kafka의 Stream을 활용하도록 지원
    - Spring-kafka를 포함하고 있음

## Producer-Consumer vs Streams
- Producer-Consumer
    - Consumer는 Polling방식으로 메시지 조회
- Streams
    - [Kafka Streams is a client library for processing and analyzing data stored in Kafka](https://kafka.apache.org/23/documentation/streams/core-concepts)
    - 내부적으로 Producer-Consumer pattern 활용
    - Data pipeline으로 활용 가능
    - Apache Storm이나 Apache Samza를 대용할 수 있음
    
## Replying-Response Pattern
Synchronous process
- [Björn Beskow의 블로그](https://callistaenterprise.se/blogg/teknik/2018/10/26/synchronous-request-reply-over-kafka)와 [Synchronous Kafka: Using Spring Request-Reply - DZone](https://dzone.com/articles/synchronous-kafka-using-spring-request-reply-1)의 글 참고
- ReplyingKafkaTemplate
    - [Spring Kafka에서 2.1.3버전부터 Request-Reply pattern을 지원 (2.2.7버전 사용중)](https://docs.spring.io/spring-kafka/docs/2.2.7.RELEASE/reference/html/#replying-template)
    
## 샘플
- Publish-Subscribe Pattern
    - `com.ho.practice.kafka.pubsub`에 구현
- Request-Reply Pattern
    - `com.ho.practice.kafka.reqrep`에 구현
    

## 이슈

### Kafka의 메세지를 Object로 맵핑하여 받는경우 `Non-standard token 'NaN'` 발생

`ObjectMapperTest 클래스`에 테스트코드 작성

메세지의 Value에 null이 있는 경우 다음과 같은 오류 발생하면서
**kafka의 메세지를 소비하지 않고 동일한 메세지에 대해서 지속적으로 오류상황 유지**
```bash
org.apache.kafka.common.errors.SerializationException: Error deserializing key/value for partition model-result-0 at offset 36. If needed, please seek past the record to continue consumption.
Caused by: org.apache.kafka.common.errors.SerializationException: Can't deserialize data [[123, 34, 115, 101, 110, 115, 111, 114, 73, 100, 34, 58, 32, 34, 118, 105, 98, 101, 95, 110, 111, 105, 115, 101, 34, 44, 32, 34, 109, 111, 100, 101, 108, 73, 100, 34, 58, 32, 34, 97, 118, 101, 114, 97, 103, 101, 95, 109, 111, 100, 101, 108, 34, 44, 32, 34, 115, 101, 110, 115, 105, 110, 103, 68, 97, 116, 101, 34, 58, 32, 34, 50, 48, 49, 57, 45, 49, 49, 45, 49, 50, 32, 49, 51, 58, 51, 55, 58, 50, 56, 34, 44, 32, 34, 118, 97, 108, 117, 101, 34, 58, 32, 78, 97, 78, 125]] from topic [model-result]
Caused by: com.fasterxml.jackson.core.JsonParseException: Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow
 at [Source: (byte[])"{"a": "1", "b": "2", "c": "2019-11-12 13:37:28", "value": NaN}"; line: 1, column: 106]
	at com.fasterxml.jackson.core.JsonParser._constructError(JsonParser.java:1804) ~[jackson-core-2.9.9.jar:2.9.9]
	at com.fasterxml.jackson.core.base.ParserMinimalBase._reportError(ParserMinimalBase.java:693) ~[jackson-core-2.9.9.jar:2.9.9]
	at com.fasterxml.jackson.core.json.UTF8StreamJsonParser._handleUnexpectedValue(UTF8StreamJsonParser.java:2608) ~[jackson-core-2.9.9.jar:2.9.9]
```

**해결**

Kafka 컨슈머에 class type만 지정하던 방식에서
```bash
new JsonDeserializer<>(ModelResultDto.class)
```

`ObjectMapper`를 커스터마이스하여 `ALLOW_NON_NUMERIC_NUMBERS` 옵션을 `true` 로 설정함
```bash
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

new JsonDeserializer<>(ModelResultDto.class, objectMapper)
```