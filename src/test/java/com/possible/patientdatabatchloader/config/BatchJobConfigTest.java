package com.possible.patientdatabatchloader.config;

import com.possible.patientdatabatchloader.PatientDataBatchLoaderApplication;
import com.possible.patientdatabatchloader.dto.PatientDto;
import com.possible.patientdatabatchloader.model.PatientRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PatientDataBatchLoaderApplication.class)
@ActiveProfiles("dev")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class})
public class BatchJobConfigTest {
    @Autowired
    private Job job;

    @Autowired
    private FlatFileItemReader<PatientDto> reader;
    @Autowired
    private Function<PatientDto, PatientRecord> processor;

    private JobParameters jobParameters;

    @Before
    public void setUp() {
        Map<String, JobParameter> params = new HashMap<>();
        params.put(Constants.JOB_PARAM_FILE_NAME, new JobParameter("test-unit-testing.csv"));
        jobParameters = new JobParameters(params);
    }

    @Test
    public void test(){
        assertNotNull(job);
        assertEquals(Constants.JOB_NAME, job.getName());
    }

    @Test
    public void testReader() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);
        int count = 0;
        try {
            count = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
                int numPatients = 0;
                PatientDto patient;
                try {
                    reader.open(stepExecution.getExecutionContext());
                    while ((patient = reader.read()) != null) {
                        assertNotNull(patient);
                        assertEquals("72739d22-3c12-539b-b3c2-13d9d4224d40", patient.getSourceId());
                        assertEquals("Hettie", patient.getFirstName());
                        assertEquals("P", patient.getMiddleInitial());
                        assertEquals("Schmidt", patient.getLastName());
                        assertEquals("rodo@uge.li", patient.getEmailAddress());
                        assertEquals("(805) 384-3727", patient.getPhoneNumber());
                        assertEquals("Hutij Terrace", patient.getStreet());
                        assertEquals("Kahgepu", patient.getCity());
                        assertEquals("ID", patient.getState());
                        assertEquals("40239", patient.getZip());
                        assertEquals("6/14/1961", patient.getBirthDate());
                        assertEquals("I", patient.getAction());
                        assertEquals("071-81-2500", patient.getSsn());
                        numPatients++;
                    }
                } finally {
                    try { reader.close(); } catch (Exception e) { fail(e.toString()); }
                }
                return numPatients;
            });
        } catch (Exception e) {
            fail(e.toString());
        }
        assertEquals(1, count);
    }

    @Test
    public void testProcessor() throws Exception {
        PatientDto patientDto = PatientDto.builder()
                .sourceId("72739d22-3c12-539b-b3c2-13d9d4224d40")
                .firstName("Hettie")
                .middleInitial("P")
                .lastName("Schmidt")
                .emailAddress("rodo@uge.li")
                .phoneNumber("(805) 384-3727")
                .street("Hutij Terrace")
                .city("Kahgepu")
                .state("ID")
                .zip("40239")
                .birthDate("6/14/1961")
                .action("I")
                .ssn("071-81-2500")
                .build();
        PatientRecord entity = processor.apply(patientDto);
        assertNotNull(entity);
        assertEquals("72739d22-3c12-539b-b3c2-13d9d4224d40", entity.getSourceId());
        assertEquals("Hettie", entity.getFirstName());
        assertEquals("P", entity.getMiddleInitial());
        assertEquals("Schmidt", entity.getLastName());
        assertEquals("rodo@uge.li", entity.getEmailAddress());
        assertEquals("(805) 384-3727", entity.getPhoneNumber());
        assertEquals("Hutij Terrace", entity.getStreet());
        assertEquals("Kahgepu", entity.getCity());
        assertEquals("ID", entity.getState());
        assertEquals("40239", entity.getZipCode());
        assertEquals(14, entity.getBirthDate().getDayOfMonth());
        assertEquals(6, entity.getBirthDate().getMonthValue());
        assertEquals(1961, entity.getBirthDate().getYear());
        assertEquals("071-81-2500", entity.getSocialSecurityNumber());
    }
}
