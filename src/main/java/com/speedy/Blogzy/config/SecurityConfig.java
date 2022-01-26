package com.speedy.Blogzy.config;

import com.speedy.Blogzy.Filters.JWTTokenValidatorFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity()
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/", "/about").permitAll()
                .antMatchers("/login", "/register").anonymous()
                .antMatchers(HttpMethod.POST,"/api/authors", "/authors", "/api/authenticate", "/authenticate").anonymous()
//                .antMatchers("/api/authors/**", "/api/blogs/**").hasRole("ADMIN")
        .anyRequest().authenticated()
                .and()
//                .exceptionHandling().authenticationEntryPoint( new JWTAuthenticationEntryPoint())
//                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .deleteCookies("AUTH");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("speedy").password("{noop}speedy123").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("balu").password("{noop}balu123").roles("USER");
//    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return  new BCryptPasswordEncoder(10);
    }
}
