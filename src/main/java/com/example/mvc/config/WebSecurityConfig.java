package com.example.mvc.config;

import com.example.mvc.configuration.DataConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequiredArgsConstructor
public class WebSecurityConfig  {
//https://www.baeldung.com/spring-security-jdbc-authentication
//https://spring.io/guides/gs/securing-web/

    private final DataSource dataSource;


    //.antMatchers("/", "/home").permitAll() , 다중할때
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers("/","/starter-template.css").permitAll() 만약 style.css 가 깨지면 이렇게 넣으면 되는데 그러면 추가 할때마다 넣어줘야하니까
                //.antMatchers("/","/css/**").permitAll() /css 아래에 모든 파일을 읽어준다.
                    .antMatchers("/","/css/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
        return http.build();
    }
    // JPA 에 JOIN이 들어가면 설정이 많이 복잡해집니다.
    //@OneToOne : user - user_detail
    //@OneToMany : user - board
    //@ManyToOne : board - user 여러개 게시판은 하나의 사용자 이죠
    //@ManyToMany : user - role 하나의 사용자는 여러개의 권한을 가질수있고 반대도 가능합니다.
    // 이걸 설정하기 위해서는 role, user 를 연결해줘야하는 mapping 테이블 이렇게 3개를 이용합니다.
    //role, user, user_role 이용하는 예제
    // 뒤에 여백을 넣어줘야함 , ? 에 자동으로 username이 들어갑니다.

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select username, password, enabled "
                        + "from users "
                        + "where username = ?")
                .authoritiesByUsernameQuery("select username, name "
                        + "from user_role ur inner join user u on ur.user_id = u.id "
                        + "inner join role r on ur.role_id = r.id "
                        + "where email = ?");
    }

//    Authentication 로그인
//    Authorizaion 권한

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //만약 Error creating bean with name 'webSecurityConfig':
    //The requested bean is currently in creation: Is there an unresolvable circular reference?
    // 이 에러가 나온다면 PasswordEncoder 부분에 static 을 넣어준다.
}