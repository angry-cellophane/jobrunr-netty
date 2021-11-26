package com.github.ka.jobrunr.spring;

import com.github.ka.jobrunr.spring.api.RunrApi;
import com.github.ka.jobrunr.spring.api.ApiEndpoints;
import org.jobrunr.dashboard.ui.model.problems.ProblemsManager;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.utils.mapper.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;
import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;

@Configuration
public class JobRunrEndpoints {

    @Bean
    ProblemsManager problemsManager(StorageProvider storageProvider) {
        return new ProblemsManager(storageProvider);
    }

    @Bean
    NoopJobRunrDashboardServer noopJobRunrDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper) {
        return new NoopJobRunrDashboardServer(storageProvider, jsonMapper);
    }

    @Bean
    CorsWebFilter staticFilesCorsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedHeader("Access-Control-Allow-Origin");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/dashboard/**", corsConfig);

        return new CorsWebFilter(source);
    }

    @Bean
    RouterFunction<ServerResponse> endpoints(RunrApi api) {
        return RouterFunctions.route()
                .path("/api", () -> ApiEndpoints.apiEndpoints(api))
                .resources("/dashboard/**", new ClassPathResource("org/jobrunr/dashboard/frontend/build/"))
                .GET("/", request -> permanentRedirect(URI.create("/dashboard/index.html")).build())
                .GET("/dashboard", request -> permanentRedirect(URI.create("/dashboard/index.html")).build())
                .build();
    }
}
