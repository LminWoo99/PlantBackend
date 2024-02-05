package Plant.PlantProject.controller;

import Plant.PlantProject.dto.EmailReq;
import Plant.PlantProject.service.RegisterMail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {// 이메일 인증
    private final RegisterMail registerMail;
    @PostMapping("/mailConfirm")
    public ResponseEntity<String> mailConfirm(@RequestBody EmailReq emailReq) throws Exception {
        String code = registerMail.sendSimpleMessage(emailReq.getEmail());
        return ResponseEntity.ok().body(code);
    }

}
