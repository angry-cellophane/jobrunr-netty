package com.github.ka.jobrunr.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class StaticContentFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getPath().pathWithinApplication().value().startsWith("/dashboard")) {
            exchange.getResponse().getHeaders()
                    .add("Access-Control-Allow-Origin", "*");
        }
        return chain.filter(exchange);
    }
}
