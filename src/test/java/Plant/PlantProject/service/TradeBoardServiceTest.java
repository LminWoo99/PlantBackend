package Plant.PlantProject.service;

import Plant.PlantProject.controller.TradeBoardController;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.repository.TradeBoardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName    : Plant/PlantProject/service
 * fileName       : TradeBoardServiceTest
 * author         : 이민우
 * date           : 2023-02-22
 * description    : 거래 게시글 저장 테스트 성공
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-02-22        이민우       최초 생성
 * 2022-02-28        이민우       페이징 테스트 성공(100개 임시 데이터 생성 후 테스트 페이지 갯수, 페이지사이즈 체크 확인)
 * 2022-02-28        이민우       게시글 상세보기 테스트 성공
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class TradeBoardServiceTest {
    @Autowired
    TradeBoardService tradeBoardService;
    @Autowired
    TradeBoardController tradeBoardController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentation) {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(tradeBoardController)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }
    @Test
    @DisplayName("게시글 작성 테스트")
    public void writeTest() throws Exception {
        // given
        TradeBoardDto tradeBoardDto = new TradeBoardDto();
        tradeBoardDto.setTitle("글 작성 테스트");
        tradeBoardDto.setContent("글 작성 테스트 내용");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"글 작성 테스트\", \"content\": \"글 작성 테스트 내용\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/write",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\": \"글 작성 테스트\", \"content\": \"글 작성 테스트 내용\"}"));

    }

    @Test
    @DisplayName("글 리스트 조회 테스트")
    public void boardListTest() throws Exception {
        //given
        for(int i=0; i<100; i++){
            tradeBoardService.saveTradePost(new TradeBoardDto("title"+i, "content"+i));
        }
        //when
        Pageable paging = PageRequest.of(0,10,Sort.Direction.ASC,"tTitle");
        Page<TradeBoardDto> result= tradeBoardService.pageList(paging);
        //then
        System.out.println(result.getSize());
        System.out.println(result.getTotalPages());
    }

    @Test
    @DisplayName("글 상세보기 테스트")
    public void boardContentTest() throws Exception {
        // given
        TradeBoardDto tradeBoardDto = new TradeBoardDto();
        tradeBoardDto.setId(1L);
        tradeBoardDto.setTitle("글1");
        tradeBoardDto.setContent("글1 내용");
        tradeBoardService.saveTradePost(tradeBoardDto);

        Assertions.assertThat(tradeBoardService.findById(1L).getContent()).isEqualTo(tradeBoardDto.getContent());

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/list/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"title\": \"글1\", \"content\": \"글1 내용\"}"));

    }

    @Test
    @DisplayName("글 수정 테스트")
    public void updateTest() throws Exception {
        // given
        TradeBoardDto tradeBoardDto = new TradeBoardDto();
        tradeBoardDto.setId(1L);
        tradeBoardDto.setTitle("기존 제목");
        tradeBoardDto.setContent("기존 내용");
        tradeBoardService.saveTradePost(tradeBoardDto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/api/list/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"title\": \"수정된 제목\", \"content\": \"수정된 내용\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"title\": \"수정된 제목\", \"content\": \"수정된 내용\"}"));
    }

    @Test
    @DisplayName("글 삭제 테스트")
    public void deleteTest() throws Exception {
        // given
        TradeBoardDto tradeBoardDto = new TradeBoardDto();
        tradeBoardDto.setId(1L);
        tradeBoardService.saveTradePost(tradeBoardDto);

        Assertions.assertThat(tradeBoardService.findById(1L).getId()).isEqualTo(tradeBoardDto.getId());

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/list/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("api/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isNoContent());

    }


}