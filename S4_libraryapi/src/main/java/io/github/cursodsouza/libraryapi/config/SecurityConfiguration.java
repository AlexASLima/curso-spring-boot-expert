package io.github.cursodsouza.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                //.formLogin(Customizer.withDefaults()) para não mostrar o form login. Assim envia um hash na url para acessar.
                // abaixo vai chamar o form login criado e não o default acima
                .formLogin(configurer -> {
                    configurer.loginPage("/login").permitAll();
                })
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().authenticated();
                })
                .build();
    }
}
