package org.squidmin.java.spring.aws.javasprings3reference.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Getter
public class S3Config {

    private final String bucketName;
    private final String region;
    private final String profile;

    public S3Config(@Value("${aws.s3.bucket}") String bucketName,
                    @Value("${aws.credentials.region}") String region,
                    @Value("${aws.profile}") String profile) {

        this.bucketName = bucketName;
        this.region = region;
        this.profile = profile;

    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(ProfileCredentialsProvider.builder().profileName(profile).build())
            .build();
    }

}
