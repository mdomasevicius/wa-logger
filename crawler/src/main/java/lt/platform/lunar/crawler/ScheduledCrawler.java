package lt.platform.lunar.crawler;

import com.github.javafaker.Faker;
import lt.lunar.platform.logger.url.CrawlURLResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
class ScheduledCrawler {

    private static final Logger log = LoggerFactory.getLogger(ScheduledCrawler.class);
    private final RestOperations restOperations;
    private final Faker faker = new Faker();
    private final String crawlerId = faker.superhero().name();

    ScheduledCrawler(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Scheduled(initialDelay = 1_000, fixedDelay = 2_000)
    void doCrawl() {
        String fakeUrl = faker.internet().url();
        log.info("[" + crawlerId + "] scanning [" + fakeUrl + "]");

        CrawlURLResource crawlURLResource = new CrawlURLResource();
        crawlURLResource.setUrl(fakeUrl);
        ResponseEntity<CrawlURLResource> response = restOperations.postForEntity(
            "/api/url",
            crawlURLResource,
            CrawlURLResource.class);
        log.info("done");
    }

}
