package lt.platform.lunar.logger.url;

public interface CrawlURLService {

    CrawlURLDto create(CrawlURLDto crawlURL);

    CrawlURLDto findOne(Long id);

    void update(CrawlURLDto crawlUrl);

    boolean exists(String url);
}
