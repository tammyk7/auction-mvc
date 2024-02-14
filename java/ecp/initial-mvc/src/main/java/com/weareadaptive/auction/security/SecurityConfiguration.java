package com.weareadaptive.auction.security;

import com.weareadaptive.auction.exception.AccessDeniedExceptionHandling;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

// todo: uncomment to enable security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfiguration
{

    private final AuthenticationConfiguration authenticationConfiguration;

    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(new AntPathRequestMatcher("/api/**"));

    AuthenticationProvider provider;

    public SecurityConfiguration(final AuthenticationConfiguration authenticationConfiguration)
    {
        super();
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        return new AuthenticationProvider();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return (web) -> web.debug(true).ignoring().requestMatchers("/token/**");
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception
    {
        //@formatter:off
        http
                .cors()
                .configurationSource(corsConfigurationSource())
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationExceptionHandling())
                .accessDeniedHandler(new AccessDeniedExceptionHandling())
            .and()
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(PROTECTED_URLS).authenticated()
                )
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
        //@formatter:on

        return http.build();
    }

    @Bean
    AuthenticationFilter authenticationFilter() throws Exception
    {
        final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    private AuthenticationSuccessHandler successHandler()
    {
        return (request, response, authentication) ->
        {
            System.out.println(authentication.getAuthorities());
            response.setStatus(HttpStatus.OK.value());
        };
    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint()
    {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}
