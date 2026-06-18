package crm.utils;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-native utility for reading data from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
public class ReadDataUtils {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    private final S3Client s3Client;

    public ReadDataUtils(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Reads a file from Amazon S3 and returns its content as a byte array.
     * 
     * @param s3Key The S3 object key (path within the bucket)
     * @return byte array containing the file content
     * @throws IOException if the file cannot be read
     */
    public byte[] readFileFromS3(String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            return readAllBytes(s3Object);
        } catch (S3Exception e) {
            throw new IOException("Failed to read file from S3: " + s3Key, e);
        }
    }

    /**
     * Reads a file from Amazon S3 and returns it as an InputStream.
     * 
     * @param s3Key The S3 object key (path within the bucket)
     * @return InputStream for reading the file content
     * @throws IOException if the file cannot be read
     */
    public InputStream readFileStreamFromS3(String s3Key) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            throw new IOException("Failed to read file stream from S3: " + s3Key, e);
        }
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[8192];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}
