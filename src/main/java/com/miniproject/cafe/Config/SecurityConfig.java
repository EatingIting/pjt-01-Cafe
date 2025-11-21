package com.miniproject.cafe.Config;

import com.miniproject.cafe.Filter.SessionSetupFilter;
import com.miniproject.cafe.Handler.FormLoginFailureHandler;
import com.miniproject.cafe.Handler.FormLoginSuccessHandler;
import com.miniproject.cafe.Handler.OAuth2FailureHandler;
import com.miniproject.cafe.Handler.OAuthLoginSuccessHandler;
import com.miniproject.cafe.Mapper.AdminMapper;
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
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final FormLoginFailureHandler formLoginFailureHandler;

    private final MemberMapper memberMapper;
    private final AdminMapper adminMapper; // [추가] 필터에 넘겨주기 위해 주입

    private static final String REMEMBER_ME_KEY = "secure-key";

    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(
                REMEMBER_ME_KEY,
                customUserDetailsService
        );
        services.setAlwaysRemember(false);
        services.setTokenValiditySeconds(60 * 60 * 24 * 14);
        services.setCookieName("remember-me");
        services.setParameter("remember-me");
        return services;
    }

    @Bean
    public RememberMeServices oauthRememberMeServices() {
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
        return new OAuthLoginSuccessHandler(memberMapper, oauthRememberMeServices());
    }

    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler(memberMapper, rememberMeServices());
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
                        .requestMatchers("/home/**").authenticated()
                        .anyRequest().permitAll()
                )

                .formLogin(f -> f
                        .loginPage("/home/login")
                        .loginProcessingUrl("/login")
                        .successHandler(formLoginSuccessHandler())
                        .failureHandler(formLoginFailureHandler)
                        .permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/home/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuthLoginSuccessHandler())
                        .failureHandler(oAuth2FailureHandler)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                )

                .rememberMe(r -> r
                        .rememberMeServices(rememberMeServices())
                )

                // [수정] SessionSetupFilter에 adminMapper도 함께 전달
                .addFilterAfter(new SessionSetupFilter(memberMapper, adminMapper), SecurityContextHolderFilter.class);

        return http.build();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}