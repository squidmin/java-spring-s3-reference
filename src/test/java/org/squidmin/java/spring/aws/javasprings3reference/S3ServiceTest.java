package org.squidmin.java.spring.aws.javasprings3reference;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.squidmin.java.spring.aws.javasprings3reference.config.S3Config;
import org.squidmin.java.spring.aws.javasprings3reference.service.S3Service;
import org.squidmin.java.spring.aws.javasprings3reference.testconfig.TestConfig;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
public class S3ServiceTest {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.credentials.region}")
    private String region;

    @Value("${aws.profile}")
    private String profile;

    @Autowired
    private S3Client s3ClientMock;

    @Autowired
    private S3Config s3ConfigMock;

    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        Mockito.when(s3ConfigMock.getBucketName()).thenReturn(bucketName);
        Mockito.when(s3ConfigMock.getRegion()).thenReturn(region);
        Mockito.when(s3ConfigMock.getProfile()).thenReturn(profile);

        Mockito.when(
            s3ClientMock.putObject(
                ArgumentMatchers.any(PutObjectRequest.class),
                ArgumentMatchers.any(RequestBody.class)
            )
        ).thenReturn(PutObjectResponse.builder().sseCustomerKeyMD5("").build());

        s3Service = new S3Service(s3ClientMock, s3ConfigMock);
    }

    @Test
    void testUploadFileWithInputStream() throws IOException {
        ;
        s3Service.uploadFile("test.csv", new ByteArrayInputStream("test content".getBytes()));

        ArgumentCaptor<PutObjectRequest> putObjectRequestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        Mockito.verify(
            s3ClientMock,
            Mockito.times(1)).putObject(putObjectRequestCaptor.capture(),
            ArgumentMatchers.any(RequestBody.class)
        );

        PutObjectRequest putObjectRequest = putObjectRequestCaptor.getValue();
        Assertions.assertTrue(putObjectRequest.key().endsWith("test.csv"));
        Assertions.assertEquals(bucketName, putObjectRequest.bucket());
    }

    @Test
    void testUploadFileWithPath() {
        String key = "test.csv";

        s3Service.uploadFile(key, Paths.get("src/test/resources/test.csv"));

        ArgumentCaptor<PutObjectRequest> putObjectRequestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        Mockito.verify(
            s3ClientMock,
            Mockito.times(1)).putObject(putObjectRequestCaptor.capture(),
            ArgumentMatchers.any(RequestBody.class)
        );

        PutObjectRequest putObjectRequest = putObjectRequestCaptor.getValue();
        Assertions.assertTrue(putObjectRequest.key().contains(key));
        Assertions.assertEquals(bucketName, putObjectRequest.bucket());
    }

    @Test
    void testReadFile() {
        String key = "test.csv";
        byte[] content = "test content".getBytes();
        ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes
            .fromByteArray(GetObjectResponse.builder().build(), content);

        Mockito.when(s3ClientMock.getObjectAsBytes(Mockito.any(GetObjectRequest.class))).thenReturn(responseBytes);

        ArgumentCaptor<GetObjectRequest> getObjectRequestCaptor = ArgumentCaptor.forClass(GetObjectRequest.class);
        Mockito.verify(s3ClientMock, Mockito.times(1))
            .getObjectAsBytes(getObjectRequestCaptor.capture());

        GetObjectRequest getObjectRequest = getObjectRequestCaptor.getValue();
        Assertions.assertEquals(key, getObjectRequest.key());
        Assertions.assertEquals(bucketName, getObjectRequest.bucket());
        Assertions.assertArrayEquals(content, s3Service.readFile(key));
    }

    @Test
    void testReadFileException() {
        ;
        Mockito.when(s3ClientMock.getObjectAsBytes(ArgumentMatchers.any(GetObjectRequest.class)))
            .thenThrow(S3Exception.class);
        Assertions.assertNull(s3Service.readFile("test.csv"));
    }

}
