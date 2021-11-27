package com.github.ka.jobrunr.spring.sse;

import lombok.AllArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.jobrunr.storage.JobStats;
import org.jobrunr.storage.StorageProvider;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Service
@AllArgsConstructor
public class SseEndpoints {

    final JobStatsSse jobStatsSse;
    final BackgroundServerSse backgroundServerSse;
    final StorageProvider storageProvider;

    public RouterFunction<ServerResponse> endpoints() {
        return RouterFunctions.route()
                .GET("/jobstats", accept(MediaType.TEXT_EVENT_STREAM), request -> {
                    return SseResponses.ok().body(jobStatsSse.getJobStats(), JobStats.class);
                })
                .GET("/servers", accept(MediaType.TEXT_EVENT_STREAM), request -> {
                    return SseResponses.ok().body(backgroundServerSse.getServers(), JobStats.class);
                })
                .GET("/jobs/{id}", accept(MediaType.TEXT_EVENT_STREAM), request -> {
                    var id = JobId.parse(request.pathVariable("id"));
                    return SseResponses.ok().body(JobSse.register(id, storageProvider), JobStats.class);
                })
                .build();
    }
}
