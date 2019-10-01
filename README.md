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

## Publish-Subscribe vs Streams
- Producer-Consumer
    - Consumer는 Polling방식으로 메시지 조회
- Streams
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