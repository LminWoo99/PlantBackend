package Plant.PlantProject.service;

import Plant.PlantProject.repository.TradeBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class TradeBoardServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    TradeBoardRepository tradeBoardRepository;
    @Test
    public void savePost() throws Exception{
        //given

        //when

        //then

        }

}