package libraryProjectGroup.libraryProject.config;
import libraryProjectGroup.libraryProject.user.UserDetailService;
import org.springframework.context.annotation.Configuration;;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(UserDetailService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        HttpMethod.POST, "/api/v1/users"
                                ).permitAll()
                                .requestMatchers(
                                        "/login"
                                ).permitAll()
                                .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwt ->
                                        jwt.jwtAuthenticationConverter(authenticationConverter())
                                )
                );
        return http.build();
    }

    private JwtAuthenticationConverter authenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        authoritiesConverter.setAuthoritiesClaimName("roles");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
