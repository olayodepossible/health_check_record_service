package com.possible.patientdatabatchloader.config;

import com.possible.patientdatabatchloader.dto.PatientDto;
import com.possible.patientdatabatchloader.model.PatientRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

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
//
//    @Bean
//    public Step step(ItemReader<PatientDto> itemReader, StepBuilderFactory stepBuilderFactory) throws Exception {
//      return   stepBuilderFactory
//            .get(Constants.STEP_NAME)
//            .<PatientDto, PatientDto>chunk(2)
//            .reader(itemReader)
//            .processor(processor())
//            .writer(writer())
//            .build();
//    }

    @Bean
    public Step step(ItemReader<PatientDto> itemReader, StepBuilderFactory stepBuilderFactory,
                     Function<PatientDto, PatientRecord> processor) throws Exception {
        return stepBuilderFactory
                .get(Constants.STEP_NAME)
                .<PatientDto, PatientRecord>chunk(2)
                .reader(itemReader)
                .processor(processor)
                .writer(writer())
                .build();
    }
    /*
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

     */

    @Bean
    @StepScope
    public FlatFileItemReader<PatientDto> reader(
            @Value("#{jobParameters['" + Constants.JOB_PARAM_FILE_NAME + "']}")String fileName,
            ApplicationProperties applicationProperties) {
        return new FlatFileItemReaderBuilder<PatientDto>()
                .name(Constants.ITEM_READER_NAME)
                .resource(
                        new PathResource( Paths.get(applicationProperties.getBatchInputData().getInputPath() + File.separator + fileName))
                )
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .build();
    }

    @Bean
    public LineMapper<PatientDto> lineMapper() {
        DefaultLineMapper<PatientDto> mapper = new DefaultLineMapper<>();
        mapper.setFieldSetMapper((fieldSet) -> new PatientDto(
                fieldSet.readString(0), fieldSet.readString(1),
                fieldSet.readString(2), fieldSet.readString(3),
                fieldSet.readString(4), fieldSet.readString(5),
                fieldSet.readString(6), fieldSet.readString(7),
                fieldSet.readString(8), fieldSet.readString(9),
                fieldSet.readString(10), fieldSet.readString(11),
                fieldSet.readString(12)));
        mapper.setLineTokenizer(new DelimitedLineTokenizer());
        return mapper;
    }

    //Processor
    @Bean
    @StepScope
    public Function<PatientDto, PatientRecord> processor() {
        return (patientDto) -> PatientRecord.builder()
                .sourceId(patientDto.getSourceId())
                .firstName(patientDto.getFirstName())
                .middleInitial(patientDto.getMiddleInitial())
                .lastName(patientDto.getLastName())
                .emailAddress(patientDto.getEmailAddress())
                .phoneNumber(patientDto.getPhoneNumber())
                .street(patientDto.getStreet())
                .city(patientDto.getCity())
                .state(patientDto.getState())
                .zipCode(patientDto.getZip())
                .birthDate(LocalDate.parse(patientDto.getBirthDate(), DateTimeFormatter.ofPattern("M/dd/yyyy")))
                .socialSecurityNumber(patientDto.getSsn())
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<PatientRecord> writer() {
        return items -> {
            for (PatientRecord patientRecord : items) {
                System.err.println("Writing item: " + patientRecord.toString());
            }
        };
    }

}
