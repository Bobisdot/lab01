package kz.iitu.springlab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize жұмыс істеуі үшін (BookService.delete)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Барлығына ашық
                        .requestMatchers("/", "/login", "/error", "/css/**", "/js/**", "/favicon.ico").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        // GET /books және /api/books - USER немесе ADMIN
                        .requestMatchers(HttpMethod.GET, "/books/**", "/api/books/**").hasAnyRole("USER", "ADMIN")
                        // POST, PUT, DELETE /books - тек ADMIN
                        .requestMatchers("/books/**", "/api/books/**").hasRole("ADMIN")

                        // /admin - тек ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // HelloController (/api/hello, /api/info, /api/palindrome) - кірген кез келген қолданушы
                        .requestMatchers("/api/**").authenticated()

                        // Қалған барлық беттер жабық
                        .anyRequest().denyAll()
                )
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/?logout"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}