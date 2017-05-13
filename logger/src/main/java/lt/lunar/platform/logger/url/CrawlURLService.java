package lt.lunar.platform.logger.url;

public interface CrawlURLService {

    CrawlURLDto create(String crawlerId, CrawlURLDto crawlURL);

    CrawlURLDto findOne(String crawlerId, Long id);

    void update(CrawlURLDto crawlUrl);

    boolean exists(String url);
}
