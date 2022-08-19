package com.santoshkc.stripespringboot.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig  {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests()
            .antMatchers("/get-cards", "/setup-intent", "/create-payment-intent")
            .authenticated()
            .anyRequest()
            .permitAll()
            .and()
            .oauth2ResourceServer()
            .jwt()

        return http.build()
    }
}