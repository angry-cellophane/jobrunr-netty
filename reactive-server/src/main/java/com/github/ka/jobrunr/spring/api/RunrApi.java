package com.github.ka.jobrunr.spring.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.dashboard.ui.model.RecurringJobUIModel;
import org.jobrunr.dashboard.ui.model.VersionUIModel;
import org.jobrunr.dashboard.ui.model.problems.Problems;
import org.jobrunr.dashboard.ui.model.problems.ProblemsManager;
import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.states.StateName;
import org.jobrunr.storage.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RunrApi {

    private final StorageProvider storageProvider;
    private final ProblemsManager problemsManager;


    Job getJobById(UUID jobId) {
        return storageProvider.getJobById(jobId);
    }

    void deleteJobById(UUID jobId) {
        log.info("received request to delete job " + jobId);
        var job = getJobById(jobId);
        job.delete("Job deleted via Dashboard");
        storageProvider.save(job);
        log.info("job " + jobId + " deleted successfully");
    }

    void requeueJobById(UUID jobId) {
        var job = getJobById(jobId);
        job.enqueue();
        storageProvider.save(job);
    }

    Page<Job> findJobByState(StateName state, PageRequest pageRequest) {
        return storageProvider.getJobPage(state, pageRequest);
    }

    Problems getProblems() {
        return problemsManager.getProblems();
    }

    void deleteProblemByType(String type) {
        log.info("received request to delete problems with type " + type);
        problemsManager.dismissProblemOfType(type);
        log.info("problems with type " + type + " deleted");
    }

    Flux<RecurringJobUIModel> getRecurringJobs() {
         return Flux.fromIterable(storageProvider
                    .getRecurringJobs()
                 )
                .map(RecurringJobUIModel::new);
    }

    void deleteRecurringJob(String jobId) {
        log.info("received request to delete recurring job with jobId " + jobId);
        storageProvider.deleteRecurringJob(jobId);
        log.info("recurring job with jobId " + jobId + " deleted");
    }

    void triggerRecurringJob(String jobId) {
        var recurringJob = storageProvider.getRecurringJobs()
                .stream()
                .filter(rj -> jobId.equals(rj.getId()))
                .findFirst()
                .orElseThrow(() -> new JobNotFoundException(jobId));

        final Job job = recurringJob.toEnqueuedJob();
        storageProvider.save(job);
    }

    List<BackgroundJobServerStatus> getBackgroundJobServers() {
        return storageProvider.getBackgroundJobServers();
    }

    VersionUIModel getVersion() {
        return new VersionUIModel();
    }
}
