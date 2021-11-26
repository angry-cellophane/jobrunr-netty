package com.github.ka.jobrunr.spring;

import com.github.ka.jobrunr.spring.api.Api;
import com.github.ka.jobrunr.spring.api.ApiEndpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Endpoints {

    @Bean
    RouterFunction<ServerResponse> endpoints(Api api) {
        return RouterFunctions.route()
                .path("/api", () -> ApiEndpoints.apiEndpoints(api))
                .resources("")
                .build();
    }
}
