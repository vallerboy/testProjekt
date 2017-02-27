package pl.oskarpolak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pl.oskarpolak.auth.UserAuth;

/**
 * Created by OskarPraca on 2017-02-19.
 */
@Configuration
@EnableWebSecurity
public class SecureConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserAuth userAuth;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers( "/resources/**", "/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successForwardUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll();
        http.antMatcher("/rest/**").csrf().disable();
    }

    @Autowired
    javax.sql.DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

        ShaPasswordEncoder encoder = new ShaPasswordEncoder();

        auth.userDetailsService(userAuth).passwordEncoder(encoder);

//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery(
//                        "select username,password, role  from user where username=?")
//                .authoritiesByUsernameQuery(
//                        "select username, role from user where username=?");
    }


}
