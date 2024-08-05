package jin.security5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration // Configuration 에 등록  // 스프링 빈 설정 클래스로 지정
@EnableWebSecurity // Security 설정 파일로 쓰겠다. // 스프링 시큐리티 설정 빈으로 등록
public class SecurityConfig {

    // 기본 설정
    // - 인메모리 방식 인증

    /**
     * 인메모리 방식 인증
     * - 기본 사용자를 메모리에 등록
     * - user / 123456
     * - admin / 123456
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() { // 사용자의 정보를 불러온다.
        UserDetails user = User.builder()
                .username("user")     // 아이디
                .password("123456")   // 패스워드
                .roles("USER")        // 권환
                .build();

        UserDetails admin = User.builder()
                .username("admin")             // 아이디
                .password("123456")            // 패스워드
                .roles("USER", "ADMIN")        // 권환
                .build();

        return new InMemoryUserDetailsManager(user, admin); // UserDetailsService 라는 실제 사용할 빈을 객체로 등록
    }

    /**
     * AuthenticationManager 빈 등록
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 암호화 방식 빈 등록
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 암호화 방식
    }

}
