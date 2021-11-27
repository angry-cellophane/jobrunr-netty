package com.github.ka.jobrunr.spring.sse;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.storage.JobStats;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.listeners.JobStatsChangeListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.annotation.PreDestroy;

@Slf4j
@Service
public class JobStatsSse {

    class Listener implements JobStatsChangeListener {
        @Override
        public void onChange(JobStats stats) {
            sink.emitNext(stats, Sinks.EmitFailureHandler.FAIL_FAST);
        }
    }

    private final Sinks.Many<JobStats> sink;
    private final StorageProvider storage;
    private final Listener listener;

    public JobStatsSse(StorageProvider storage) {
        this.storage = storage;
        this.sink = Sinks.many().replay().latest();
        this.listener = new Listener();

        storage.addJobStorageOnChangeListener(this.listener);
    }

    @PreDestroy
    public void stop() {
        this.storage.removeJobStorageOnChangeListener(this.listener);
    }

    public Flux<JobStats> getJobStats() {
        return sink.asFlux();
    }
}
