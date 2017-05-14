package lt.platform.lunar.logger.url;

import java.util.List;

public interface CrawlURLService {

    CrawlURLDto create(CrawlURLDto crawlURL);

    CrawlURLDto findOne(Long id);

    CrawlURLDto findOne(String url);

    void update(CrawlURLDto crawlUrl);

    boolean exists(String url);

    boolean exists(Long id);

    List<CrawlURLDto> findAll(boolean unfinishedOnly);
}
