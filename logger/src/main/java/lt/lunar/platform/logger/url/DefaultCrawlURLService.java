package lt.lunar.platform.logger.url;

import lt.lunar.platform.logger.common.NotFoundException;
import lt.lunar.platform.logger.common.RecordAlreadyExistsException;
import lt.lunar.platform.logger.key.RemoteKey;
import lt.lunar.platform.logger.key.RemoteKeyDto;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
class DefaultCrawlURLService implements CrawlURLService {

    private final CrawlURLRepository crawlURLRepository;

    DefaultCrawlURLService(CrawlURLRepository crawlURLRepository) {
        this.crawlURLRepository = crawlURLRepository;
    }

    @Override
    public CrawlURLDto create(String crawlerId, CrawlURLDto crawlURL) {
        if (crawlURLRepository.findByCrawlerIdAndUrl(crawlerId, crawlURL.getUrl()) != null) {
            throw new RecordAlreadyExistsException();
        }

        CrawlURL entity = fromDto(crawlURL);
        entity.setCrawlerId(crawlerId);
        return toDto(crawlURLRepository.save(entity));
    }

    @Override
    public CrawlURLDto findOne(String crawlerId, Long id) {
        return Optional.ofNullable(crawlURLRepository.findByCrawlerIdAndId(crawlerId, id))
            .map(DefaultCrawlURLService::toDto)
            .orElseThrow(NotFoundException::new);
    }

    @Override
    public void update(CrawlURLDto crawlUrl) {
        Assert.notNull(crawlUrl.getId(), "id must not be null");
        Assert.notNull(crawlUrl.getCrawlerId(), "crawlerId must not be null");

        crawlURLRepository.save(fromDto(crawlUrl));
    }

    private static CrawlURLDto toDto(CrawlURL crawlURL) {
        CrawlURLDto dto = new CrawlURLDto();
        dto.setId(crawlURL.getId());
        dto.setCrawlerId(crawlURL.getCrawlerId());
        dto.setUrl(crawlURL.getUrl());

        RemoteKeyDto remoteKeyDto = Optional.ofNullable(crawlURL.getRemoteKey())
            .map(k -> new RemoteKeyDto(k.getKey()))
            .orElse(null);
        dto.setRemoteKey(remoteKeyDto);

        return dto;
    }

    private static CrawlURL fromDto(CrawlURLDto dto) {
        CrawlURL url = new CrawlURL();
        url.setId(dto.getId());
        url.setCrawlerId(dto.getCrawlerId());
        url.setUrl(dto.getUrl());

        RemoteKey remoteKey = Optional.ofNullable(dto.getRemoteKey())
            .map(k -> {
                RemoteKey rk = new RemoteKey();
                rk.setKey(k.getKey());
                return rk;
            }).orElse(null);
        url.setRemoteKey(remoteKey);

        return url;
    }
}
