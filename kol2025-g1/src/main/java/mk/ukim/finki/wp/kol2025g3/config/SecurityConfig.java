package mk.ukim.finki.wp.kol2025g3.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class is used to configure user login on path '/login' and logout on path '/logout'.
 * The only public pages in the application should be '/' and '/expenses'.
 * All other pages should be visible only for a user with admin role.
 * Furthermore, in the "list.html" template, the 'Edit', 'Delete', 'Add' buttons should only be
 * visible for a user with admin role.
 * The 'Extend' button should only be visible for a user with role of user.
 * <p>
 * For login inMemory users should be used. Their credentials are given below:
 * [{
 * username: "user",
 * password: "user",
 * role: "USER"
 * },
 * <p>
 * {
 * username: "admin",
 * password: "admin",
 * role: "ADMIN"
 * }]
 */

@Configuration //za Spring da ja chita klasata pri startuvanje
@EnableWebSecurity //za da se vkluci Spring Security
@EnableMethodSecurity //ovozmozuva proverka so roles
public class SecurityConfig {
    @Bean //se kreira bean za enkodiranje lozinki
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //tuka gi definirame korisnicite
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder() //user
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder() //admin
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin); //korisnicite se vo memorija ne vo baza
    }

    @Bean //glavnata security logika
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //go iskluchuva CSFR, potrebno za POST formi, bez ova Selenium testovi pagjaat
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) //dozvoluva h2 console
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/expenses").permitAll() //javni stranici sekoj moze da gi gleda bez login
                        .requestMatchers("/expenses/extend/**").hasRole("USER") //samo user moze extend, admin nema pristap
                        .anyRequest().hasRole("ADMIN") //se drugo (add,edit,delete) samo admin moze
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/expenses", true) //avtomatski /login page, po login-/expenses
                        .permitAll() //login dostapen za site
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") //logout na /logout
                        .logoutSuccessUrl("/") //po logout-/ (home)
                );

        return http.build();
    }
}
