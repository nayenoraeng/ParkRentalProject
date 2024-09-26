package com.project.parkrental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class webSecurityConfig {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/files/**").permitAll()
                        .anyRequest().anonymous()  // 어떠한 요청이라도 인증 필요 없음 ( anonymous() )  //authenticated()
                );
        http.formLogin((formLogin) -> formLogin
                .loginPage("/login")
                .permitAll());

        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .permitAll());

        return http.build();
    }
}
