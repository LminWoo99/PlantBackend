package Plant.PlantProject.config;

import Plant.PlantProject.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 인증 무시 -> static 디렉토리의 파일들은 항상 인증 무시 (통과)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    //Request 가 들어오는 경우 권한 설정 & 로그인 & 로그아웃 처리
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/manager/**").hasRole("MANAGER")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/member/memberLoginForm")
                .defaultSuccessUrl("/member/memberLoginResult")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/member/logoutResult")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/member/denied")
                .and()
                .oauth2Login()				// OAuth2기반의 로그인인 경우
                .loginPage("/loginForm")		// 인증이 필요한 URL에 접근하면 /loginForm으로 이동
                .defaultSuccessUrl("/")			// 로그인 성공하면 "/" 으로 이동
                .failureUrl("/loginForm")		// 로그인 실패 시 /loginForm으로 이동
                .userInfoEndpoint()			// 로그인 성공 후 사용자정보를 가져온다
                .userService(memberService);
    }

    // 모든 인증을 처리하기 위한 AuthenticationManager

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
