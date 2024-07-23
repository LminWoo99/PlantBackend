package com.example.plantcouponservice.common.config;

import com.example.plantcouponservice.vo.request.CouponRequestDto;
import com.example.plantcouponservice.vo.request.PaymentRequestDto;
import com.google.common.collect.ImmutableMap;
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

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${kafka.url}")
    private String kafkaServerUrl;

    // Kafka Producer 구성을 위한 설정값들을 포함한 맵을 반환하는 메서드
    @Bean
    public Map<String, Object> producerConfigurations() {
        return ImmutableMap.<String, Object>builder()
                .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl)
                .put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                //멱등성 프로듀서 명시적 설정
                .put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true)
                .build();

    }

    @Bean
    public ProducerFactory<String, CouponRequestDto> couponCreatedProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());

    }

    @Bean
    public KafkaTemplate<String, CouponRequestDto> kafkaTemplate() {
        return new KafkaTemplate<>(couponCreatedProducerFactory());
    }
    @Bean
    public ProducerFactory<String, PaymentRequestDto> paymentProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());

    }

    @Bean
    public KafkaTemplate<String, PaymentRequestDto> paymentUseKafkaTemplate() {
        return new KafkaTemplate<>(paymentProducerFactory());
    }
}
