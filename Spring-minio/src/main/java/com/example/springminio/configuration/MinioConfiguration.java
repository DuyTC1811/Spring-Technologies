package com.example.springminio.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Configuration
public class MinioConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioConfiguration.class);
    @Value("${spring.minio.url}")
    private String url;
    @Value("${spring.minio.bucket}")
    private String bucket;
    @Value("${spring.minio.access-key}")
    private String accessKey;
    @Value("${spring.minio.secret-key}")
    private String secretKey;

    @Bean
    @Primary
    public MinioAsyncClient minioAsyncClient() {
        return MinioAsyncClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public void initializationBucket() {
        CompletableFuture<Boolean> found;
        try {
            found = minioAsyncClient().bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found.get()) {
                minioAsyncClient().makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                LOGGER.info("Create bucket {} successful", bucket);
            } else {
                LOGGER.info("Bucket {} already exists.", bucket);
            }
        } catch (InsufficientDataException | InternalException | InvalidKeyException | IOException |
                 NoSuchAlgorithmException | XmlParserException | ExecutionException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}
