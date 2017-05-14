package lt.platform.lunar.logger.key;

import lt.platform.lunar.logger.common.NotFoundException;
import lt.platform.lunar.logger.url.CrawlURL;
import lt.platform.lunar.logger.url.CrawlURLRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
class DefaultRemoteKeyService implements RemoteKeyService {

    private final CrawlURLRepository crawlURLRepository;

    DefaultRemoteKeyService(CrawlURLRepository crawlURLRepository) {
        this.crawlURLRepository = crawlURLRepository;
    }

    @Transactional(readOnly = true)
    public RemoteKeyDto findOne(Long urlId) {
        return Optional.ofNullable(crawlURLRepository.findOne(urlId).getRemoteKey())
            .map(k -> new RemoteKeyDto(k.getKey()))
            .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void createKeyForUrl(Long urlId, String remoteKey) {
        CrawlURL crawlUrl = Optional.ofNullable(crawlURLRepository.findOne(urlId))
            .orElseThrow(NotFoundException::new);

        crawlUrl.setRemoteKey(new RemoteKey(remoteKey));
    }

    private static RemoteKeyDto toDto(RemoteKey remoteKey) {
        return new RemoteKeyDto(remoteKey.getKey());
    }
}
