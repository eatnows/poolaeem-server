package com.poolaeem.poolaeem.security.config;

import com.poolaeem.poolaeem.security.jwt.filter.FilterSkipMatcher;
import com.poolaeem.poolaeem.security.jwt.filter.JwtAuthenticationFilter;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAccessDeniedHandler;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationSuccessHandler;
import com.poolaeem.poolaeem.security.jwt.provider.JwtAuthenticationProvider;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginFailureHandler;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessHandler;
import com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.poolaeem.poolaeem.security.oauth2.service.CustomOAuth2UserService;
import com.poolaeem.poolaeem.security.oauth2.service.CustomOidcUserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(jwtAuthenticationProvider);
        authenticationManager = builder.build();
        http.authenticationManager(authenticationManager);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable);
        http
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(config -> config.configurationSource(corsConfigurationSource()));
        http
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(endpoint -> endpoint
                                        .userService(customOAuth2UserService)
                                        .oidcUserService(customOidcUserService)
                                )
                                .authorizedClientService(oAuth2AuthorizedClientService)
                                .authorizationEndpoint(endpoint -> endpoint
                                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                                                .baseUri("/api/signin/oauth2")
//                                                .authorizationRedirectStrategy(new CustomRedirectStrategy())
                                )
                                .successHandler(loginSuccessHandler)
                                .failureHandler(loginFailureHandler)
                )
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/api/user").hasAnyRole("SCOPE_profile", "SCOPE_email")
                        .requestMatchers("/api/oidc").hasAnyRole("SCOPE_openid")
                        .requestMatchers("/api/signup/terms", "/api/access-token/refresh", "/poolaeem-api/docs/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(config -> {
                    config.accessDeniedHandler(jwtAccessDeniedHandler);
                    config.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                });

        http
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }

    private Filter jwtFilter() {
        FilterSkipMatcher matcher = new FilterSkipMatcher(List.of(
                "/api/signin/oauth2",
                "/api/signup/terms",
                "/api/access-token/refresh"
        ), "/api/**");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher, jwtAuthenticationSuccessHandler, jwtAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.PATCH);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedHeader(CorsConfiguration.ALL);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
