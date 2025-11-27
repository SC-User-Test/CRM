package crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.sql.DataSource;
import java.sql.Connection;

@EntityScan(
        basePackageClasses = {CrmApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

    @Bean
    public HealthIndicator databaseHealthIndicator(DataSource dataSource) {
        return () -> {
            try (Connection connection = dataSource.getConnection()) {
                if (connection.isValid(1)) {
                    return Health.up()
                            .withDetail("database", "Available")
                            .withDetail("validationQuery", "Connection valid")
                            .build();
                } else {
                    return Health.down()
                            .withDetail("database", "Connection validation failed")
                            .build();
                }
            } catch (Exception e) {
                return Health.down()
                        .withDetail("database", "Unavailable")
                        .withDetail("error", e.getMessage())
                        .build();
            }
        };
    }

    @Bean
    public HealthIndicator fileSystemHealthIndicator() {
        return () -> {
            try {
                String tmpDir = System.getProperty("java.io.tmpdir");
                java.io.File tempDir = new java.io.File(tmpDir);
                if (tempDir.exists() && tempDir.canWrite()) {
                    return Health.up()
                            .withDetail("fileSystem", "Available")
                            .withDetail("tmpDir", tmpDir)
                            .build();
                } else {
                    return Health.down()
                            .withDetail("fileSystem", "Temp directory not writable")
                            .withDetail("tmpDir", tmpDir)
                            .build();
                }
            } catch (Exception e) {
                return Health.down()
                        .withDetail("fileSystem", "Error checking file system")
                        .withDetail("error", e.getMessage())
                        .build();
            }
        };
    }

}
