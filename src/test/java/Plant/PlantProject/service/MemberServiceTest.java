//package Plant.PlantProject.service;
//
//import Plant.PlantProject.Entity.Member;
//import Plant.PlantProject.config.SecurityConfig;
//import Plant.PlantProject.dto.MemberDto;
//import Plant.PlantProject.repository.MemberRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///*
// 작성자 : 이민우
// 작성 일자: 02.19
// 내용 : member, tradeboard 연관관계 테스트
//*/
//@SpringBootTest
//@Transactional
//@AutoConfigureMockMvc
//
//class MemberServiceTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//    @MockBean
//    MemberService memberService;
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    private  AuthenticationManager authenticationManager;
//
//
//    @Test
//    public void 멤버서비스_테스트() throws Exception{
//        //given
//        MemberDto memberDto = new MemberDto("mw310", "이민우");
//        Member member = memberDto.toEntity();
//        memberService.joinUser(member);
//        System.out.println("memberDto = " + memberService.findByUserName("이민우"));
//        System.out.println("memberDto = " + memberService.findByUserId("mw310"));
//        Assertions.assertThat((memberService.findByUserName(memberDto.getUsername())).equals(memberDto));
//        Assertions.assertThat((memberService.findByUserId(memberDto.getUserId())).equals(memberDto));
//        Assertions.assertThat(memberService.findAll());
//
//    }
//    @Test
//    public void 스프링_시큐리티_회원가입_테스트 () throws Exception{
//        //given
//        //회원등록
//        MemberDto memberDto = new MemberDto();
//        memberDto.setUsername("testuser");
//        memberDto.setPassword("testpass");
//
//        //when
//        // 회원가입 API 호출 및 응답 검증
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(memberDto)))
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.header().exists("Location"));
//        //then
//        }
//    @Test
//    public void 로그인_테스트() throws Exception {
//
//        MemberDto memberDto = new MemberDto();
//        memberDto.setUsername("testuser");
//        memberDto.setPassword("testpass");
//        String username = "testuser";
//        String password = "testpass";
//        Member member = memberDto.toEntity();
////        memberService.loadUserByUsername("testuser");
//        memberService.joinUser(member);
//
//        mockMvc.perform(post("/api/login")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(memberDto)))
//                        .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
////    @Test
////    public void 회원_조회() throws Exception {
////        // 회원 조회 API 호출 및 응답 검증
////        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
////                .andExpect(status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("testuser"));
////    }
//    // 객체를 JSON 문자열로 변환하는 메소드
//    private static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}