package lt.platform.lunar.crawler;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
public class Config {

    @Bean
    RestOperations restOperations() {
        String loggerUrl = System.getenv()
            .getOrDefault("LOGGER_URL", "http://localhost:8080");

        return new RestTemplateBuilder()
            .rootUri(loggerUrl)
            .build();
    }
}
