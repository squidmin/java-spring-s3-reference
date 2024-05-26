package org.squidmin.java.spring.aws.javasprings3reference;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
        @Value("${aws.s3.bucket}") String bucketName,
        @Value("${aws.credentials.region}") String region,
        @Value("${aws.profile}") String profile) {

        this.s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(ProfileCredentialsProvider.builder().profileName(profile).build())
            .build();

        this.bucketName = bucketName;
    }

    public void uploadFile(String key, InputStream inputStream) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));
    }

    public void uploadFile(String key, Path filePath) {
        key = UUID.randomUUID() + "_" + key;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath));

        log.info("File uploaded to S3: {}", key);
    }

}

