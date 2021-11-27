package com.github.ka.jobrunr.spring.api;

import lombok.AllArgsConstructor;
import org.jobrunr.utils.mapper.JsonMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ApiResponses {

    final JsonMapper mapper;

    Mono<ServerResponse> ok(Object o) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(mapper.serialize(o));
    }

    Mono<ServerResponse> noContent() {
        return ServerResponse.status(HttpStatus.NO_CONTENT).build();
    }

}
