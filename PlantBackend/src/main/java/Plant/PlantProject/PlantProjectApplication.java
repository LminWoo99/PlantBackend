package Plant.PlantProject;

import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.kakao.KaKaoService;
import Plant.PlantProject.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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
	//spring.jpa.hibernate.ddl-auto=update 바꾸면서 주석처리
//	@Bean
//	CommandLineRunner run(MemberService memberService, KaKaoService kaKaoService) {
//		return args -> {
//			memberService.saveRole(new Role(null, "ROLE_USER"));
//			memberService.saveRole(new Role(null, "ROLE_MANAGER"));
//			memberService.saveRole(new Role(null, "ROLE_ADMIN"));
//			memberService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
//
//		};
//	}
}


