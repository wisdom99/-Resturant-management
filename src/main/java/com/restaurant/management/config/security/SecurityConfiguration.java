/**
 *
 */
package com.restaurant.management.config.security;

import com.restaurant.management.config.security.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Autowired
//    @SuppressWarnings("SpringJavaAutowiringInspection")
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**", "/img/**", "/webjars/**", "/vendor");
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/registration")
                .permitAll()
                .antMatchers("/")
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/process-logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .addFilterBefore(authenticationFilter(), JsonAuthenticationFilter.class)
                .userDetailsService(userDetailsServiceBean());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {

        return new CustomUserDetailsService();
    }

    @Bean
    public JsonAuthenticationFilter authenticationFilter() throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        filter.setFilterProcessesUrl("/process-login");
        filter.setPostOnly(true);
        filter.setUsernameParameter("userId");
        filter.setPasswordParameter("secret");

        return filter;
    }

    @Bean
    public RestAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    @Bean
    public RestAuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public RestLogoutSuccessHandler logoutSuccessHandler(){
        return new RestLogoutSuccessHandler();
    }
}
