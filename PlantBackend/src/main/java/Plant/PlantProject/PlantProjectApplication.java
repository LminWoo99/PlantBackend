package Plant.PlantProject;

import Plant.PlantProject.service.kakao.KaKaoService;
import Plant.PlantProject.service.user.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.management.relation.Role;

@SpringBootApplication
@EnableWebMvc
@EnableEurekaClient
public class PlantProjectApplication {
//	public static final String APPLICATION_LOCATIONS = "spring.config.location="
//			+ "classpath:application.yml,"
//			+ "classpath:real-application.yml";
	public static void main(String[] args) {

		new SpringApplicationBuilder(PlantProjectApplication.class).run(args);;
	}

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
		return new HiddenHttpMethodFilter();
	}

}


