package lt.platform.lunar.logger.url;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawlURLRepository extends CrudRepository<CrawlURL, Long> {

    CrawlURL findByUrl(String url);

    @Query("select c from CrawlURL c where c.remoteKey is null or c.celebrities is empty")
    List<CrawlURL> findAllUnfinished();
}
