package com.example.plantpayservice.service;


import com.example.plantpayservice.domain.entity.CouponStatus;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, topics = {"payment", "coupon-success", "payment-failed", "coupon-rollback"})
class PaymentSagaTest {

    @Autowired
    private KafkaTemplate<String, PaymentRequestDto> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    private DefaultKafkaConsumerFactory<String, PaymentRequestDto> consumerFactory;

    @BeforeEach
    public void setup() {
        // 컨슈머 설정을 위한 구성 생성
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "false", embeddedKafka);
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", ErrorHandlingDeserializer.class.getName());
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    @Test
    public void testPaymentOrchestrator() {
        // Arrange: 테스트할 PaymentRequestDto 객체 생성
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setMemberNo(1);
        paymentRequestDto.setPayMoney(3000);
        paymentRequestDto.setCouponStatus(CouponStatus.쿠폰사용);

        // Act: Kafka 토픽에 메시지를 전송
        kafkaTemplate.send("payment", paymentRequestDto);

        // Assert: 토픽에서 메시지를 읽어와 확인
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "false", embeddedKafka);
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", JsonDeserializer.class.getName());
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        DefaultKafkaConsumerFactory<String, PaymentRequestDto> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        ConsumerRecord<String, PaymentRequestDto> record = KafkaTestUtils.getSingleRecord(consumerFactory.createConsumer(), "payment");

        assertThat(record.value()).isEqualTo(paymentRequestDto);
    }
}