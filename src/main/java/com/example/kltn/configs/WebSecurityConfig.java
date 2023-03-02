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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailServiceIplm userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(WHITE_LIST_URLS).permitAll();

        http
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasAnyAuthority(Constants.USER.ROLE.ADMIN)
                .antMatchers(HttpMethod.POST,"/api/**").hasAnyAuthority(Constants.USER.ROLE.CUSTOMER, Constants.USER.ROLE.ADMIN)
                .antMatchers(HttpMethod.PUT,"/api/**").hasAnyAuthority(Constants.USER.ROLE.CUSTOMER, Constants.USER.ROLE.ADMIN)
                .antMatchers(HttpMethod.DELETE,"/api/**").hasAnyAuthority(Constants.USER.ROLE.CUSTOMER, Constants.USER.ROLE.ADMIN)
                .antMatchers(HttpMethod.GET, "/api/**").permitAll();


        http.addFilterBefore(new UserAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
