package com.weareadaptive.auction.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

// todo: uncomment to enable security
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

  private final AuthenticationConfiguration authenticationConfiguration;

  private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
      new AntPathRequestMatcher("/**")
  );

  public SecurityConfiguration(final AuthenticationConfiguration authenticationConfiguration) {
    super();
    this.authenticationConfiguration = authenticationConfiguration;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new AuthenticationProvider();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.debug(true).ignoring().requestMatchers("/token/**");
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .and()
        .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
        .csrf().disable()
        .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
        .formLogin().disable()
        .httpBasic().disable()
        .logout().disable()
        .authorizeHttpRequests();

    return http.build();
  }

  @Bean
  AuthenticationFilter authenticationFilter() throws Exception {
    final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
    filter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
    return filter;
  }

}