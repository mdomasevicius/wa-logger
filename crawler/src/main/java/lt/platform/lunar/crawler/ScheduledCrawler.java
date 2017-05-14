package lt.platform.lunar.crawler;

import com.github.javafaker.Faker;
import lt.platform.lunar.logger.celebrities.CelebrityResource;
import lt.platform.lunar.logger.key.RemoteKeyResource;
import lt.platform.lunar.logger.url.CrawlURLResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
class ScheduledCrawler {

    private static final Logger log = LoggerFactory.getLogger(ScheduledCrawler.class);
    private final RestOperations restOperations;
    private final Faker faker = new Faker();

    ScheduledCrawler(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Scheduled(initialDelay = 1_000, fixedDelay = 2_000)
    void doCrawl() {
        CrawlURLResource urlResource = tryToRegisterURL(faker.internet().url());
        sleep();

        /*collectAndLogCelebrities(urlResource.getUrl());
        sleep();*/ //todo to implement

        registerUrlRemoteKey(urlResource.getId());
        sleep();
    }

    private CrawlURLResource tryToRegisterURL(String url) {
        log.info("Attempting to register: {}", url);

        CrawlURLResource crawlURLResource = new CrawlURLResource();
        crawlURLResource.setUrl(url);
        ResponseEntity<CrawlURLResource> createResponse = restOperations.postForEntity(
            "/api/url",
            crawlURLResource,
            CrawlURLResource.class);

        if (!createResponse.getStatusCode().is2xxSuccessful()) {
            log.warn("{} already crawled", url);
        }

        String createdLocation = createResponse.getHeaders()
            .get("Location")
            .stream()
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        ResponseEntity<CrawlURLResource> createdURLResource = restOperations.getForEntity(
            getResourcePathOrFail(createdLocation),
            CrawlURLResource.class
        );

        if (!createdURLResource.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to retrieve saved url.");
            throw new IllegalStateException();
        }

        log.info("{} registered successfully", url);
        return createdURLResource.getBody();
    }

    private static String getResourcePathOrFail(String createdLocation) {
        try {
            return new URL(createdLocation).getPath();
        } catch (MalformedURLException e) {
            log.error("Could not parse created resource location path.", e);
            throw new IllegalArgumentException();
        }
    }

    private void collectAndLogCelebrities(String url) {
        List<CelebrityResource> collectedCelebrities = new ArrayList<>();

        for (int i = 0; i < faker.random().nextInt(10); i++) {
            CelebrityResource celebrity = new CelebrityResource();
            celebrity.setSourceUrl(url);
            celebrity.setFirstName(faker.name().firstName());
            celebrity.setLastName(faker.name().lastName());
            celebrity.setAddress(faker.address().fullAddress());
            collectedCelebrities.add(celebrity);
        }

        log.info("Logging {} celebrities.", collectedCelebrities);
        ResponseEntity<Object> response = restOperations.postForEntity(
            "/api/celebrities",
            collectedCelebrities,
            Object.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Logging complete!");
        } else {
            log.warn("Logging failed!");
        }
    }

    private void registerUrlRemoteKey(Long urlId) {
        log.info("Attempting to register remote key for url id {}", urlId);
        RemoteKeyResource remoteKey = new RemoteKeyResource();
        remoteKey.setRemoteKey(faker.chuckNorris().fact());

        ResponseEntity<Object> response = restOperations.postForEntity(
            String.format("/api/url/%d/remote-key", urlId),
            remoteKey,
            Object.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Remote key for url id {} - registered", urlId);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(faker.random().nextInt(2000) + 1000);
        } catch (InterruptedException e) {
            log.error("Crash", e);
        }
    }

}
