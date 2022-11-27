package com.example.springminio.services;

import com.example.springminio.configuration.MinioConfiguration;
import com.example.springminio.payload.FileResponse;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MinioServicesImpl implements MinioServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioServicesImpl.class);
    private final MinioConfiguration minioConfiguration;
    @Value("${spring.minio.bucket}")
    private String bucket;

    public MinioServicesImpl(MinioConfiguration minioConfiguration) {
        this.minioConfiguration = minioConfiguration;
    }

    @Override
    public void uploadFiles(MultipartFile[] files) {
        for (MultipartFile file : files) {
            uploads(file);
        }
    }

    private void uploads(MultipartFile file) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            minioConfiguration.minioAsyncClient().putObject(putObjectArgs);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    @Override
    public void deleteFile(String fileName) {

    }

    @Override
    public List<FileResponse> getListFile() {
        List<FileResponse> result = new ArrayList<>();
        try {
            Iterable<Result<Item>> response = minioConfiguration.minioAsyncClient().listObjects(ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> item : response) {
                result.add(FileResponse.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName()))
                        .build());
            }
            return result;
        } catch (Exception exception) {
            LOGGER.error("Happened error when get list objects from minio: ", exception);
            throw new RuntimeException(exception);
        }
    }

    private String getPreSignedUrl(String filename) {
        return "http://localhost:8080/file/".concat(filename);
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket(bucket).object(fileName).build();
            CompletableFuture<GetObjectResponse> response = minioConfiguration.minioAsyncClient().getObject(getObjectArgs);
            return IOUtils.toByteArray(response.get());
        } catch (Exception exception) {
            LOGGER.error("field does not exist {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deleteFiles(List<String> listFileName) {
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket(bucket).objects(listName(listFileName)).build();
        Iterable<Result<DeleteError>> results = minioConfiguration.minioAsyncClient().removeObjects(removeObjectsArgs);
        results.forEach(result -> {
            try {
                DeleteError deleteError = result.get();
                LOGGER.error("Error in deleting object" + deleteError.objectName() + "; " + deleteError.message());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    private List<DeleteObject> listName(List<String> listFileName) {
        return listFileName.stream().map(DeleteObject::new).collect(Collectors.toList());
    }
}
