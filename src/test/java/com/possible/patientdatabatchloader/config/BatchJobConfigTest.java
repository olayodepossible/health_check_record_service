package com.possible.patientdatabatchloader.config;

import com.possible.patientdatabatchloader.PatientDataBatchLoaderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PatientDataBatchLoaderApplication.class)
@ActiveProfiles("dev")
public class BatchJobConfigTest {
    @Autowired
    private Job job;

    @Test
    public void test(){
        assertNotNull(job);
        assertEquals(Constants.JOB_NAME, job.getName());
    }
}
