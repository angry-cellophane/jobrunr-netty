package com.github.ka.jobrunr.spring.sse;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.JobId;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.listeners.JobChangeListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static org.jobrunr.jobs.states.StateName.*;

@Slf4j
public class JobSse {

    @Value
    static class Listener implements JobChangeListener {

        Sinks.Many<Job> sink;
        StorageProvider storage;
        JobId jobId;

        @Override
        public void onChange(Job job) {
            try {
                sink.emitNext(job, Sinks.EmitFailureHandler.FAIL_FAST);
            } catch (Exception e) {
                close();
                throw e;
            }

            if (job.hasState(SUCCEEDED) || job.hasState(FAILED) || job.hasState(DELETED)) {
                close();
            }
        }

        @Override
        public void close() {
            storage.removeJobStorageOnChangeListener(this);
            sink.tryEmitComplete();
        }
    }

    static Flux<Job> register(JobId jobId, StorageProvider storage) {
        var sink = Sinks.many().unicast().<Job>onBackpressureBuffer();
        var listener = new Listener(sink, storage, jobId);
        storage.addJobStorageOnChangeListener(listener);

        return sink.asFlux()
                .doAfterTerminate(listener::close);
    }
}
