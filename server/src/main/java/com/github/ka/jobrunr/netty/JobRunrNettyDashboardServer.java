package com.github.ka.jobrunr.netty;

import org.jobrunr.dashboard.JobRunrDashboardWebServer;
import org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.utils.mapper.JsonMapper;

public class JobRunrNettyDashboardServer extends JobRunrDashboardWebServer {

    public JobRunrNettyDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper) {
        super(storageProvider, jsonMapper);
    }

    public JobRunrNettyDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper, int port) {
        super(storageProvider, jsonMapper, port);
    }

    public JobRunrNettyDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper, int port, String username, String password) {
        super(storageProvider, jsonMapper, port, username, password);
    }

    public JobRunrNettyDashboardServer(StorageProvider storageProvider, JsonMapper jsonMapper, JobRunrDashboardWebServerConfiguration configuration) {
        super(storageProvider, jsonMapper, configuration);
    }
}
