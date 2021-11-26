package com.github.ka.jobrunr.spring.api;

import com.github.ka.jobrunr.spring.QueryParams;
import org.jobrunr.dashboard.ui.model.RecurringJobUIModel;
import org.jobrunr.jobs.states.StateName;
import org.jobrunr.storage.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

public class ApiEndpoints {

    public static RouterFunction<ServerResponse> apiEndpoints(RunrApi api) {
        return RouterFunctions.route()
                .GET("/jobs", accept(MediaType.APPLICATION_JSON), request -> {
                    var state = request.queryParam("state")
                            .map(s -> StateName.valueOf(s.toUpperCase()))
                            .orElse(StateName.ENQUEUED);
                    var pageRequest = QueryParams.parse(request.queryParams(), PageRequest.class);
                    return ServerResponse.ok().bodyValue(api.findJobByState(state, pageRequest));
                })
                .GET("/jobs/{id}", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = UUID.fromString(request.pathVariable("id"));
                    return ServerResponse.ok().bodyValue(api.getJobById(id));
                })
                .DELETE("/jobs/{id}", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = UUID.fromString(request.pathVariable("id"));
                    api.deleteJobById(id);
                    return ServerResponse.status(HttpStatus.NO_CONTENT).build();
                })
                .POST("/jobs/{id}/requeue", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = UUID.fromString(request.pathVariable("id"));
                    api.requeueJobById(id);
                    return ServerResponse.status(HttpStatus.NO_CONTENT).build();
                })
                .GET("/problems", accept(MediaType.APPLICATION_JSON), request -> {
                    return ServerResponse.ok().bodyValue(api.getProblems());
                })
                .DELETE("/problems/{type}", accept(MediaType.APPLICATION_JSON), request -> {
                    var type = request.pathVariable("type");
                    api.deleteProblemByType(type);
                    return ServerResponse.status(HttpStatus.NO_CONTENT).build();
                })
                .GET("/recurring-jobs", accept(MediaType.APPLICATION_JSON), request -> {
                    return ServerResponse.ok().body(BodyInserters.fromPublisher(api.getRecurringJobs(), RecurringJobUIModel.class));
                })
                .DELETE("/recurring-jobs/{id}", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = request.pathVariable("id");
                    api.deleteRecurringJob(id);
                    return ServerResponse.status(HttpStatus.NO_CONTENT).build();
                })
                .POST("/recurring-jobs/{id}/trigger", accept(MediaType.APPLICATION_JSON), request -> {
                    var id = request.pathVariable("id");
                    api.triggerRecurringJob(id);
                    return ServerResponse.status(HttpStatus.NO_CONTENT).build();
                })
                .GET("/servers", accept(MediaType.APPLICATION_JSON), request -> {
                    return ServerResponse.ok().bodyValue(api.getBackgroundJobServers());
                })
                .GET("/version", accept(MediaType.APPLICATION_JSON), request -> {
                    return ServerResponse.ok().bodyValue(api.getVersion());
                })
                .build();
    }
}
