package org.squidmin.java.spring.aws.javasprings3reference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.squidmin.java.spring.aws.javasprings3reference.service.S3Service;

import java.nio.file.Paths;

@SpringBootApplication
public class JavaSpringS3ReferenceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(JavaSpringS3ReferenceApplication.class, args);

        appContext.getBean(S3Service.class).uploadFile("test.csv", Paths.get("src/main/resources/test.csv"));
    }

}
