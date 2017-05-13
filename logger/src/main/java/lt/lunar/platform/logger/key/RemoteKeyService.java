package lt.lunar.platform.logger.key;

import lt.lunar.platform.logger.common.NotFoundException;
import lt.lunar.platform.logger.url.CrawlURLDto;
import lt.lunar.platform.logger.url.CrawlURLService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RemoteKeyService {

    private final CrawlURLService crawlURLService;

    public RemoteKeyService(CrawlURLService crawlURLService) {
        this.crawlURLService = crawlURLService;
    }

    RemoteKeyDto findOne(String crawlerId, Long urlId) {
        return Optional.ofNullable(crawlURLService.findOne(crawlerId, urlId).getRemoteKey())
            .orElseThrow(NotFoundException::new);
    }

    void createKeyForUrl(String crawlerId, Long urlId, String remoteKey) {
        CrawlURLDto crawlUrl = crawlURLService.findOne(crawlerId, urlId);
        crawlUrl.setRemoteKey(new RemoteKeyDto(remoteKey));
        crawlURLService.update(crawlUrl);
    }

    private static RemoteKeyDto toDto(RemoteKey remoteKey) {
        return new RemoteKeyDto(remoteKey.getKey());
    }
}
