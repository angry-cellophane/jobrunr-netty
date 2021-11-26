package com.github.ka.jobrunr.spring;

import org.jobrunr.dashboard.JobRunrDashboardWebServer;
import org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.utils.mapper.JsonMapper;

import static org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration.usingStandardDashboardConfiguration;

public class NoopJobRunrDashboardServer extends JobRunrDashboardWebServer {

    public NoopJobRunrDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper) {
        this(storageProvider, jsonMapper, usingStandardDashboardConfiguration());
    }

    public NoopJobRunrDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper, int port) {
        this(storageProvider, jsonMapper, usingStandardDashboardConfiguration().andPort(port));
    }

    public NoopJobRunrDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper, int port, String username, String password) {
        this(storageProvider, jsonMapper, usingStandardDashboardConfiguration().andPort(port).andBasicAuthentication(username, password));
    }

    public NoopJobRunrDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper, JobRunrDashboardWebServerConfiguration configuration) {
        super(storageProvider, jsonMapper, configuration);
    }

    @Override
    public void start() {
        // do nothing, everything's registered in spring
    }

    @Override
    public void stop() {
        // do nothing, everything's registered in spring
    }
}
