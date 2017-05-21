package lt.platform.lunar.crawler;

import com.github.javafaker.Faker;
import lt.platform.lunar.logger.CollectionResource;
import lt.platform.lunar.logger.celebrities.CelebrityResource;
import lt.platform.lunar.logger.key.RemoteKeyResource;
import lt.platform.lunar.logger.url.CrawlURLResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.http.HttpMethod.GET;

@Component
class ScheduledCrawler {

    private static final Logger log = LoggerFactory.getLogger(ScheduledCrawler.class);
    private final RestOperations restOperations;
    private final Faker faker = new Faker();

    ScheduledCrawler(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Scheduled(initialDelay = 5_000, fixedDelay = 2_000)
    void doCrawl() {
        CrawlURLResource urlResource = tryToRegisterURL(faker.internet().url());
        sleep();

        collectAndLogCelebrities(urlResource.getId());
        sleep();

        randomlyInquireForUnfinishedCrawlJobs();
        sleep();

        randomlyInquireForCelebrities(urlResource);
        sleep();

        registerUrlRemoteKey(urlResource.getId());
        sleep();
    }

    private void randomlyInquireForCelebrities(CrawlURLResource crawlUrl) {
        if (faker.random().nextBoolean()) {
            log.info("Inquiring celebrity list for {}", crawlUrl.getUrl());
            ResponseEntity<CollectionResource> response = restOperations.getForEntity(
                String.format("/api/url/%d/celebrities", crawlUrl.getId()),
                CollectionResource.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                int celebrityCount = response.getBody().getEntries().size();
                log.info("Celebrity list for url {} retrieved. Size: {}", crawlUrl.getUrl(), celebrityCount);
            } else {
                log.warn("Failed to inquire celebrity list for url: {}", crawlUrl.getUrl());
            }
        }
    }

    private void randomlyInquireForUnfinishedCrawlJobs() {
        if (faker.random().nextBoolean()) {
            log.info("Inquiring for unfinished jobs");
            ResponseEntity<CollectionResource> response = restOperations.exchange(
                "/api/url",
                GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<CollectionResource>() {});

            if (response.getStatusCode().is2xxSuccessful()) {
                int unfinishedJobCount = response.getBody().getEntries().size();
                log.info("There are currently {} unfinished jobs", unfinishedJobCount);
            } else {
                log.warn("Failed to inquire for unfinished jobs");
            }
        }
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
            log.warn("Failed to retrieve saved url.");
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

    private void collectAndLogCelebrities(Long id) {
        for (int i = 0; i < faker.random().nextInt(10); i++) {
            CelebrityResource celebrity = new CelebrityResource();
            celebrity.setFirstName(faker.name().firstName());
            celebrity.setLastName(faker.name().lastName());
            celebrity.setAddress(faker.address().fullAddress());

            log.info("Logging celebrity: {} {}", celebrity.getFirstName(), celebrity.getLastName());
            ResponseEntity<Object> response = restOperations.postForEntity(
                String.format("/api/url/%d/celebrities", id),
                celebrity,
                Object.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Logging complete!");
            } else {
                log.warn("Logging failed!");
            }
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
        } else {
            log.warn("Failed to register remote key for urlId - {}", urlId);
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
