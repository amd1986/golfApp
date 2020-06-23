package fr.greta.golf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(this.dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials, active from users where username = ?")
                .authoritiesByUsernameQuery("select users_username as principal, roles_role as role from roles_users where users_username = ?")
                .rolePrefix("ROLE_")
                .passwordEncoder(this.getPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").defaultSuccessUrl("/");
        http.authorizeRequests().antMatchers("/{[fr|en|es]}/admin/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/{[fr|en|es]}/user/**").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/{[fr|en|es]}/editor/**").hasAnyRole("EDITOR", "MANAGER");
        http.authorizeRequests().antMatchers("/{[fr|en|es]}/manager/**").hasRole("MANAGER");
        http.authorizeRequests().antMatchers("/{[fr|en|es]}/superAdmin/**").hasRole("USERADMIN");
        http.logout().invalidateHttpSession(true).logoutSuccessUrl("/");
    }
}
