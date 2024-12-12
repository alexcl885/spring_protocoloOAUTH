package com.iesvdc.acceso.pistasdeportivas.configuracion;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
    @Autowired 
      DataSource dataSource;
      
      @Autowired
      public void configure(AuthenticationManagerBuilder amb) throws Exception {
        amb.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select username, password, enabled "+
                "from usuario where username = ?")
            .authoritiesByUsernameQuery("select u.username, u.tipo as 'authority' "+
                "from usuario u where username = ?" );
      }

      @Bean
      BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
      }

      @Bean
        public SecurityFilterChain filter(HttpSecurity http) throws Exception {
                
                // Con Spring Security 6.2 y 7: usando Lambda DSL

                return http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/webjars/**", "/img/**", "/css/**", "/js/**",
                                                "/register/**", "/ayuda/**", "/login",
                                                "/denegado", "/error", "/acerca")
                                .permitAll()
                                .requestMatchers("/horario", "/instalacion", "/horario/instalacion/**")
                                .authenticated()
                                .requestMatchers("/admin/**", "/admin/*/**", "/admin/*/*/**",
                                                "/admin/*/*/*/*/*/**",
                                                "/horario/*/**", "/horario/*/*/**",
                                                "/horario/*/*/*/*/*/**",
                                                "/instalacion/*/**", "/instalacion/*/*/**",
                                                "/instalacion/*/*/*/*/*/**")
                                // .authenticated()
                                .hasAuthority("ADMIN")
                                .requestMatchers("/pedidos/**", "/pedidos/*/**", "/pedidos/*/*/**",
                                                "/pedidos/*/*/*/**", "/pedidos/*/*/*/*/**")
                                // .authenticated()
                                .hasAuthority("OPERARIO")
                                .requestMatchers("/mis-reservas/**", "/mis-reservas/*/**",
                                                "/reservar/**", "/reservar/*/**",
                                                "/mis-datos/**", "/mis-datos/*/**", "/mis-datos/*/*/**",
                                                "/mis-datos/*/*/*/**")
                                // .authenticated()
                                .hasAuthority("USUARIO")
                // .anyRequest().permitAll()
                // ).headers(headers -> headers
                // .frameOptions(frameOptions -> frameOptions
                // .sameOrigin())
                // ).sessionManagement((session) -> session
                // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).exceptionHandling((exception) -> exception.accessDeniedPage("/denegado"))
                .formLogin((formLogin) -> formLogin
                                .loginPage("/login")
                                // .defaultSuccessUrl("/")
                                .permitAll())
                .oauth2Login((oauthlogin) -> oauthlogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/")
                                .permitAll())
                                
                // ).rememberMe(
                // Customizer.withDefaults()
                .logout((logout) -> logout
                                .invalidateHttpSession(true)
                                .logoutSuccessUrl("/")
                                // .deleteCookies("JSESSIONID") // no es necesario, JSESSIONID se hace
                                // por defecto
                                .permitAll())
                .csrf((protection) -> protection
                                .disable()
                // ).cors((protection)-> protection
                // .disable()
                ).build();

        }
}
