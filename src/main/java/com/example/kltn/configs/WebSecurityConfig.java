package com.example.kltn.configs;


import com.example.kltn.constants.Constants;
import com.example.kltn.filter.UserAuthorizationFilter;
import com.example.kltn.services.auth.UserDetailServiceIplm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthenticationProvider authenticationProvider;

    private static final String[] WHITE_LIST_URLS = {
            "/api/registerTest",
            "/api/login/**",
            "/api/phoneLogin/**",
            "/api/register/phone",
            "/api/register-email",
            "/api/resetPassword/**",
            "/api/verifyRegistration/*",
            "/api/resendVerifyToken",
            "/api/savePassword",
            "/api/refreshToken",
            "/api/voucher",
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/ws/**"
    };
    private static final String[] ADMINSYSROLE = {
            Constants.USER.ROLE.ADMINSYS
    };
    private static final String[] ADMINSHOPROLE = {
            Constants.USER.ROLE.ADMINSYS,
            Constants.USER.ROLE.ADMINSHOP
    };
    private static final String[] CUSTOMERROLE = {
            Constants.USER.ROLE.ADMINSYS,
            Constants.USER.ROLE.ADMINSHOP,
            Constants.USER.ROLE.CUSTOMER
    };

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().antMatchers(WHITE_LIST_URLS).permitAll();

        http
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests()
                .antMatchers("/api/adminSys/**").hasAnyAuthority(ADMINSYSROLE)
                .antMatchers("/api/adminShop/**").hasAnyAuthority(ADMINSHOPROLE)
                .antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority(CUSTOMERROLE)
                .antMatchers(HttpMethod.PUT, "/api/**").hasAnyAuthority(CUSTOMERROLE)
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority(CUSTOMERROLE)
                .antMatchers(HttpMethod.GET, "/api/**").permitAll();

        http.addFilterBefore(new UserAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}
