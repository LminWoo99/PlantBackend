package Plant.PlantProject.messagequeue;

import Plant.PlantProject.dto.TradeBoardDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    /**
     * 중고 거래 게시글 삭제시 카프카를 통해 plant-chat-service 마이크로서비스에 전달
     * @param : String topic, TradeBoardDto tradeBoardDto
     */
    public TradeBoardDto send(String topic, TradeBoardDto tradeBoardDto) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try{

            jsonInString = mapper.writeValueAsString(tradeBoardDto);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer send data from the Plant microservice " + tradeBoardDto);
        return tradeBoardDto;
    }
}
