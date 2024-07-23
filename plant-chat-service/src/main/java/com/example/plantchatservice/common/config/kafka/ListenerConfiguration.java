package com.example.plantchatservice.common.config.kafka;

import com.example.plantchatservice.dto.chat.Message;
import com.example.plantchatservice.dto.notification.NotificationEventDto;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.util.List;
import java.util.Map;

@EnableKafka
@Configuration
public class ListenerConfiguration {
    //KafkaListener 컨테이너 팩토리를 생성하는 Bean 메서드
    @Value("${kafka.url}")
    private String kafkaServerUrl;
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
    // Kafka ConsumerFactory를 생성하는 Bean 메서드
    @Bean
    public ConsumerFactory<String, Message> consumerFactory() {
        JsonDeserializer<Message> deserializer = new JsonDeserializer<>();
        // 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
        deserializer.addTrustedPackages("*");
        // Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl)
                        .put(ConsumerConfig.GROUP_ID_CONFIG, "chat")
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaDeletePostContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(deletePostConsumer());
        return factory;
    }
    // Kafka deletePostConsumerFactory를 생성하는 Bean 메서드
    @Bean
    public ConsumerFactory<String, String> deletePostConsumer() {

        // Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl)
                        .put(ConsumerConfig.GROUP_ID_CONFIG, "deletePost")
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations);
    }
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, NotificationEventDto> kafkaNotificationContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaNotificationConsumer());
        return factory;
    }
    @Bean
    public ConsumerFactory<String, NotificationEventDto> kafkaNotificationConsumer() {
        JsonDeserializer<NotificationEventDto> deserializer = new JsonDeserializer<>(NotificationEventDto.class, false);
        // 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
        deserializer.addTrustedPackages("*");
        // Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.GROUP_ID_CONFIG, "notification")
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl)
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

}
