package com.example.plantchatservice.chat;

import com.example.plantchatservice.client.PlantServiceClient;
import com.example.plantchatservice.common.util.TokenHandler;
import com.example.plantchatservice.repository.chat.ChatRepository;
import com.example.plantchatservice.repository.chat.ChatRoomRepository;
import com.example.plantchatservice.repository.mongo.MongoChatRepository;
import com.example.plantchatservice.service.chat.ChatRoomService;
import com.example.plantchatservice.service.chat.MessageSender;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
@MockBean(MongoChatRepository.class)
@MockBean(JpaMetamodelMappingContext.class)
@MockBean(ChatRoomRepository.class)
@MockBean(MessageSender.class)
@MockBean(AggregationSender.class)
@MockBean(MongoTemplate.class)
@MockBean(PlantServiceClient.class)
@MockBean(CircuitBreakerFactory.class)
@MockBean(ChatRoomService.class)
@MockBean(TokenHandler.class)
@MockBean(ChatRepository.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MvcTestBasic {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;



    @BeforeEach
    void setUp(WebApplicationContext context){
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected String createStringJson(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }
}
