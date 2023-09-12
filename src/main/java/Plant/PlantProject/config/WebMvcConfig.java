package Plant.PlantProject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("" +
                        "/**")
            .allowedOrigins("http://localhost:3000")
                .allowedOrigins("http://54.180.44.32/")
                .allowedOrigins("http://54.180.44.32/3000")
            .allowedMethods("OPTIONS", "GET","PATCH", "POST", "PUT", "DELETE");
    }
}