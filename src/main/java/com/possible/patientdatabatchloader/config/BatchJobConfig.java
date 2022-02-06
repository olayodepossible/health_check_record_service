package com.possible.patientdatabatchloader.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Configuration
public class BatchJobConfig {
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;

//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;

//    @Autowired
//    private ApplicationProperties applicationProperties;

    @Bean
    JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor (JobRegistry jobRegistry){
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean
    public Job job (Step step, JobBuilderFactory jobBuilderFactory, ApplicationProperties applicationProperties){
        return jobBuilderFactory
                .get(Constants.JOB_NAME)
                .validator(validator(applicationProperties))
                .start(step)
                .build();
    }


    @Bean
    public JobParametersValidator validator(ApplicationProperties applicationProperties) {
//        return new JobParametersValidator() {
//            @Override
//            public void validate(JobParameters parameters) throws JobParametersInvalidException {
//                String fileName = parameters.getString(Constants.JOB_PARAM_FILE_NAME);
//                if (StringUtils.isBlank(fileName)) {
//                    throw new JobParametersInvalidException(
//                            "The patient-batch-loader.fileName parameter is required.");
//                }
//                try {
//                    Path file = Paths.get(applicationProperties.getBatchInputData().getInputPath() +
//                            File.separator + fileName);
//                    if (Files.notExists(file) || !Files.isReadable(file)) {
//                        throw new Exception("File did not exist or was not readable");
//                    }
//                } catch (Exception e) {
//                    throw new JobParametersInvalidException(
//                            "The input path + patient-batch-loader.fileName parameter needs to " +
//                                    "be a valid file location.");
//                }
//            }
//        };

        return parameters -> {
            String fileName = parameters.getString(Constants.JOB_PARAM_FILE_NAME);
            if (StringUtils.isBlank(fileName)) {
                throw new JobParametersInvalidException(
                        "The patient-batch-loader.fileName parameter is required.");
            }
            try {
                Path file = Paths.get(applicationProperties.getBatchInputData().getInputPath() +
                        File.separator + fileName);
                if (Files.notExists(file) || !Files.isReadable(file)) {
                    throw new Exception("File did not exist or was not readable");
                }
            } catch (Exception e) {
                throw new JobParametersInvalidException(
                        "The input path + patient-batch-loader.fileName parameter needs to " +
                                "be a valid file location.");
            }
        };
    }


    // Step bean
    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory) throws Exception{
        return stepBuilderFactory
                .get(Constants.STEP_NAME)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        log.debug("HELLO, TESTING BATCH PROCESS");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
