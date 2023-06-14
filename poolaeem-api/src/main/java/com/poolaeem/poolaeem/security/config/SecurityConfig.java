package com.poolaeem.poolaeem.security.config;

import com.poolaeem.poolaeem.common.jwt.HeaderTokenExtractor;
import com.poolaeem.poolaeem.security.filter.FilterSkipMatcher;
import com.poolaeem.poolaeem.security.filter.JwtAuthenticationFilter;
import com.poolaeem.poolaeem.security.handler.JwtAccessDeniedHandler;
import com.poolaeem.poolaeem.security.handler.JwtAuthenticationEntryPoint;
import com.poolaeem.poolaeem.security.handler.JwtAuthenticationFailureHandler;
import com.poolaeem.poolaeem.security.handler.JwtAuthenticationSuccessHandler;
import com.poolaeem.poolaeem.security.provider.JwtAuthenticationProvider;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final HeaderTokenExtractor headerTokenExtractor;


    public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider, JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler, JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler, HeaderTokenExtractor headerTokenExtractor) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
        this.jwtAuthenticationFailureHandler = jwtAuthenticationFailureHandler;
        this.headerTokenExtractor = headerTokenExtractor;
    }

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
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/api/**").authenticated();
                })
                .exceptionHandling(config -> {
                    config.accessDeniedHandler(jwtAccessDeniedHandler);
                    config.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                });
        http
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private Filter jwtFilter() {
        FilterSkipMatcher matcher = new FilterSkipMatcher(List.of(
                "/api/**/login",
                "/api/**/signup"
        ), "/api/**");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(matcher, jwtAuthenticationSuccessHandler, jwtAuthenticationFailureHandler, headerTokenExtractor);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("https://poolaeem-client.vercel.app/");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
