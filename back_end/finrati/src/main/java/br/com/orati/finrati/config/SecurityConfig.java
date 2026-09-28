package br.com.orati.finrati.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    /**
     * .anyRequest().authenticated(): agora exige login em qualquer rota (era
     * .permitAll(), liberando tudo)
     * .httpBasic(...): ativa explicitamente o mecanismo de usuário/senha via aquele
     * popup
     * 
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(httpBasic -> httpBasic.realmName("Finrati"));
        return http.build();
    }
}
