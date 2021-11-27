package com.github.ka.jobrunr.spring.sse;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.storage.BackgroundJobServerStatus;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.listeners.BackgroundJobServerStatusChangeListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.annotation.PreDestroy;
import java.util.List;

@Slf4j
@Service
public class BackgroundServerSse {

    class Listener implements BackgroundJobServerStatusChangeListener {

        @Override
        public void onChange(List<BackgroundJobServerStatus> changedServerStates) {
            sink.emitNext(changedServerStates, Sinks.EmitFailureHandler.FAIL_FAST);
        }
    }

    private final Sinks.Many<List<BackgroundJobServerStatus>> sink;
    private final StorageProvider storage;
    private final Listener listener;

    public BackgroundServerSse(StorageProvider storage) {
        this.storage = storage;
        this.sink = Sinks.many().replay().latest();
        this.listener = new Listener();

        storage.addJobStorageOnChangeListener(this.listener);
    }

    @PreDestroy
    public void stop() {
        this.storage.removeJobStorageOnChangeListener(this.listener);
    }

    public Flux<List<BackgroundJobServerStatus>> getServers() {
        return sink.asFlux();
    }
}
