package net.sni.graduation.config;

import lombok.RequiredArgsConstructor;
import net.sni.graduation.filter.AuthenticationFilter;
import net.sni.graduation.filter.JwtFilter;
import net.sni.graduation.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http, JwtFilter jwtFilter, AuthenticationFilter authenticationFilter) throws Exception {

        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");

        http
                .cors().and().csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/api/v1/login")
                    .authorizeHttpRequests(authorize -> authorize.antMatchers(HttpMethod.POST).permitAll())
                .antMatcher("/api/v1/refresh")
                    .authorizeHttpRequests(authorize -> authorize.antMatchers(HttpMethod.GET).permitAll())
                .antMatcher("/api/v1/users")
                    .authorizeHttpRequests(authorize -> authorize.antMatchers(HttpMethod.POST).permitAll())
                .antMatcher("/api/**")
                    .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .addFilter(authenticationFilter)
                .addFilterBefore(jwtFilter, AuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .antMatchers("/webjars/**", "/static/**").permitAll()
                                .antMatchers("/admin/**").hasRole("ADMIN")
                                .antMatchers("/login*").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin().loginPage("/login")
                .and()
                .logout()
                .deleteCookies()
                .permitAll();

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
