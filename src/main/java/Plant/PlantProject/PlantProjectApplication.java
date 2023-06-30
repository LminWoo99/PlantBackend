package Plant.PlantProject;

import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@EnableJpaAuditing
public class PlantProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantProjectApplication.class, args);
	}

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
		return new HiddenHttpMethodFilter();
	}
	@Bean
	CommandLineRunner run(MemberService memberService) {
		return args -> {
			memberService.saveRole(new Role(null, "ROLE_USER"));
			memberService.saveRole(new Role(null, "ROLE_MANAGER"));
			memberService.saveRole(new Role(null, "ROLE_ADMIN"));
			memberService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
		};
	}
}


