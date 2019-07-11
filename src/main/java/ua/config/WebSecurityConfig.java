package ua.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import ua.security.AccessDeniedEntryPoint;
import ua.security.RedirectionAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String KEY = "theKey";

    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Filter
        http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
        http.authorizeRequests()
                .antMatchers("/", "/login", "/accessDenied", "/error/**", "/site/**").permitAll()
                .anyRequest()
                .authenticated();

        // Login
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/?error")
                .successHandler(new RedirectionAuthenticationSuccessHandler())
                .permitAll();

        // Cookie login
        http.rememberMe()
                .key(KEY)
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("_identity");

        // Logout
        http.logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                //.deleteCookies("rememberMe")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

        // Access denied
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
    }

    /**
     *  For support BCryptPasswordEncoder PHP
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(){
            @Override
            public String encode(CharSequence rawPassword){
                return super.encode(rawPassword).replaceFirst("2a", "2y");
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword){
                return super.matches(rawPassword, encodedPassword.replaceFirst("2y", "2a"));
            }
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint(){
        AccessDeniedEntryPoint authenticationEntryPoint = new AccessDeniedEntryPoint("/error/accessDenied");
        authenticationEntryPoint.setUseForward(true);
        return authenticationEntryPoint;
    }

    private CharacterEncodingFilter characterEncodingFilter(){
        return new CharacterEncodingFilter("UTF-8", true);
    }
}
