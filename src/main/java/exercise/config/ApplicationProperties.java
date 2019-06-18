package exercise.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "parkingtoll", ignoreUnknownFields = true)
public class ApplicationProperties {
}
