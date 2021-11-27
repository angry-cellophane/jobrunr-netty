package com.github.ka.jobrunr.spring.api;

import com.github.ka.jobrunr.spring.QueryParams;
import lombok.AllArgsConstructor;
import org.jobrunr.jobs.states.StateName;
import org.jobrunr.storage.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Service
@AllArgsConstructor
public class ApiEndpoints {

    final RunrApi api;
    final ApiResponses responses;

    public RouterFunction<ServerResponse> endpoints() {
        return RouterFunctions.route()
                .GET("/jobs", accept(MediaType.APPLICATION_JSON), request -> {
                    var state = request.queryParam("state")
                            .map(s -> StateName.valueOf(s.toUpperCase()))
                            .orElse(StateName.ENQUEUED);
                    var pageRequest = QueryParams.parse(request.queryParams(), PageRequest.class);
                    return responses.ok(api.findJobByState(state, pageRequest));
                })
                .GET("/jobs/{id}", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = UUID.fromString(request.pathVariable("id"));
                    return responses.ok(api.getJobById(id));
                })
                .DELETE("/jobs/{id}", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = UUID.fromString(request.pathVariable("id"));
                    api.deleteJobById(id);
                    return responses.noContent();
                })
                .POST("/jobs/{id}/requeue", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = UUID.fromString(request.pathVariable("id"));
                    api.requeueJobById(id);
                    return responses.noContent();
                })
                .GET("/problems", accept(MediaType.APPLICATION_JSON), request -> {
                    return responses.ok(api.getProblems());
                })
                .DELETE("/problems/{type}", accept(MediaType.APPLICATION_JSON), request -> {
                    var type = request.pathVariable("type");
                    api.deleteProblemByType(type);
                    return responses.noContent();
                })
                .GET("/recurring-jobs", accept(MediaType.APPLICATION_JSON), request -> {
                    return responses.ok(api.getRecurringJobs());
                })
                .DELETE("/recurring-jobs/{id}", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = request.pathVariable("id");
                    api.deleteRecurringJob(id);
                    return responses.noContent();
                })
                .POST("/recurring-jobs/{id}/trigger", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = request.pathVariable("id");
                    api.triggerRecurringJob(id);
                    return responses.noContent();
                })
                .GET("/servers", accept(MediaType.APPLICATION_JSON), request -> {
                    return responses.ok(api.getBackgroundJobServers());
                })
                .GET("/version", accept(MediaType.APPLICATION_JSON), request -> {
                    return responses.ok(api.getVersion());
                })
                .build();
    }
}
