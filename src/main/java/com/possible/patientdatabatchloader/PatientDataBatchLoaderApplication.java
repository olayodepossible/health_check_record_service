package com.possible.patientdatabatchloader;

import com.possible.patientdatabatchloader.config.ApplicationProperties;
import com.possible.patientdatabatchloader.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;


@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
public class PatientDataBatchLoaderApplication {

    private static final Logger log = LoggerFactory.getLogger(PatientDataBatchLoaderApplication.class);

    private final Environment env;

    public PatientDataBatchLoaderApplication(Environment env) {
        this.env = env;
    }

    /**
     * Initializes PatientBatchLoader.
     * <p>
     * Spring profiles can be configured with a program arguments
     * --spring.profiles.active=your-active-profile
     * <p>
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT)
                && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run "
                    + "with both the 'dev' and 'prod' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args
     *            the command line arguments
     * @throws UnknownHostException
     *             if the local host name could not be resolved into an address
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PatientDataBatchLoaderApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Spring Batch Application '{}' is running!\n\t"
                        + "Active Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), env.getActiveProfiles());
    }

    /*

        @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
        ...
            @Bean
            public PlatformTransactionManager transactionManager() {
                return new ResourcelessTransactionManager();
            }
        }
     */
}

