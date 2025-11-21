package com.miniproject.cafe.Config;

import com.miniproject.cafe.Handler.FormLoginFailureHandler;
import com.miniproject.cafe.Handler.FormLoginSuccessHandler;
import com.miniproject.cafe.Handler.OAuth2FailureHandler;
import com.miniproject.cafe.Handler.OAuthLoginSuccessHandler;
import com.miniproject.cafe.Mapper.MemberMapper;
import com.miniproject.cafe.Service.CustomOAuth2UserService;
import com.miniproject.cafe.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final FormLoginFailureHandler formLoginFailureHandler;
    private final FormLoginSuccessHandler formLoginSuccessHandler;

    private final MemberMapper memberMapper;

    private static final String REMEMBER_ME_KEY = "secure-key";

    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(
                REMEMBER_ME_KEY,
                customUserDetailsService
        );
        services.setAlwaysRemember(true);
        services.setTokenValiditySeconds(60 * 60 * 24 * 14);
        services.setCookieName("remember-me");
        return services;
    }

    @Bean
    public OAuthLoginSuccessHandler oAuthLoginSuccessHandler() {
        return new OAuthLoginSuccessHandler(memberMapper, rememberMeServices());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/member/**", "/oauth2/**").permitAll()
                        .requestMatchers("/menu/**").permitAll()
                        .requestMatchers("/home/saveRegion", "/home/getRegion").permitAll()
                        .requestMatchers("/home/login").permitAll()
                        // 로그인 필요 페이지
                        .requestMatchers("/home/**").authenticated()
                        .anyRequest().permitAll()
                )

                // 일반 폼 로그인
                .formLogin(f -> f
                        .loginPage("/home/login")
                        .loginProcessingUrl("/login")
                        .successHandler(formLoginSuccessHandler)
                        .failureHandler(formLoginFailureHandler)
                        .permitAll()
                )

                // OAuth2 로그인
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/home/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuthLoginSuccessHandler())
                        .failureHandler(oAuth2FailureHandler)
                )

                // 로그아웃
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )

                // remember-me (폼 + OAuth2 모두에 적용됨)
                .rememberMe(r -> r
                        .rememberMeServices(rememberMeServices()) // 위에서 만든 Bean 사용
                );

        return http.build();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
