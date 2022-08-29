package io.github.pedrogennaro.config;

import io.github.pedrogennaro.security.jwt.JwtAuthFilter;
import io.github.pedrogennaro.security.jwt.JwtService;
import io.github.pedrogennaro.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Autenticando usuário em memória
//        auth.inMemoryAuthentication()
//                .passwordEncoder(passwordEncoder())
//                .withUser("pedro")
//                .password(passwordEncoder().encode("123"))
//                .roles("USER");

        //Autenticado usuário via banco de dados
        auth.userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/clientes/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/pedidos/**")
                    .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/produtos/**")
                    .hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/usuarios/**")
                    .permitAll()
                .anyRequest().authenticated()
            .and()
                //.httpBasic(); // para autenticação básica
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
