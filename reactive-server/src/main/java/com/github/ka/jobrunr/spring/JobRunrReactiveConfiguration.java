package com.github.ka.jobrunr.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.ka.jobrunr.spring.api.ApiEndpoints;
import com.github.ka.jobrunr.spring.sse.SseEndpoints;
import org.jobrunr.dashboard.ui.model.problems.ProblemsManager;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.utils.mapper.JsonMapper;
import org.jobrunr.utils.mapper.jackson.JacksonJsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;

@Configuration
public class JobRunrReactiveConfiguration {

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
    RouterFunction<ServerResponse> endpoints(ApiEndpoints api, SseEndpoints sse) {
        return RouterFunctions.route()
                .path("/api", () -> api.endpoints())
                .resources("/dashboard/**", new ClassPathResource("org/jobrunr/dashboard/frontend/build/"))
                .GET("/", request -> permanentRedirect(URI.create("/dashboard/index.html")).build())
                .GET("/dashboard", request -> permanentRedirect(URI.create("/dashboard/index.html")).build())
                .path("/sse", () -> sse.endpoints())
                .build();
    }

    @Bean
    ObjectMapper objectMapper() {
        var f = ReflectionUtils.findField(JacksonJsonMapper.class, "objectMapper");
        ReflectionUtils.makeAccessible(f);
        var mapper = new JacksonJsonMapper(
                new ObjectMapper()
                        .registerModule(new SimpleModule() {{
                            addSerializer(Flux.class, new FluxSerializer());
                        }})
        );
        return (ObjectMapper) ReflectionUtils.getField(f, mapper);
    }
}
