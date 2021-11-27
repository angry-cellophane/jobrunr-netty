package com.github.ka.jobrunr.spring.sse;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

public class SseResponses {

    static ServerResponse.BodyBuilder ok() {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .header("Cache-Control", "no-cache,public")
                .header("Content-Type", "text/event-stream")
                .header("Connection", "keep-alive")
                .header("Language", "en-US")
                .header("Charset", "UTF-8")
                .header("Access-Control-Allow-Origin", "*");
    }
}
