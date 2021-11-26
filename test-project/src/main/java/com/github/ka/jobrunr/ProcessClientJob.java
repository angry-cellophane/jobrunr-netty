package com.github.ka.jobrunr;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProcessClientJob {

    final JobRunrDashboardLogger logger = new JobRunrDashboardLogger(log);

    public static record ProcessClientRequest(String clientName, long timestamp) {}

    @Job(name = "process client data", retries = 3)
    public void run(JobContext ctx, ProcessClientRequest request) throws Exception {
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new RuntimeException("oopsie, error");
        }

        var progressBar = ctx.progressBar(2);
        logger.info("starting processing data for client {} at time {}", request.clientName(), request.timestamp());
        progressBar.increaseByOne();
        TimeUnit.SECONDS.sleep(15);
        progressBar.increaseByOne();
        logger.info("processed data for client {} at time {}", request.clientName(), request.timestamp());
    }
}
