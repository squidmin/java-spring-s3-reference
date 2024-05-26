package org.squidmin.java.spring.aws.javasprings3reference.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.squidmin.java.spring.aws.javasprings3reference.config.S3Config;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Setter
@Slf4j
public class S3Service {

    private final S3Client s3Client;
    private final S3Config s3Config;
    private final String bucketName;

    public S3Service(S3Client s3Client, S3Config s3Config) {

        this.s3Config = s3Config;
        this.bucketName = s3Config.getBucketName();
        this.s3Client = s3Client;

    }

    public void uploadFile(String key, InputStream inputStream) throws IOException {
        key = UUID.randomUUID() + "_" + key;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));
        log.info("File uploaded to S3: {}", key);
    }

    public void uploadFile(String key, Path filePath) {
        key = UUID.randomUUID() + "_" + key;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath));
        log.info("File uploaded to S3: {}", key);
    }

    public byte[] readFile(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
            ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(getObjectRequest);
            return objectAsBytes.asByteArray();
        } catch (S3Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
