package org.squidmin.java.spring.aws.javasprings3reference.testconfig;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.squidmin.java.spring.aws.javasprings3reference.config.S3Config;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {

    @Bean
    public S3Client s3ClientMock() { return Mockito.mock(S3Client.class); }

    @Bean
    public S3Config s3ConfigMock() { return Mockito.mock(S3Config.class); }

}
