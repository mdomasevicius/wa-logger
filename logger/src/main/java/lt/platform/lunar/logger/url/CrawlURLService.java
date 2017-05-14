package lt.platform.lunar.logger.url;

import java.util.List;

public interface CrawlURLService {

    CrawlURLDto create(CrawlURLDto crawlURL);

    CrawlURLDto findOne(Long id);

    List<CrawlURLDto> findAll(boolean unfinishedOnly);
}
