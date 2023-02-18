package Plant.PlantProject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/api/hello")
    public String test() {
        return "연결 성공!";
    }
    @GetMapping("/api/minu")
    public String test1() {
        return "minu 연결 성공!";
    }
}
