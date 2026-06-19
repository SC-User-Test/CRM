package crm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cloud-native utility for reading data from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
@Component
public class ReadDataUtils {

    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket.name:default-bucket}")
    private String bucketName;

    public ReadDataUtils(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Read file from S3 bucket by key.
     * 
     * @param s3Key The S3 object key (path within bucket)
     * @return InputStream of the S3 object
     */
    public InputStream readFileFromS3(String s3Key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();
        
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        return s3Object;
    }

    /**
     * List files in S3 bucket with optional prefix filter.
     * 
     * @param prefix Optional prefix to filter objects (e.g., "uploads/")
     * @param fileExtension Optional file extension filter (e.g., "csv", "pdf")
     * @return List of S3 object keys matching the criteria
     */
    public List<String> listFilesFromS3(String prefix, String fileExtension) {
        ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
                .bucket(bucketName);
        
        if (prefix != null && !prefix.isEmpty()) {
            requestBuilder.prefix(prefix);
        }
        
        ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());
        
        List<String> keys = response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
        
        // Filter by extension if provided
        if (fileExtension != null && !fileExtension.isEmpty()) {
            keys = keys.stream()
                    .filter(key -> key.toLowerCase().endsWith("." + fileExtension.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return keys;
    }

    /**
     * Read file from S3 with custom bucket name.
     * 
     * @param bucketName The S3 bucket name
     * @param s3Key The S3 object key
     * @return InputStream of the S3 object
     */
    public InputStream readFileFromS3(String bucketName, String s3Key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();
        
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        return s3Object;
    }
}
