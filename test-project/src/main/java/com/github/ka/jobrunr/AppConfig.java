package com.github.ka.jobrunr;

import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.mariadb.MariaDbStorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    StorageProvider storageProvider(DataSource dataSource, JobMapper jobMapper) {
        var p = new MariaDbStorageProvider(dataSource);
        p.setJobMapper(jobMapper);

        return p;
    }
}
