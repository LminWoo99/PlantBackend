package Plant.PlantProject.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoggingController {
    @Value("${logging-module.version}")
    private String version;

    @GetMapping("")
    public String version() {
        return String.format("Project Version : %s", version);
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "healthy";
    }
}
