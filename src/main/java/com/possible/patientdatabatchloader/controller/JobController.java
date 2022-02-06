package com.possible.patientdatabatchloader.controller;

import com.possible.patientdatabatchloader.config.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<String> runJob(@PathVariable String fileName) {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put(Constants.JOB_PARAM_FILE_NAME, new JobParameter(fileName));
        try {
            jobLauncher.run(job, new JobParameters(parameterMap));
        } catch (Exception e) {
            return new ResponseEntity<>("Failure: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
