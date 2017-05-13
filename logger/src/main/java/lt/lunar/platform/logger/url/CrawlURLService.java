package lt.lunar.platform.logger.url;

interface CrawlURLService {

    CrawlURLDto create(String crawlerId, CrawlURLDto crawlURL);

    CrawlURLDto findOne(String crawlerId, Long id);
}
