package com.ops.monitor.regitryserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);

        http
            .authorizeRequests()
                /** and enable healthcheck to point to "healthCheckPath": "/actuator/health"
                 *  as described in https://github.com/projectkudu/kudu/wiki/Health-Check-(Preview) **/
                .antMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
                .and()
            .csrf().ignoringAntMatchers("/eureka/**")
                .and()
            .httpBasic();

    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                         /** set in Application Settings variable `EUREKA_PASSWORD` */
                        .password(System.getenv("EUREKA_PASSWORD"))
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

}
