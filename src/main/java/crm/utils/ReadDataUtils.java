package crm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cloud-native utility for reading files from Amazon S3 instead of local file system.
 * This eliminates hard-coded file path dependencies and makes the application cloud-ready.
 */
@Component
public class ReadDataUtils {

    @Value("${aws.s3.bucket.name:default-bucket}")
    private String bucketName;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    private S3Client s3Client;

    /**
     * Initialize S3 client with default credentials provider.
     * Uses IAM roles in cloud environments (ECS, EKS, Lambda).
     */
    private S3Client getS3Client() {
        if (s3Client == null) {
            s3Client = S3Client.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }
        return s3Client;
    }

    /**
     * Read file from S3 bucket by key.
     * Replaces local file system read operations.
     *
     * @param s3Key The S3 object key (file path in bucket)
     * @return InputStream of the file content
     */
    public InputStream readFileFromS3(String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = getS3Client().getObject(getObjectRequest);
            
            // Read the entire stream into memory to avoid connection issues
            byte[] content = s3Object.readAllBytes();
            s3Object.close();
            
            return new ByteArrayInputStream(content);
        } catch (Exception e) {
            throw new IOException("Failed to read file from S3: " + s3Key, e);
        }
    }

    /**
     * List files in S3 bucket with optional prefix filter.
     * Replaces file system directory listing.
     *
     * @param prefix Optional prefix to filter objects (e.g., "uploads/csv/")
     * @return List of S3 object keys matching the prefix
     */
    public List<String> listFilesInS3(String prefix) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        return getS3Client().listObjectsV2(listRequest)
                .contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    /**
     * List files in S3 bucket with extension filter.
     * Replaces file chooser dialog functionality for cloud environments.
     *
     * @param prefix Directory prefix in S3
     * @param fileExtension File extension to filter (e.g., "csv", "pdf")
     * @return List of S3 object keys with matching extension
     */
    public List<String> listFilesByExtension(String prefix, String fileExtension) {
        return listFilesInS3(prefix).stream()
                .filter(key -> key.toLowerCase().endsWith("." + fileExtension.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Check if a file exists in S3.
     *
     * @param s3Key The S3 object key
     * @return true if the object exists, false otherwise
     */
    public boolean fileExistsInS3(String s3Key) {
        try {
            getS3Client().headObject(builder -> builder.bucket(bucketName).key(s3Key));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Close S3 client resources.
     */
    public void close() {
        if (s3Client != null) {
            s3Client.close();
        }
    }
}
