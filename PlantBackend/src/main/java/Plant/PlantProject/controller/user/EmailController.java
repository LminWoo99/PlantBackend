package Plant.PlantProject.controller.user;

import Plant.PlantProject.vo.response.EmailReq;
import Plant.PlantProject.service.user.RegisterMail;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {// 이메일 인증
    private final RegisterMail registerMail;

    @PostMapping("/mailConfirm")
    @Operation(summary = "이메일 인증", description = "회원가입시 본인 이메일 확인인증을 위해 사용하는 API")
    public ResponseEntity<String> mailConfirm(@RequestBody EmailReq emailReq) throws Exception {
        String code = registerMail.sendSimpleMessage(emailReq.getEmail());
        return ResponseEntity.ok().body(code);
    }

}
