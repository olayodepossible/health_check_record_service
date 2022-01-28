package com.possible.patientdatabatchloader.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

@Component
@EnableBatchProcessing
public class BatchConfig implements BatchConfigurer {

    private JobRepository jobRepository; // configure to store batch meta data
    private JobExplorer jobExplorer; // fetch stored meta data from db
    private JobLauncher jobLauncher;  // configure to run job with given parameters
}
