package lt.lunar.platform.logger.url;

import lt.lunar.platform.logger.common.NotFoundException;
import lt.lunar.platform.logger.common.RecordAlreadyExistsException;
import lt.lunar.platform.logger.key.RemoteKey;
import lt.lunar.platform.logger.key.RemoteKeyDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
class DefaultCrawlURLService implements CrawlURLService {

    private final CrawlURLRepository crawlURLRepository;

    DefaultCrawlURLService(CrawlURLRepository crawlURLRepository) {
        this.crawlURLRepository = crawlURLRepository;
    }

    @Transactional
    @Override
    public CrawlURLDto create(CrawlURLDto crawlURL) {
        if (crawlURLRepository.findByUrl(crawlURL.getUrl()) != null) {
            throw new RecordAlreadyExistsException();
        }

        return toDto(crawlURLRepository.save(fromDto(crawlURL)));
    }

    @Transactional(readOnly = true)
    @Override
    public CrawlURLDto findOne(Long id) {
        return Optional.ofNullable(crawlURLRepository.findOne(id))
            .map(DefaultCrawlURLService::toDto)
            .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public void update(CrawlURLDto crawlUrl) {
        Assert.notNull(crawlUrl.getId(), "id must not be null");

        crawlURLRepository.save(fromDto(crawlUrl));
    }

    @Override
    public boolean exists(String url) {
        return crawlURLRepository.existsByUrl(url);
    }

    private static CrawlURLDto toDto(CrawlURL crawlURL) {
        CrawlURLDto dto = new CrawlURLDto();
        dto.setId(crawlURL.getId());
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
