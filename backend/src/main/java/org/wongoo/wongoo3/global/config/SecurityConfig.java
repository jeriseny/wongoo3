package org.wongoo.wongoo3.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.wongoo.wongoo3.global.jwt.auth.JwtAuthenticationFilter;
import org.wongoo.wongoo3.global.jwt.token.JwtClaimsResolver;
import org.wongoo.wongoo3.global.jwt.token.JwtParser;
import org.wongoo.wongoo3.global.security.oauth2.CustomOAuth2UserService;
import org.wongoo.wongoo3.global.security.oauth2.OAuth2FailureHandler;
import org.wongoo.wongoo3.global.security.oauth2.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtParser parser;
    private final JwtClaimsResolver claimsResolver;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/user/signup", "/api/user/signup/social", "/api/auth/**", "/api/oauth2/**").permitAll()
                        .requestMatchers("/api/stats").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/board/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/post/**").permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(auth -> auth.baseUri("/api/oauth2/authorization"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )

                .addFilterBefore(new JwtAuthenticationFilter(parser, claimsResolver), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
