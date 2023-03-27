package com.zhuxingyi;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.InputStream;

/**
 * @author zhuxingyi
 * @date 27/3/2023 下午3:48
 */
public class S3Client {
    private static final software.amazon.awssdk.services.s3.S3Client s3;

    static {
        AwsCredentials credentials = AwsBasicCredentials.create("12", "1212");
        s3 = software.amazon.awssdk.services.s3.S3Client.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public void uploadImage(String bucketName, String key, File file) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName).key(key)
                .build();
        RequestBody requestBody = RequestBody.fromFile(file);
        s3.putObject(request, requestBody);
    }

    public InputStream downloadImage(String bucketName, String key) {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName)
                .key(key).build();
        return s3.getObjectAsBytes(request).asInputStream();
    }
}
