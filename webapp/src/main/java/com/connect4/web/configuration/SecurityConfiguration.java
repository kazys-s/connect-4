package com.connect4.web.configuration;

import com.connect4.web.authentication.PlayerPreAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import static com.connect4.web.endpoints.games.GameController.GAMES_URL;
import static com.connect4.web.endpoints.players.PlayerController.PLAYERS_URL;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PlayerPreAuthenticationService preAuthenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(ssoFilter(), RequestHeaderAuthenticationFilter.class)
                .authenticationProvider(preauthAuthProvider())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .anonymous().authorities("ROLE_ANONYMOUS").and()
                .csrf().disable()   // disabling for easy demoing
                .authorizeRequests()
                .antMatchers(PLAYERS_URL).permitAll()
                .antMatchers(GAMES_URL + "/**").authenticated()
                .anyRequest().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(preauthAuthProvider());
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
        PreAuthenticatedAuthenticationProvider preauthAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preauthAuthProvider.setPreAuthenticatedUserDetailsService(preAuthenticationService);
        return preauthAuthProvider;
    }

    @Bean
    public RequestHeaderAuthenticationFilter ssoFilter() throws Exception {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setExceptionIfHeaderMissing(false);
        filter.setPrincipalRequestHeader("sso");
        return filter;
    }
}