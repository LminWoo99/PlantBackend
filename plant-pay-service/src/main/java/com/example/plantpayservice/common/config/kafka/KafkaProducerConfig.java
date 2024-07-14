package com.example.plantpayservice.common.config.kafka;

import com.example.plantpayservice.vo.request.PaymentRequestDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.url}")
    private String kafkaServerUrl;

    @Bean
    public ProducerFactory<String, PaymentRequestDto> kafkaProducerFactory() {
        HashMap<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        //멱등성 프로듀서 명시적 설정
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        return new DefaultKafkaProducerFactory<>(config);

    }

    @Bean
    public KafkaTemplate<String, PaymentRequestDto> couponUseKafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
}
