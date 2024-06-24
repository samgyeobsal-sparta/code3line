package sparta.code3line.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sparta.code3line.jwt.JwtService;
import sparta.code3line.security.handler.AuthenticationEntryPointImpl;
import sparta.code3line.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {

        return new JwtAuthenticationFilter(jwtService, userDetailsService);

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();

    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {

        return new OAuth2AuthenticationSuccessHandler(jwtService, objectMapper);

    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {

        return new AuthenticationEntryPointImpl(jwtService, objectMapper);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPoint())
        );

        http.authorizeHttpRequests(request ->
                request
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/users/signup").permitAll()
                        .requestMatchers("/users/email/**").permitAll()
                        .requestMatchers("/auth/reissue").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/boards/**").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/comments/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated());


        http.addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);

        http.oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                .loginPage("/templates/login.html")
                .successHandler(oAuth2AuthenticationSuccessHandler())
        );

        return http.build();

    }

}
