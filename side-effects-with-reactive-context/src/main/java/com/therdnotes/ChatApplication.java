package com.therdnotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
@Configuration
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll())
                .addFilterAfter((exchange, chain) -> Mono
                        .justOrEmpty(exchange.getRequest().getHeaders().get("name"))
                        .switchIfEmpty(Mono.just(List.of("Stranger")))
                        .flatMap(name -> chain.filter(exchange).contextWrite(ctx -> ctx.put("user", name.get(0)))), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
