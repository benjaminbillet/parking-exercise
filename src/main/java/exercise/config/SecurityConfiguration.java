package exercise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;


@Configuration
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final SecurityProblemSupport problemSupport;

  public SecurityConfiguration(SecurityProblemSupport problemSupport) {
    this.problemSupport = problemSupport;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .exceptionHandling()
      // deal with RFC7807 when authentication exception are thrown
      .accessDeniedHandler(problemSupport)
    .and()
       // disable X-Frame header(s) https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options
      .headers()
      .frameOptions().disable()
    .and()
      .authorizeRequests()
      .antMatchers("/api/**").permitAll();
  }
}
