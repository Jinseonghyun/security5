package jin.security5.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration // Configuration 에 등록  // 스프링 빈 설정 클래스로 지정
@EnableWebSecurity // Security 설정 파일로 쓰겠다. // 스프링 시큐리티 설정 빈으로 등록
public class SecurityConfig {

    @Autowired
    private DataSource dataSource; // application.properties 에 정의한 DB 인 데이터 소스가 여기 객체로 정보가 들어온다. // jdbc 인증 방식의 객체 알려주기만 하면 된다.

    // 기본 설정
    // - 인메모리 방식 인증
    // - JDBC 방식 인증

    /**
     * 인메모리 방식 인증
     * - 기본 사용자를 메모리에 등록
     * - user / 123456
     * - admin / 123456
     * @return
     */
/*    @Bean
    public UserDetailsService userDetailsService() { // 사용자의 정보를 불러온다.
        UserDetails user = User.builder()
                .username("user")     // 아이디
                .password(passwordEncoder().encode("123456"))   // 패스워드
                .roles("USER")        // 권환
                .build();

        UserDetails admin = User.builder()
                .username("admin")             // 아이디
                .password(passwordEncoder().encode("123456"))        // 패스워드
                .roles("USER", "ADMIN")        // 권환
                .build();

        return new InMemoryUserDetailsManager(user, admin); // UserDetailsService 라는 실제 사용할 빈을 객체로 등록
    }
*/

    // JDBC 인증 방식
    // 데이터 소스 (URL, ID, PW) - applicatiopn.properties
    // SQL 쿼리 등록
    // - 사용자 인증 쿼리
    // - 사용자 권한 쿼리
    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource); // 객체로 일단 선언

        // 사용자 인증 쿼리
        String sql1 = " SELECT user_id as username, user_pw as password, enables " //  시큐리티는 기본적으로 명칭을 username, password 로 지정하고 있다.
                    + " FROM users "
                    + " WHERE user_id = ? ";       // ? 에 아이디를 조건으로 해서 유저 아이디 컬럼에 우리가 입력한 아이디가 들어간다. -> 그걸 데이터로 가져온다.
                    ;                              // -> 그게 우리 실제 데이터베이스에 있는 아이디랑 일치하는지 확인 -> 그리고 enabled (활성화) 야브기 1로 되어 있는지 확인해서 -> 로그인 처리
        // 사용자 권한 쿼리
        String sql2 = " SELECT ueser_id as username, auth "     // 위에서 로그인 처리 되면 애가 갖고 있는 권한에 대해서 조회해서 가져오게 한다.
                    + " FROM user_auth "
                    + " WHERE user_id = ? ";
                    ;

        // 위에서 작성한 객체 쿼리들을 등록을 시켜주자
        userDetailsManager.setUsersByUsernameQuery(sql1);
        userDetailsManager.setAuthoritiesByUsernameQuery(sql2);
        return userDetailsManager;
    }

    /**
     * AuthenticationManager 빈 등록
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // 스프링 한테 알려주기 위해서
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
