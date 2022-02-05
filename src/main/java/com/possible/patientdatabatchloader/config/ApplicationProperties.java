package com.possible.patientdatabatchloader.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Properties specific to Patient Batch Loader.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Component
@Data
@Validated
public class ApplicationProperties {
    private final BatchInputData batchInputData= new BatchInputData();

    public BatchInputData getBatchInputData(){
        return batchInputData;
    }

    @Data
    public static class BatchInputData {
        private String inputPath; // set default value in case of failure
    }


}
