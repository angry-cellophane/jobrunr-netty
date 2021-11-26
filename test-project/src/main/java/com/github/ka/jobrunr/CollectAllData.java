package com.github.ka.jobrunr;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CollectAllData {

    final JobScheduler scheduler;

    @Recurring(id = "collect-all-data", cron = "*/5 * * * *")
    @Job(name = "collect all data")
    public void run() {
        log.info("done");
    }
}
