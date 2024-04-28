package com.seai.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.textract.TextractClient;

@Configuration
public class ScannerConfig {

    @Bean
    public TextractClient amazonTextract() {
        return TextractClient.builder().region(Region.EU_CENTRAL_1).build();
    }

    @Bean
    public S3Client amazonS3Client() {
        return S3Client.builder().region(Region.EU_CENTRAL_1).build();
    }
}
