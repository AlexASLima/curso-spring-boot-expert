package io.github.cursodsouza.libraryapi.config;

import io.github.cursodsouza.libraryapi.security.CustomUserDetailsService;
import io.github.cursodsouza.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // comentar esse @ para desabilitar a segurança
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // Para habilitar colocar as regras de acesso nos endpoint.
public class SecurityConfiguration {
    // ================================================
    // MODO 1 - SEM SEGURANÇA
    // ================================================
    // Para DESABILITAR a segurança:
    // Deixe este @Bean ativo e o outro comentado.
    // @Bean // -  essa linha comentada desabilita o sem segurança
    public SecurityFilterChain securityFilterChainSemSeguranca(
            HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().permitAll();
                })
                .build();
    }

    // ================================================
    // MODO 2 - COM SEGURANÇA
    // ================================================
    // Para HABILITAR a segurança:
    // Descomente este @Bean e comente o anterior.
    @Bean
    public SecurityFilterChain securityFilterChainComSeguranca(
            HttpSecurity http) throws Exception {
        return http
                // Temporariamente desabilitado para
                // estudarmos primeiro a autenticação.
                // Depois habilitaremos a proteção CSRF.
                .csrf(AbstractHttpConfigurer::disable)
                // Configura nossa página de login.
                .formLogin(configurer -> {
                    configurer.loginPage("/login").permitAll();
                })
                // Configura o encerramento da sessão do usuário
                .logout(configurer -> {
                    configurer
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll();
                })

                // Configura as permissões de acesso.
                .authorizeHttpRequests(authorize -> {
                    // Permite acessar a página de login.
                    authorize.requestMatchers("/login", "/login/**").permitAll();
                    // Exige autenticação para as demais URLs.
                    authorize.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    //public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    public UserDetailsService userDetailsService(UsuarioService usuarioService) {
        /*
        UserDetails user1 = User.builder()
                .username("usuario")
                .password(encoder.encode("123"))
                .roles("USER")
                .build();

        UserDetails user2 = User.builder()
                .username("admin")
                .password(encoder.encode("321"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, user2); */

        return new CustomUserDetailsService(usuarioService);
    }
}
