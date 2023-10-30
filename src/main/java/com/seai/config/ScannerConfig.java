package com.seai.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScannerConfig {

    @Bean
    public AmazonTextract amazonTextract() {
        return AmazonTextractClient.builder()
                .withRegion(Regions.EU_CENTRAL_1).build();
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
    }
}
