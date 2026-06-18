package crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 Configuration for cloud-native storage.
 * Configures S3 client with default credentials provider for AWS environments.
 * 
 * The S3Client will automatically use:
 * - Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
 * - EC2 instance profile credentials
 * - ECS task role credentials
 * - AWS credentials file (~/.aws/credentials)
 */
@Configuration
public class AwsS3Config {

    private static final String AWS_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(AWS_REGION))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
