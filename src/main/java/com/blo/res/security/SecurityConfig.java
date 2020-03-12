package com.blo.res.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter 
{
	 @Autowired
	    PasswordEncoder passwordEncoder;
	 

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder encoder = 
//          PasswordEncoderFactories.createDelegatingPasswordEncoder();		//commented to use BCrypt instead
       
        auth
          .inMemoryAuthentication()
          .withUser("user")
          .password(passwordEncoder.encode("password"))
          .roles("USER")
          .and()
          .withUser("admin")
          .password(passwordEncoder.encode("admin"))
          .roles("USER", "ADMIN");
    }

	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        .httpBasic()
	        .and()
	          .authorizeRequests()
//	          .anyRequest()
//	          .authenticated()
	          .antMatchers(HttpMethod.GET).hasAnyRole("USER","ADMIN")
	          .antMatchers(HttpMethod.POST).hasRole("ADMIN")
	          .antMatchers(HttpMethod.PUT).hasRole("ADMIN")
	          .antMatchers(HttpMethod.PATCH).hasRole("ADMIN")
	          .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
	          .and()
              .csrf().disable()
              .formLogin()
	        ;
	    }
	 
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 

//	    /*@Bean
//	    public UserDetailsService userDetailsService() {
//	        //ok for demo
//	        User.UserBuilder users = User.withDefaultPasswordEncoder();
//
//	        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//	        manager.createUser(users.username("user").password("password").roles("USER").build());
//	        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
//	        return manager;
//	    }*/
	 
}