package com.threatstream.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public EmbeddedKafkaKraftBroker embeddedKafkaBroker() {
        return new EmbeddedKafkaKraftBroker(1, 1, "hello-topic");
    }

    @Bean
    public NewTopic helloTopic() {
        return new NewTopic("hello-topic", 1, (short) 1);
    }
}
