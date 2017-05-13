package lt.lunar.platform.logger.url;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CrawlURLRepository extends CrudRepository<CrawlURL, Long> {

    CrawlURL findByCrawlerIdAndUrl(String crawlerId, String url);
}
