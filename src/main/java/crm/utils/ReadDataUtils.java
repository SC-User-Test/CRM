package crm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Cloud-ready utility for reading data from Amazon S3 instead of local file system.
 * This eliminates hard-coded file path dependencies and makes the application cloud-native.
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
     * Reads a file from Amazon S3 bucket.
     * 
     * @param s3Key The S3 object key (path within the bucket)
     * @return InputStream of the file content from S3
     * @throws S3Exception if the file cannot be retrieved from S3
     */
    public InputStream readFileFromS3(String s3Key) throws S3Exception {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        return s3Object;
    }

    /**
     * Reads a file from Amazon S3 bucket with custom bucket name.
     * 
     * @param bucketName The S3 bucket name
     * @param s3Key The S3 object key (path within the bucket)
     * @return InputStream of the file content from S3
     * @throws S3Exception if the file cannot be retrieved from S3
     */
    public InputStream readFileFromS3(String bucketName, String s3Key) throws S3Exception {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        return s3Object;
    }

}
