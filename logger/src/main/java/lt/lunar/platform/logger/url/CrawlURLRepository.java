package lt.lunar.platform.logger.url;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface CrawlURLRepository extends CrudRepository<CrawlURL, Long> {

    CrawlURL findByCrawlerIdAndUrl(String crawlerId, String url);

    CrawlURL findByCrawlerIdAndId(String crawlerId, Long id);

    @Query("select count(c) > 0 from CrawlURL c where c.url = :url")
    boolean existsByUrl(@Param("url") String url);
}
