package lt.platform.lunar.crawler;

import com.github.javafaker.Faker;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestOperations;

import java.io.IOException;

import static java.util.Collections.singletonList;

@Configuration
public class Config {

    @Bean
    RestOperations restOperations() {
        return new RestTemplateBuilder()
            .rootUri("http://localhost:8080")
            .build();
    }
}
